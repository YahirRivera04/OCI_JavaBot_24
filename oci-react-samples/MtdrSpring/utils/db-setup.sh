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
    TaskStatusId NUMBER REFERENCES $U.TASKSTATUS(TaskStatusId),
    UserTeamId NUMBER REFERENCES $U.USERTEAM(UserTeamId)
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

INSERT INTO $U.UPDATETYPE (Name, Description) VALUES
('Status change', 'A task is updated with a status change.'),
('Deletion', 'A task or sprint is deleted.'),
('Name change', 'A task or status has its name updated.'),
('Priority change', 'A task is updated to reflect a change in priority.'),
('Estimated hours change', 'A task is updated to reflect a change in the estimated hours it will take.'),
('Description change', 'A task or sprint is updated to reflect a change in its description.'),
('Start date change', 'A sprint is updated to reflect a change in its start date.'),
('End date change', 'A sprint is updated to reflect a change in its end date.');

INSERT INTO $U.USERTYPE (Name, Description) VALUES
('Manager', 'User in charge of a team, can view tasks from team members, create, edit, and delete sprints and projects.'),
('Developer', 'User who is part of a team, can view, edit, create, and delete their own tasks.');

INSERT INTO $U.TEAMTYPE (Name, Description) VALUES
('Development', 'Teams in charge of writing code.'),
('Deployment', 'Teams in charge of deploying finished code to final environments.'),
('Testing', 'Teams in charge of testing code created by development teams.');

INSERT INTO $U.TASKSTATUS (Name, Description) VALUES
('To Do', 'Tasks that have not been started but have been created.'),
('In Progress', 'Tasks that users have begun to work on.'),
('Committed', 'Tasks that are completed and awaiting integration into the main branch.'),
('Done', 'Tasks that are integrated into the final project and have been tested.');

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

#CREATE TABLE TASKSTATUS (TaskStatusId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200));
#CREATE TABLE TASK (TaskId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200), EstimatedHours FLOAT, Priority NUMBER, UserId NUMBER REFERENCES USER(UserId), SprintId NUMBER REFERENCES SPRINT(SprintId), TaskStatusId NUMBER REFERENCES TASKSTATUS(TaskStatusId), UserTypeId NUMBER REFERENCES USERTYPE(UserTypeId), UserTeamId NUMBER REFERENCES USERTEAM(UserTeamId));
#CREATE TABLE SPRINT (SprintId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(500), StartDate DATE, EndDate DATE, ProjectId NUMBER REFERENCES PROJECT(ProjectId));
#CREATE TABLE PROJECT (ProjectId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(500));
#CREATE TABLE USER (UserId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Email NVARCHAR2(100), PhoneNumber NVARCHAR2(15), TelegramName NVARCHAR2(100), UserTypeId NUMBER REFERENCES USERTYPE(UserTypeId), UserTeamId NUMBER REFERENCES USERTEAM(UserTeamId));
#CREATE TABLE USERTYPE (UserTypeId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200));
#CREATE TABLE USERTEAM (UserTeamId NUMBER PRIMARY KEY, UserId NUMBER REFERENCES USER(UserId), TeamId NUMBER REFERENCES TEAM(TeamId));
#CREATE TABLE TEAM (TeamId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200), TeamTypeId NUMBER REFERENCES TEAMTYPE(TeamTypeId));
#CREATE TABLE TEAMTYPE (TeamTypeId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200));
#CREATE TABLE TASKUPDATE (TaskUpdateId NUMBER PRIMARY KEY, TimeStamp TIMESTAMP, UpdateTypeId NUMBER REFERENCES UPDATETYPE(UpdateTypeId), TaskId NUMBER REFERENCES TASK(TaskId), UserId NUMBER REFERENCES USER(UserId));
#CREATE TABLE UPDATETYPE (UpdateTypeId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200));
#CREATE TABLE SPRINTUPDATE (SprintUpdateId NUMBER PRIMARY KEY, TimeStamp TIMESTAMP, UpdateTypeId NUMBER REFERENCES UPDATETYPE(UpdateTypeId), SprintId NUMBER REFERENCES SPRINT(SprintId), UserId NUMBER REFERENCES USER(UserId));
#CREATE TABLE CONVERSATION (ConversationId NUMBER PRIMARY KEY, StartTime TIMESTAMP, EndTime TIMESTAMP);
#CREATE TABLE MESSAGE (MessageId NUMBER PRIMARY KEY, Content NVARCHAR2(100), UserId NUMBER REFERENCES USER(UserId), ConversationId NUMBER REFERENCES CONVERSATION(ConversationId));
#CREATE TABLE BOTMENU (BotMenuId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200), UserTypeId NUMBER REFERENCES USERTYPE(UserTypeId));
#CREATE TABLE BOTOPTION (BotOptionId NUMBER PRIMARY KEY, Text NVARCHAR2(100), Description NVARCHAR2(200), BotMenuId NUMBER REFERENCES BOTMENU(BotMenuId));

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