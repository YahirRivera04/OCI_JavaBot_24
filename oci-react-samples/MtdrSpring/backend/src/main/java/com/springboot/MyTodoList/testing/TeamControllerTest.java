package com.springboot.MyTodoList.testing;
import com.springboot.MyTodoList.controller.TeamController;
import com.springboot.MyTodoList.model.Team;
import com.springboot.MyTodoList.service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {

    @InjectMocks
    TeamController teamController;

    @Mock
    TeamService teamService;

    @Test
    public void testFindAllTeams() {
        Team team1 = new Team();
        team1.setID(1L);
        team1.setName("Team 1");

        Team team2 = new Team();
        team2.setID(2L);
        team2.setName("Team 2");

        List<Team> teamList = Arrays.asList(team1, team2);

        when(teamService.findAllTeams()).thenReturn(teamList);

        ResponseEntity<List<Team>> response = teamController.findAllTeams();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Team 1", response.getBody().get(0).getName());
        assertEquals("Team 2", response.getBody().get(1).getName());
    }
}