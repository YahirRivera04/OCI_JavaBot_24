package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    // Verify if user exists by telegram name
    Boolean existsByTelegramName(String telegramName);

    // Get telegram id by telegram user name
    @Query(value = "SELECT TelegramUserId FROM TODOUSER.TELEGRAMUSER WHERE TelegramName = ?1", nativeQuery = true)
    Long findTelegramUserIdByTelegramName(String telegramName);

    // Get chat id by telegram user id
    @Query(value = "SELECT ChatId FROM TODOUSER.TELEGRAMUSER WHERE TelegramUserId = ?1", nativeQuery = true)
    Long fndChatIdByTelegramUserId(Long telegramUserId);


    // Set chat id by telegram user id
    @Query(value = "UPDATE TODOUSER.TELEGRAMUSER SET ChatId = ?2 WHERE TelegramUserId = ?1", nativeQuery = true)
    Boolean setChatIdByTelegramUserId(Long telegramUserId, Long chatId);

}
