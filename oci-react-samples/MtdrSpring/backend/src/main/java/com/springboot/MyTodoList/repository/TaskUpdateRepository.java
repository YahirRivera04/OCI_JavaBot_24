package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.TaskUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement

public interface TaskUpdateRepository extends JpaRepository<TaskUpdate, Long> {

    @Modifying
    @Query(value = "DELETE FROM TODOUSER.TASKUPDATE WHERE TaskId = ?1", nativeQuery = true)
    void deleteTasksUpdateByTaskId(Long taskId);
};
