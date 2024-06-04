package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.controller.TaskStatusController;
import com.springboot.MyTodoList.model.TaskStatus;
import com.springboot.MyTodoList.service.TaskStatusService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskStatusService taskStatusService;

    @Test
    public void testFindTaskStatus() throws Exception {
        TaskStatus taskStatus = new TaskStatus(1L, "Test TaskStatus", "Description");
        when(taskStatusService.findAllTaskStatus()).thenReturn(Arrays.asList(taskStatus));

        mockMvc.perform(MockMvcRequestBuilders.get("/taskstatus/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(taskStatus.toString())));
    }

    @Test
    public void testFindAllTaskStatus() throws Exception {
        TaskStatus taskStatus = new TaskStatus(1L, "Test TaskStatus", "Description");
        when(taskStatusService.findAllTaskStatus()).thenReturn(Arrays.asList(taskStatus));

        mockMvc.perform(MockMvcRequestBuilders.get("/taskstatus/"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"name\":\"Test TaskStatus\",\"description\":\"Description\"}]"));
    }
}