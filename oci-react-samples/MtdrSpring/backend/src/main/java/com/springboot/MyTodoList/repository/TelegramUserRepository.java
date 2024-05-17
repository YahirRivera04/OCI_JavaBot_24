package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    
    // Verify if user exists by telegram name
    Boolean existsByTelegramName(String telegramName);

    // get telegram id by telegram user name
    @Query(value = "SELECT telegramUserId FROM TODOUSER.TELEGRAMUSER WHERE telegramName = ?1", nativeQuery = true)
    Integer findTelegramUserIdByTelegramName(String telegramName);

    // get chat id by telegram user id
    @Query(value = "SELECT chatId FROM TODOUSER.TELEGRAMUSER WHERE telegramUserId = ?1", nativeQuery = true)
    Long fndChatIdByTelegramUserId(Long id);
}
