package com.springboot.MyTodoList.repository;
import com.springboot.MyTodoList.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement

public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {

    // Esists By Chat Id
    Boolean existsByChatId (Long chatId);

    // Get TelegramUser By Chat Id
    TelegramUser findByTelegramName (String telegramName);


    // Set Chat Id by Telegram User Id
    @Modifying
    @Query(value = "UPDATE TODOUSER.TELEGRAMUSER SET ChatId = ?2 WHERE TelegramUserId = ?1", nativeQuery = true)
    void setChatIdByTelegramUserId(Long telegramUserId, Long chatId);

}
