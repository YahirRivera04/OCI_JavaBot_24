package com.springboot.MyTodoList.repository;
import com.springboot.MyTodoList.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import java.sql.Timestamp;
import javax.transaction.Transactional;

@Repository
@Transactional
@EnableTransactionManagement

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query(value = "UPDATE TODOUSER.TELEGRAMUSER SET EndTime = ?2 WHERE ConversationId = ?1", nativeQuery = true)
    void updateConversation(Long conversationId, Timestamp endTime);
}
