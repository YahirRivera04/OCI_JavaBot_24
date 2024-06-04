package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.controller.SprintUpdateController;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.model.SprintUpdate;
import com.springboot.MyTodoList.service.SprintUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.springboot.MyTodoList.model.UpdateType;
import com.springboot.MyTodoList.model.TelegramUser;
import java.sql.Timestamp;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class SprintUpdateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SprintUpdateService sprintUpdateService;

    @Test
    public void testUpdateSprint() throws Exception {
        Long id = 1L;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        UpdateType updateType = new UpdateType(); // You need to set this up
        Sprint sprint = new Sprint(); // You need to set this up
        TelegramUser telegramUser = new TelegramUser(); // You need to set this up

        SprintUpdate sprintUpdate = new SprintUpdate(id, timestamp, updateType, sprint, telegramUser);
        String successMessage = "Sprint Update with timestamp " + sprintUpdate.getTimeStamp() + " created succesfully.";
        when(sprintUpdateService.createNewSprintUpdate(sprintUpdate)).thenReturn(successMessage);

        mockMvc.perform(MockMvcRequestBuilders.put("/sprints/{id}", sprintUpdate.getID())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"timeStamp\": \"" + timestamp + "\"}")) // You need to adjust this to match your SprintUpdate structure
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(successMessage));
    }
}