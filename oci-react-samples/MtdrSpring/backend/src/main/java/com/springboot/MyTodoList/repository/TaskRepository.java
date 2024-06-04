package com.springboot.MyTodoList.repository;
import com.springboot.MyTodoList.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    @Query(value = "SELECT * FROM TODOUSER.TASK WHERE TelegramUserId = ?1", nativeQuery = true)
    List<Task> findAllTasksByTelegramUserId(Long telegramUserId);

    @Modifying
    @Query(value = "DELETE FROM TODOUSER.TASK WHERE TelegramUserId = ?1 AND Name = ?2", nativeQuery = true)
    void deleteTasksByTelegramUserIdAndTaskName(Long telegramUserId, String name);

}
