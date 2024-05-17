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
    
    Boolean existsByTelegramName(String telegramName);
    
    //TelegramUser findByTelegramNameIs(String telegramName);
    // Get telegram id by telegram user name
    Integer findTelegramUserIdByTelegramName(String telegramName);
}
