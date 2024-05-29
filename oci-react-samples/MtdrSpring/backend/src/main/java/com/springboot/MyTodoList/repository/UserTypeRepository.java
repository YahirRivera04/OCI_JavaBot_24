package com.springboot.MyTodoList.repository;

import com.springboot.MyTodoList.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement

public interface UserTypeRepository extends JpaRepository<UserType, Long> {

    // Get User Type Info by Name
    @Query(value = "SELECT UserTypeId FROM TODOUSER.USERTYPE WHERE Name = ?1", nativeQuery = true)
    Long findUserTypeIdByName(String Name);

}
