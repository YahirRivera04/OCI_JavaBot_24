package com.springboot.MyTodoList.testing;

import com.springboot.MyTodoList.controller.TelegramUserController;
import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.service.TelegramUserService;
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
public class TelegramUserControllerTest {

    @InjectMocks
    TelegramUserController telegramUserController;

    @Mock
    TelegramUserService telegramUserService;

    @Test
    public void testFindTelegramUsers() {
        TelegramUser telegramUser1 = new TelegramUser();
        telegramUser1.setID(1L);
        telegramUser1.setName("TelegramUser 1");

        TelegramUser telegramUser2 = new TelegramUser();
        telegramUser2.setID(2L);
        telegramUser2.setName("TelegramUser 2");

        List<TelegramUser> telegramUserList = Arrays.asList(telegramUser1, telegramUser2);

        when(telegramUserService.findAllTelegramUsers()).thenReturn(telegramUserList);

        ResponseEntity<String> response = telegramUserController.findTelegramUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("TelegramUser[ID=1, Name=TelegramUser 1, Email=null, PhoneNumber=null, TelegramName=null, UserType=null, ChatId=null]\nTelegramUser[ID=2, Name=TelegramUser 2, Email=null, PhoneNumber=null, TelegramName=null, UserType=null, ChatId=null]\n", response.getBody());
    }

    @Test
    public void testUpdateTelegramUserController() {
        Long telegramUserId = 1L;
        Long chatId = 12345L;
        String expectedResponse = "ChatId updated successfully ";

        when(telegramUserService.updateChatId(telegramUserId, chatId)).thenReturn(expectedResponse);

        ResponseEntity<String> response = telegramUserController.updateTelegramUserController(telegramUserId, chatId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
    }
}