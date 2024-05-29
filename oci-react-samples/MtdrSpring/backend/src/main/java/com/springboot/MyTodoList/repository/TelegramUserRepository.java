package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.TelegramUser;
import com.springboot.MyTodoList.model.UserType;

import org.hibernate.annotations.ParamDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    // Verify if user exists by telegram name
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM TELEGRAMUSER WHERE ChatId = ?1", nativeQuery = true)
    Boolean existsByChatId(Long chatId);
    //Boolean existsByChatId(Long chatId);

    // Get telegram user id by telegram user name
    @Query(value = "SELECT TelegramUserId FROM TODOUSER.TELEGRAMUSER WHERE TelegramName = ?1", nativeQuery = true)
    Long findTelegramUserIdByTelegramName(String telegramName);

    // Set chat id by telegram user id
    @Modifying
    @Query(value = "UPDATE TODOUSER.TELEGRAMUSER SET ChatId = ?2 WHERE TelegramUserId = ?1", nativeQuery = true)
    void setChatIdByTelegramUserId(Long telegramUserId, Long chatId);

    // Get telegram user id by chat id
    @Query(value = "SELECT TelegramUserId FROM TODOUSER.TELEGRAMUSER WHERE ChatId = ?1", nativeQuery = true)
    Long findUserIdByChatId(Long chatId);

    // Get User type id by telegram user id
    @Query(value = "SELECT UserTypeId FROM TODOUSER.TELEGRAMUSER WHERE TelegramUserId = ?1", nativeQuery = true)
    Long findUserTypeId(Long telegramUserId);

    @Query(value = "SELECT TelegramName FROM TODOUSER.TELEGRAMUSER WERE TelegramUserId = ?1", nativeQuery = true)
    String findTelegramNameByTelegramUserId(Long telegramUserId);


}
