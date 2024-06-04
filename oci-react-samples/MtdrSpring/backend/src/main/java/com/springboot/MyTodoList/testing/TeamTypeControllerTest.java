package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.controller.TeamTypeController;
import com.springboot.MyTodoList.model.TeamType;
import com.springboot.MyTodoList.service.TeamTypeService;
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
public class TeamTypeControllerTest {

    @InjectMocks
    TeamTypeController teamTypeController;

    @Mock
    TeamTypeService teamTypeService;

    @Test
    public void testFindAllTeamType() {
        TeamType teamType1 = new TeamType();
        teamType1.setID(1L);
        teamType1.setName("TeamType 1");

        TeamType teamType2 = new TeamType();
        teamType2.setID(2L);
        teamType2.setName("TeamType 2");

        List<TeamType> teamTypeList = Arrays.asList(teamType1, teamType2);

        when(teamTypeService.findAllTeamTypes()).thenReturn(teamTypeList);

        ResponseEntity<List<TeamType>> response = teamTypeController.findAllTeamType();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("TeamType 1", response.getBody().get(0).getName());
        assertEquals("TeamType 2", response.getBody().get(1).getName());
    }
}

