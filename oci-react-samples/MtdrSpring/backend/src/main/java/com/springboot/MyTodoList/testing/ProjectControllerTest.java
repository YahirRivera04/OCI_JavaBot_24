package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.controller.ProjectController;
import com.springboot.MyTodoList.model.Project;
import com.springboot.MyTodoList.service.ProjectService;
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
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test
    public void testGetProjects() throws Exception {
        Project project = new Project();
        project.setID(1L);;
        project.setName("Test Project");
        when(projectService.findAllProjects()).thenReturn(Arrays.asList(project));

        mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateProject() throws Exception {
        Project project = new Project();
        project.setID(1L);
        project.setName("Test Project");
        when(projectService.createNewProject(project)).thenReturn("Project " + project.getName() + " created succesfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"name\": \"Test Project\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}