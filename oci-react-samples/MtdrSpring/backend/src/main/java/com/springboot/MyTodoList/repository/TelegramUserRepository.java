package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.TelegramUser;

import org.hibernate.annotations.ParamDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query(value = "SELECT e.TelegramUserId FROM TODOUSER.TELEGRAMUSER WHERE e.TelegramName = ?1", nativeQuery = true)
    Long findTelegramUserIdByTelegramName(String telegramName);

    // Get chat id by telegram user id
    @Query(value = "SELECT e.ChatId FROM TODOUSER.TELEGRAMUSER WHERE e.TelegramUserId = :id", nativeQuery = true)
    Long fndChatIdByTelegramUserId(@Param("id") Long telegramUserId);


    // Set chat id by telegram user id
    @Query(value = "UPDATE TODOUSER.TELEGRAMUSER SET e.ChatId = :chatId WHERE TelegramUserId = :id", nativeQuery = true)
    void setChatIdByTelegramUserId(@Param("id")Long telegramUserId,@Param("chatId") Long chatId);

}
