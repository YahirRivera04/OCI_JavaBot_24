package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.controller.UserTeamController;
import com.springboot.MyTodoList.model.UserTeam;
import com.springboot.MyTodoList.service.UserTeamService;
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
public class UserTeamControllerTest {

    @InjectMocks
    UserTeamController userTeamController;

    @Mock
    UserTeamService userTeamService;

    @Test
    public void testFindUserTeams() {
        UserTeam userTeam1 = new UserTeam();
        userTeam1.setID(1L);
        // Set other properties of userTeam1 as needed

        UserTeam userTeam2 = new UserTeam();
        userTeam2.setID(2L);
        // Set other properties of userTeam2 as needed

        List<UserTeam> userTeamList = Arrays.asList(userTeam1, userTeam2);

        when(userTeamService.findAllUserTeams()).thenReturn(userTeamList);

        ResponseEntity<String> response = userTeamController.findUserTeams();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userTeam1.toString() + "\n" + userTeam2.toString() + "\n", response.getBody());
    }
}