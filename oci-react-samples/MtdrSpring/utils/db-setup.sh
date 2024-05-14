#!/bin/bash
# Copyright (c) 2022 Oracle and/or its affiliates.
# Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.

# Fail on error
set -e


# Create Object Store Bucket (Should be replaced by terraform one day)
while ! state_done OBJECT_STORE_BUCKET; do
  echo "Checking object storage bucket"
#  oci os bucket create --compartment-id "$(state_get COMPARTMENT_OCID)" --name "$(state_get RUN_NAME)"
  if oci os bucket get --name "$(state_get RUN_NAME)-$(state_get MTDR_KEY)"; then
    state_set_done OBJECT_STORE_BUCKET
    echo "finished checking object storage bucket"
  fi
done


# Wait for Order DB OCID
while ! state_done MTDR_DB_OCID; do
  echo "`date`: Waiting for MTDR_DB_OCID"
  sleep 2
done


# Get Wallet
while ! state_done WALLET_GET; do
  echo "creating wallet"
  cd $MTDRWORKSHOP_LOCATION
  mkdir wallet
  cd wallet
  oci db autonomous-database generate-wallet --autonomous-database-id "$(state_get MTDR_DB_OCID)" --file 'wallet.zip' --password 'Welcome1' --generate-type 'ALL'
  unzip wallet.zip
  cd $MTDRWORKSHOP_LOCATION
  state_set_done WALLET_GET
  echo "finished creating wallet"
done


# Get DB Connection Wallet and to Object Store
while ! state_done CWALLET_SSO_OBJECT; do
  echo "grabbing wallet"
  cd $MTDRWORKSHOP_LOCATION/wallet
  oci os object put --bucket-name "$(state_get RUN_NAME)-$(state_get MTDR_KEY)" --name "cwallet.sso" --file 'cwallet.sso'
  cd $MTDRWORKSHOP_LOCATION
  state_set_done CWALLET_SSO_OBJECT
  echo "done grabbing wallet"
done


# Create Authenticated Link to Wallet
while ! state_done CWALLET_SSO_AUTH_URL; do
  echo "creating authenticated link to wallet"
  ACCESS_URI=`oci os preauth-request create --object-name 'cwallet.sso' --access-type 'ObjectRead' --bucket-name "$(state_get RUN_NAME)-$(state_get MTDR_KEY)" --name 'mtdrworkshop' --time-expires $(date '+%Y-%m-%d' --date '+7 days') --query 'data."access-uri"' --raw-output`
  state_set CWALLET_SSO_AUTH_URL "https://objectstorage.$(state_get REGION).oraclecloud.com${ACCESS_URI}"
  echo "done creating authenticated link to wallet"
done


# Give DB_PASSWORD priority
while ! state_done DB_PASSWORD; do
  echo "Waiting for DB_PASSWORD"
  sleep 5
done


# Create Inventory ATP Bindings
while ! state_done DB_WALLET_SECRET; do
  echo "creating Inventory ATP Bindings"
  cd $MTDRWORKSHOP_LOCATION/wallet
  cat - >sqlnet.ora <<!
WALLET_LOCATION = (SOURCE = (METHOD = file) (METHOD_DATA = (DIRECTORY="/mtdrworkshop/creds")))
SSL_SERVER_DN_MATCH=yes
!
  if kubectl create -f - -n mtdrworkshop; then
    state_set_done DB_WALLET_SECRET
  else
    echo "Error: Failure to create db-wallet-secret.  Retrying..."
    sleep 5
  fi <<!
apiVersion: v1
data:
  README: $(base64 -w0 README)
  cwallet.sso: $(base64 -w0 cwallet.sso)
  ewallet.p12: $(base64 -w0 ewallet.p12)
  keystore.jks: $(base64 -w0 keystore.jks)
  ojdbc.properties: $(base64 -w0 ojdbc.properties)
  sqlnet.ora: $(base64 -w0 sqlnet.ora)
  tnsnames.ora: $(base64 -w0 tnsnames.ora)
  truststore.jks: $(base64 -w0 truststore.jks)
kind: Secret
metadata:
  name: db-wallet-secret
!
  cd $MTDRWORKSHOP_LOCATION
done


# DB Connection Setup
export TNS_ADMIN=$MTDRWORKSHOP_LOCATION/wallet
cat - >$TNS_ADMIN/sqlnet.ora <<!
WALLET_LOCATION = (SOURCE = (METHOD = file) (METHOD_DATA = (DIRECTORY="$TNS_ADMIN")))
SSL_SERVER_DN_MATCH=yes
!
MTDR_DB_SVC="$(state_get MTDR_DB_NAME)_tp"
TODO_USER=TODOUSER
ORDER_LINK=ORDERTOINVENTORYLINK
ORDER_QUEUE=ORDERQUEUE


# Get DB Password
while true; do
  if DB_PASSWORD=`kubectl get secret dbuser -n mtdrworkshop --template={{.data.dbpassword}} | base64 --decode`; then
    if ! test -z "$DB_PASSWORD"; then
      break
    fi
  fi
  echo "Error: Failed to get DB password.  Retrying..."
  sleep 5
done


# Wait for DB Password to be set in Order DB
while ! state_done MTDR_DB_PASSWORD_SET; do
  echo "`date`: Waiting for MTDR_DB_PASSWORD_SET"
  sleep 2
done


# Order DB User, Objects
while ! state_done TODO_USER; do
  echo "connecting to mtdr database"
  U=$TODO_USER
  SVC=$MTDR_DB_SVC
  sqlplus /nolog <<!
WHENEVER SQLERROR EXIT 1
WHENEVER OSERROR EXIT 9
connect admin/"$DB_PASSWORD"@$SVC
CREATE USER $U IDENTIFIED BY "$DB_PASSWORD" DEFAULT TABLESPACE data QUOTA UNLIMITED ON data;
GRANT CREATE SESSION, CREATE VIEW, CREATE SEQUENCE, CREATE PROCEDURE TO $U;
GRANT CREATE TABLE, CREATE TRIGGER, CREATE TYPE, CREATE MATERIALIZED VIEW TO $U;
GRANT CONNECT, RESOURCE, pdb_dba, SODA_APP to $U;
CREATE TABLE TODOUSER.todoitem (id NUMBER GENERATED ALWAYS AS IDENTITY, description VARCHAR2(4000), creation_ts TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, done NUMBER(1,0) , PRIMARY KEY (id));
insert into TODOUSER.todoitem  (description, done) values ('Manual item insert', 0);

CREATE TABLE $U.TASKSTATUS (
    TaskStatusId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name NVARCHAR2(100),
    Description NVARCHAR2(200)
);

CREATE TABLE $U.CONVERSATION (
    ConversationId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    StartTime TIMESTAMP,
    EndTime TIMESTAMP
);

CREATE TABLE $U.PROJECT (
    ProjectId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name NVARCHAR2(100),
    Description NVARCHAR2(500)
);

CREATE TABLE $U.UPDATETYPE (
    UpdateTypeId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name NVARCHAR2(100),
    Description NVARCHAR2(200)
);

CREATE TABLE $U.USERTYPE (
    UserTypeId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name NVARCHAR2(100),
    Description NVARCHAR2(200)
);

CREATE TABLE $U.TEAMTYPE (
    TeamTypeId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name NVARCHAR2(100),
    Description NVARCHAR2(200)
);

CREATE TABLE $U.TEAM (
    TeamId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name NVARCHAR2(100),
    Description NVARCHAR2(200),
    TeamTypeId NUMBER REFERENCES $U.TEAMTYPE(TeamTypeId)
);

CREATE TABLE $U.TELEGRAMUSER (
    TelegramUserId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name NVARCHAR2(100),
    Email NVARCHAR2(100),
    PhoneNumber NVARCHAR2(15),
    TelegramName NVARCHAR2(100),
    UserTypeId NUMBER REFERENCES $U.USERTYPE(UserTypeId)
    );

CREATE TABLE $U.USERTEAM (
    UserTeamId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TelegramUserId NUMBER REFERENCES $U.TELEGRAMUSER(TelegramUserId),
    TeamId NUMBER REFERENCES $U.TEAM (TeamId)
);

CREATE TABLE $U.SPRINT (
    SprintId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name NVARCHAR2(100),
    Description NVARCHAR2(500),
    StartDate DATE,
    EndDate DATE,
    ProjectId NUMBER REFERENCES $U.PROJECT(ProjectId)
);

CREATE TABLE $U.TASK (
    TaskId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name NVARCHAR2(100),
    Description NVARCHAR2(200),
    EstimatedHours FLOAT,
    Priority NUMBER,
    TelegramUserId NUMBER REFERENCES $U.TELEGRAMUSER(TelegramUserId),
    SprintId NUMBER REFERENCES $U.SPRINT(SprintId),
    TaskStatusId NUMBER REFERENCES $U.TASKSTATUS(TaskStatusId)
);

CREATE TABLE $U.TASKUPDATE (
    TaskUpdateId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TimeStamp TIMESTAMP,
    UpdateTypeId NUMBER REFERENCES $U.UPDATETYPE(UpdateTypeId),
    TaskId NUMBER REFERENCES $U.TASK(TaskId),
    TelegramUserId NUMBER REFERENCES $U.TELEGRAMUSER(TelegramUserId)
);

CREATE TABLE $U.SPRINTUPDATE (
    SprintUpdateId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    TimeStamp TIMESTAMP,
    UpdateTypeId NUMBER REFERENCES $U.UPDATETYPE(UpdateTypeId),
    SprintId NUMBER REFERENCES $U.SPRINT(SprintId),
    TelegramUserId NUMBER REFERENCES $U.TELEGRAMUSER(TelegramUserId)
);

CREATE TABLE $U.MESSAGE (
    MessageId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Content NVARCHAR2(100),
    TelegramUserId NUMBER REFERENCES $U.TELEGRAMUSER (TelegramUserId),
    ConversationId NUMBER REFERENCES $U.CONVERSATION(ConversationId)
);

CREATE TABLE $U.BOTMENU (
    BotMenuId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Name NVARCHAR2(100),
    Description NVARCHAR2(200),
    UserTypeId NUMBER REFERENCES $U.USERTYPE(UserTypeId)
);

CREATE TABLE $U.BOTOPTION (
    BotOptionId NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    Text NVARCHAR2(100),
    Description NVARCHAR2(200),
    BotMenuId NUMBER REFERENCES $U.BOTMENU(BotMenuId)
);

#######################################################| Config |#######################################################

INSERT INTO $U.UPDATETYPE (Name, Description) VALUES ('Status change', 'A task is updated with a status change.');
INSERT INTO $U.UPDATETYPE (Name, Description) VALUES ('Deletion', 'A task or sprint is deleted.');
INSERT INTO $U.UPDATETYPE (Name, Description) VALUES ('Name change', 'A task or status has its name updated.');
INSERT INTO $U.UPDATETYPE (Name, Description) VALUES ('Priority change', 'A task is updated to reflect a change in priority.');
INSERT INTO $U.UPDATETYPE (Name, Description) VALUES ('Estimated hours change', 'A task is updated to reflect a change in the estimated hours it will take.');
INSERT INTO $U.UPDATETYPE (Name, Description) VALUES ('Description change', 'A task or sprint is updated to reflect a change in its description.');
INSERT INTO $U.UPDATETYPE (Name, Description) VALUES ('Start date change', 'A sprint is updated to reflect a change in its start date.');
INSERT INTO $U.UPDATETYPE (Name, Description) VALUES ('End date change', 'A sprint is updated to reflect a change in its end date.');

INSERT INTO $U.USERTYPE (Name, Description) VALUES('Manager', 'User in charge of a team, can view tasks from team members, create, edit, and delete sprints and projects.');
INSERT INTO $U.USERTYPE (Name, Description) VALUES ('Developer', 'User who is part of a team, can view, edit, create, and delete their own tasks.');

INSERT INTO $U.TEAMTYPE (Name, Description) VALUES ('Development', 'Teams in charge of writing code.');
INSERT INTO $U.TEAMTYPE (Name, Description) VALUES ('Deployment', 'Teams in charge of deploying finished code to final environments.');
INSERT INTO $U.TEAMTYPE (Name, Description) VALUES ('Testing', 'Teams in charge of testing code created by development teams.');

INSERT INTO $U.TASKSTATUS (Name, Description) VALUES ('To Do', 'Tasks that have not been started but have been created.');
INSERT INTO $U.TASKSTATUS (Name, Description) VALUES ('In Progress', 'Tasks that users have begun to work on.');
INSERT INTO $U.TASKSTATUS (Name, Description) VALUES ('Committed', 'Tasks that are completed and awaiting integration into the main branch.');
INSERT INTO $U.TASKSTATUS (Name, Description) VALUES ('Done', 'Tasks that are integrated into the final project and have been tested.');

#######################################################| Profile Manager |#######################################################

INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Main Menu Manager', 'Starting screen for the manager role.', 1);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('View Projects', 'Contains all the current projects assigned to a manager.', 1);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('View Projects Sprints', 'Contains all the current sprints assigned to a project.', 1);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('View Project Users', 'Contains all the users working on a project.', 1);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('View Sprint Users', 'Contains all the users working on a sprint.', 1);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('View User Tasks', 'Contains all the tasks assigned to users.', 1);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Create Sprint', 'Create a new sprint for the assigned team.', 1);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Create Project', 'Create a new project for the assigned team.', 1);

NUMBER ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'Main Menu Manager';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('View Projects', 'Sends to view projects.', ID);
INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Create Project', 'Sends to create project.', ID);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'View Projects';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('View Project Users', 'Sends to view project users.', ID);
INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('View Project Sprints', 'Sends to view project sprints.', ID);
INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Create Sprint', 'Sends to create sprint.', ID);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'Create Project';

Insert INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Create Project', 'Creates a project when finished sends to Main Menu.', ID);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'View Project Users';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('View User Tasks', 'Sends to view user tasks.', ID);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'View Project Sprints';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('View Sprint Users', 'Sends to view sprint users.', ID);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'View Sprint Users';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('View User Tasks', 'Sends to view user tasks.', ID);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'View User Tasks';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('View Task Info', 'Sends to view user task info.', ID);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'Create Sprint';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Create Sprint', 'Creates a sprint when finished sends to View Projects.', ID);

#######################################################| Profile Developer |#######################################################

INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Main Menu Developer', 'Starting screen for the developer role.', 2);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Create Task', 'Create a new task for the user.', 2);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('View Tasks', 'View all tasks assigned to a user.', 2);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Edit Task', 'Allow the user to modify the selected task.', 2);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Delete Task', 'Allow the user to delete the selected task.', 2);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Edit Task Status', 'Allow the user to change the status of a task (to-do, in progress, committed, completed).', 2);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Edit Task Priority', 'Allow the user to change the priority of a task (3, 2, 1).', 2);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Edit Task Description', 'Allow the user to change the description of a task.', 2);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Edit Task Name', 'Allow the user to change the name of a task.', 2);
INSERT INTO $U.BOTMENU (Name, Description, UserTypeId) VALUES ('Edit Task Estimated Hours', 'Allow the user to change the estimated hours of a task.', 2);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'Main Menu Developer';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Create Task', 'Sends to create task.', ID);
INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('View Tasks', 'Sends to view tasks.', ID);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'Create Task';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Create Task', 'Creates a task when finished sends to Main Menu.', ID);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'View Tasks';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Edit Task', 'Sends to edit task.', ID);
INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Delete Task', 'Sends to delete task.', ID);

ID = SELECT BOTMENUID FROM $U.BOTMENU WHERE NAME = 'Edit Task';

INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Edit Task Status', 'Sends to edit task status.', ID);
INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Edit Task Priority', 'Sends to edit task priority.', ID);
INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Edit Task Description', 'Sends to edit task description.', ID);
INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Edit Task Name', 'Sends to edit task name.', ID);
INSERT INTO $U.BOTOPTION (Text, Description, BotMenuId) VALUES ('Edit Task Estimated Hours', 'Sends to edit task estimated hours.', ID);


#######################################################| Users |#######################################################

ID = SLECT USERID FROM $U.USERTYPE WHERE NAME = 'Developer';

INSERT INTO $U.TELEGRAMUSER (Name, Email, PhoneNumber, TelegramName, UserTypeId) VALUES ('Yahir Rivera', 'A00572029@tec.mx', '0000000000', 'Yahir Rivera', ID);
INSERT INTO $U.TELEGRAMUSER (Name, Email, PhoneNumber, TelegramName, UserTypeId) VALUES ('Guillermo Esquivel', 'A01625621@tec.mx', '0000000000', 'esquivel_guillermo', ID);
INSERT INTO $U.TELEGRAMUSER (Name, Email, PhoneNumber, TelegramName, UserTypeId) VALUES ('Manuel Ramos', 'A00227837@tec.mx', '0000000000', 'Saiko_93', ID);
INSERT INTO $U.TELEGRAMUSER (Name, Email, PhoneNumber, TelegramName, UserTypeId) VALUES ('Alfonso Ramirez', 'A01641937', '0000000000', 'Ramirez_Alfonso', ID);
INSERT INTO $U.TELEGRAMUSER (Name, Email, PhoneNumber, TelegramName, UserTypeId) VALUES ('Admin Dev', 'adminDev', '0000000000', 'adminDev', ID);

ID = SLECT USERID FROM $U.USERTYPE WHERE NAME = 'Manager';

INSERT INTO $U.TELEGRAMUSER (Name, Email, PhoneNumber, TelegramName, UserTypeId) VALUES ('Samuel Garcia', 'sam.garcia.96@gmail.com', '0000000000', 'Samuel Garcia', ID);
INSERT INTO $U.TELEGRAMUSER (Name, Email, PhoneNumber, TelegramName, UserTypeId) VALUES ('Admin Man', 'adminMan', '0000000000', 'adminMan', ID);


#######################################################| Teams |#######################################################

ID = SELECT TEAMTYPEID FROM $U.TEAMTYPE WHERE NAME = 'Development';
INSERT INTO $U.TEAM (Name, Description, TeamTypeId) VALUES ('Development Team', 'Team in charge of planning, structuring, and developing backend features.', ID);

ID = SELECT TEAMTYPEID FROM $U.TEAMTYPE WHERE NAME = 'Deployment';
INSERT INTO $U.TEAM (Name, Description, TeamTypeId) VALUES ('Deployment Team', 'Team in charge of deploying on OCI all the infrastructure planned by the Development Team.', ID);

ID = SELECT TEAMTYPEID FROM $U.TEAMTYPE WHERE NAME = 'Testing';
INSERT INTO $U.TEAM (Name, Description, TeamTypeId) VALUES ('Testing Team', 'Team in charge of ensuring the correct working of the service.', ID);

#######################################################| Assignation  of Users to a Team |#######################################################

# Deployment
NUMBER USERID = SELECT TELEGRAMUSERID FROM $U.TELEGRAMUSER WHERE TELEGRAMNAME = 'Yahir Rivera';
NUMBER TEAMID = SELECT TEAMID FROM $U.TEAM WHERE NAME = 'Deployment Team';
INSERT INTO $U.USERTEAM (TelegramUserId, TeamId) VALUES (USERID, TEAMID);

USERID = SELECT TELEGRAMUSERID FROM $U.TELEGRAMUSER WHERE TELEGRAMNAME = 'Manuel Ramos';
INSERT INTO $U.USERTEAM (TelegramUserId, TeamId) VALUES (USERID, TEAMID);

USERID = SELECT TELEGRAMUSERID FROM $U.TELEGRAMUSER WHERE TELEGRAMNAME = 'Samuel Garcia';
INSERT INTO $U.USERTEAM (TelegramUserId, TeamId) VALUES (USERID, TEAMID);

#Development
USERID = SELECT TELEGRAMUSERID FROM $U.TELEGRAMUSER WHERE TELEGRAMNAME = 'Guillermo Esquivel';
TEAMID = SELECT TEAMID FROM $U.TEAM WHERE NAME = 'Development Team';
INSERT INTO $U.USERTEAM (TelegramUserId, TeamId) VALUES (USERID, TEAMID);

USERID = SELECT TELEGRAMUSERID FROM $U.TELEGRAMUSER WHERE TELEGRAMNAME = 'Samuel Garcia';
INSERT INTO $U.USERTEAM (TelegramUserId, TeamId) VALUES (USERID, TEAMID);

#Testing
USERID = SELECT TELEGRAMUSERID FROM $U.TELEGRAMUSER WHERE TELEGRAMNAME = 'Alfonso Ramirez';
TEAMID = SELECT TEAMID FROM $U.TEAM WHERE NAME = 'Testing Team';
INSERT INTO $U.USERTEAM (TelegramUserId, TeamId) VALUES (USERID, TEAMID);

USERID = SELECT TELEGRAMUSERID FROM $U.TELEGRAMUSER WHERE TELEGRAMNAME = 'Samuel Garcia';
INSERT INTO $U.USERTEAM (TelegramUserId, TeamId) VALUES (USERID, TEAMID);

commit;

exit
!
  state_set_done TODO_USER
  echo "finished connecting to database and creating attributes"
done
# DB Setup Done
state_set_done DB_SETUP


# CREATE TABLE TODOUSER.todoitem (id NUMBER GENERATED ALWAYS AS IDENTITY, description VARCHAR2(4000), creation_ts TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, done NUMBER(1,0) , PRIMARY KEY (id));
# insert into TODOUSER.todoitem  (description, done) values ('Manual item insert', 0);

#drop TABLE PROJECT;
#drop TABLE CONVERSATION;
#drop TABLE TASKSTATUS;
#DROP TABLE TEAMTYPE;
#DROP TABLE UPDATETYPE;
#DROP TABLE TEAM;
#DROP TABLE TEAMTYPE;
#DROP TABLE BOTMENU;
#DROP TABLE BOTOPTION;
#DROP TABLE USERTEAM;
#DROP TABLE USERTYPE;
#drop table TELEGRAMUSER;
#DROP TABLE MESSAGE;
#DROP TABLE SPRINT;
#DROP TABLE TASK;
#DROP TABLE SPRINTUPDATE;