package com.example.jmsdb.module.repository;

import com.example.jmsdb.module.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("select h from Todo h where h.status=?1 and createdat<=?2")
    List<Todo> getTaskByStatusByDate(String status, Date date);

    @Query("select h from Todo h where h.status=?1")
    List<Todo> getTaskByStatus(String status);
}
