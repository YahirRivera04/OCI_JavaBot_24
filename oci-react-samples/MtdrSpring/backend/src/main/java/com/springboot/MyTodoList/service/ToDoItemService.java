package com.springboot.MyTodoList.service;

import com.springboot.MyTodoList.model.ToDoItem;
import com.springboot.MyTodoList.repository.ToDoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToDoItemService {

    @Autowired
    private ToDoItemRepository toDoItemRepository;
    
    public List<ToDoItem> findAll(){
        List<ToDoItem> todoItems = toDoItemRepository.findAll();
        return todoItems;
    }
    public ResponseEntity<ToDoItem> getItemById(int id){
        Optional<ToDoItem> todoData = toDoItemRepository.findById(id);
        if (todoData.isPresent()){
            return new ResponseEntity<>(todoData.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    public ToDoItem addToDoItem(ToDoItem toDoItem){
        return toDoItemRepository.save(toDoItem);
    }

    public boolean deleteToDoItem(int id){
        try{
            toDoItemRepository.deleteById(id);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public ToDoItem updateToDoItem(int id, ToDoItem td){
        Optional<ToDoItem> toDoItemData = toDoItemRepository.findById(id);
        if(toDoItemData.isPresent()){
            ToDoItem toDoItem = toDoItemData.get();
            toDoItem.setID(id);
            toDoItem.setCreation_ts(td.getCreation_ts());
            toDoItem.setDescription(td.getDescription());
            toDoItem.setDone(td.isDone());
            return toDoItemRepository.save(toDoItem);
        }else{
            return null;
        }
    }

}

/*WHENEVER SQLERROR EXIT 1
connect admin/"$DB_PASSWORD"@$SVC
CREATE USER $U IDENTIFIED BY "$DB_PASSWORD" DEFAULT TABLESPACE data QUOTA UNLIMITED ON data;
GRANT CREATE SESSION, CREATE VIEW, CREATE SEQUENCE, CREATE PROCEDURE TO $U;
GRANT CREATE TABLE, CREATE TRIGGER, CREATE TYPE, CREATE MATERIALIZED VIEW TO $U;
GRANT CONNECT, RESOURCE, pdb_dba, SODA_APP to $U;
CREATE TABLE TODOUSER.TASKSTATUS (TaskStatusId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200));
CREATE TABLE TODOUSER.TASK (TaskId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200), EstimatedHours FLOAT, Priority NUMBER, UserId NUMBER REFERENCES USER(UserId), SprintId NUMBER REFERENCES SPRINT(SprintId), TaskStatusId NUMBER REFERENCES TASKSTATUS(TaskStatusId), UserTypeId NUMBER REFERENCES USERTYPE(UserTypeId), UserTeamId NUMBER REFERENCES USERTEAM(UserTeamId));
CREATE TABLE TODOUSER.SPRINT (SprintId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(500), StartDate DATE, EndDate DATE, ProjectId NUMBER REFERENCES PROJECT(ProjectId));
CREATE TABLE TODOUSER.PROJECT (ProjectId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(500));
CREATE TABLE TODOUSER.USER (UserId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Email NVARCHAR2(100), PhoneNumber NVARCHAR2(15), TelegramName NVARCHAR2(100), UserTypeId NUMBER REFERENCES USERTYPE(UserTypeId), UserTeamId NUMBER REFERENCES USERTEAM(UserTeamId));
CREATE TABLE TODOUSER.USERTYPE (UserTypeId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200));
CREATE TABLE TODOUSER.USERTEAM (UserTeamId NUMBER PRIMARY KEY, UserId NUMBER REFERENCES USER(UserId), TeamId NUMBER REFERENCES TEAM(TeamId));
CREATE TABLE TODOUSER.TEAM (TeamId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200), TeamTypeId NUMBER REFERENCES TEAMTYPE(TeamTypeId));
CREATE TABLE TODOUSER.TEAMTYPE (TeamTypeId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200));
CREATE TABLE TODOUSER.TASKUPDATE (TaskUpdateId NUMBER PRIMARY KEY, TimeStamp TIMESTAMP, UpdateTypeId NUMBER REFERENCES UPDATETYPE(UpdateTypeId), TaskId NUMBER REFERENCES TASK(TaskId), UserId NUMBER REFERENCES USER(UserId));
CREATE TABLE TODOUSER.UPDATETYPE (UpdateTypeId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200));
CREATE TABLE TODOUSER.SPRINTUPDATE (SprintUpdateId NUMBER PRIMARY KEY, TimeStamp TIMESTAMP, UpdateTypeId NUMBER REFERENCES UPDATETYPE(UpdateTypeId), SprintId NUMBER REFERENCES SPRINT(SprintId), UserId NUMBER REFERENCES USER(UserId));
CREATE TABLE TODOUSER.CONVERSATION (ConversationId NUMBER PRIMARY KEY, StartTime TIMESTAMP, EndTime TIMESTAMP);
CREATE TABLE TODOUSER.MESSAGE (MessageId NUMBER PRIMARY KEY, Content NVARCHAR2(100), UserId NUMBER REFERENCES USER(UserId), ConversationId NUMBER REFERENCES CONVERSATION(ConversationId));
CREATE TABLE TODOUSER.BOTMENU (BotMenuId NUMBER PRIMARY KEY, Name NVARCHAR2(100), Description NVARCHAR2(200), UserTypeId NUMBER REFERENCES USERTYPE(UserTypeId));
CREATE TABLE TODOUSER.BOTOPTION (BotOptionId NUMBER PRIMARY KEY, Text NVARCHAR2(100), Description NVARCHAR2(200), BotMenuId NUMBER REFERENCES BOTMENU(BotMenuId));
commit; */



