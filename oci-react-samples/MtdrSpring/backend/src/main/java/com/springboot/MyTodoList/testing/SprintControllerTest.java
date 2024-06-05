package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.controller.SprintController;
import com.springboot.MyTodoList.model.Sprint;
import com.springboot.MyTodoList.service.SprintService;
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

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class SprintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SprintService sprintService;

    @Test
    public void testGetSprints() throws Exception {
        Sprint sprint = new Sprint();
        sprint.setID(1L);
        sprint.setName("Test Sprint");
        when(sprintService.findAllSprints()).thenReturn(Arrays.asList(sprint));

        mockMvc.perform(MockMvcRequestBuilders.get("/sprints"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateSprint() throws Exception {
        Sprint sprint = new Sprint();
        sprint.setID(1L);
        sprint.setName("Test Sprint");
        when(sprintService.createNewSprint(sprint)).thenReturn("Sprint " + sprint.getName() + " created succesfully.");

        mockMvc.perform(MockMvcRequestBuilders.post("/sprints")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"name\": \"Test Sprint\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}