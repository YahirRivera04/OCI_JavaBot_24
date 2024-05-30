package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;

import java.util.List;

@Repository
@Transactional
@EnableTransactionManagement

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

}
