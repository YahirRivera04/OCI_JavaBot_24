package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.controller.UserTypeController;
import com.springboot.MyTodoList.model.UserType;
import com.springboot.MyTodoList.service.UserTypeService;

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
public class UserTypeControllerTest {

    @InjectMocks
    UserTypeController userTypeController;

    @Mock
    UserTypeService userTypeService;

    @Test
    public void testFindUserType() {
        UserType userType1 = new UserType();
        userType1.setID(1L);
        userType1.setName("Admin");
        userType1.setDescription("Admin user type");

        UserType userType2 = new UserType();
        userType2.setID(2L);
        userType2.setName("User");
        userType2.setDescription("Regular user type");

        List<UserType> userTypeList = Arrays.asList(userType1, userType2);

        when(userTypeService.findAllUserType()).thenReturn(userTypeList);

        ResponseEntity<String> response = userTypeController.findUserType();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(userType1.toString() + "\n" + userType2.toString() + "\n", response.getBody());
    }
}