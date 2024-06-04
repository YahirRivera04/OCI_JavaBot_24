package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.model.TaskUpdate;
import com.springboot.MyTodoList.service.TaskUpdateService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import com.springboot.MyTodoList.controller.TaskUpdateController;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TaskUpdateControllerTest {

    @InjectMocks
    TaskUpdateController taskUpdateController;

    @Mock
    TaskUpdateService taskUpdateService;

    @Test
    public void testCreateTaskUpdate() {
        TaskUpdate taskUpdate = new TaskUpdate();
        taskUpdate.setID(1L);
        taskUpdate.setTimeStamp(new Timestamp(System.currentTimeMillis()));

        when(taskUpdateService.createNewTaskUpdate(any(TaskUpdate.class))).thenReturn("Task Update log with timestamp " + taskUpdate.getTimeStamp() + " created successfully");

        ResponseEntity<String> response = taskUpdateController.createTaskUpdate(taskUpdate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Task Update log with timestamp " + taskUpdate.getTimeStamp() + " created successfully", response.getBody());

        verify(taskUpdateService, times(1)).createNewTaskUpdate(taskUpdate);
    }
}