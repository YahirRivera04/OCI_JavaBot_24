
package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.springboot.MyTodoList.service.TaskService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    public void testFindTasksByTelegramUserId() throws Exception {
        Long telegramUserId = 1L;
        Task task1 = new Task(1L, "Task 1", "Description 1", 1.0f, 1, null, null, null);
        Task task2 = new Task(2L, "Task 2", "Description 2", 2.0f, 2, null, null, null);
        List<Task> taskList = Arrays.asList(task1, task2);
        String expectedResponse = task1.toString() + "\n" + task2.toString() + "\n";

        when(taskService.findAllTaskByTelegramUserId(telegramUserId)).thenReturn(taskList);

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/{TelegramUserId}", telegramUserId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }
}