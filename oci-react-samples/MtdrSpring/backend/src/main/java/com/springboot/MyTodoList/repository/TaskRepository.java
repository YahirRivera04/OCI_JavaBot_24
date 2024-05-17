package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement

public interface TaskRepository extends JpaRepository<Task,Integer> {

 @Query(value = "SELECT name FROM TODOUSER.TASK WHERE telegramUserId = ?1", nativeQuery = true)
    List <String> findByTelegramUserId(int telegramUserId);
}
