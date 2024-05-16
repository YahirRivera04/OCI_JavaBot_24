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

    @Query(value = "SELECT* FROM TODOUSER.TELEGRAMUSER WHERE TelegramName = ?1", nativeQuery = true)
    List<TelegramUser> findByTelegramName(String account);

    TelegramUser findDistinctByTelegramName(String telegramName);
}
