package com.example.jmsdb.module.controller;

import com.example.jmsdb.module.Todo;
import com.example.jmsdb.module.exception.ResourceNotFoundException;
import com.example.jmsdb.module.repository.TodoRepository;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api")
public class TodoController {

    @Inject
    TodoRepository todorepository;


    @PostMapping("/todolist")
    public Todo createTask(@RequestBody Todo note) {
        return todorepository.save(note);
    }

    @PutMapping("/todolist/{id}")
    public Todo updateStatus(@PathVariable(value = "id") Long taskId, @RequestBody Todo TaskDetails){
        Todo task = todorepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", taskId));

        String st = TaskDetails.getStatus();
        task.setStatus(TaskDetails.getStatus());
        if(st.equals("A")|| st.equals("D")){
           Todo updatedNote = todorepository.save(task);
            return updatedNote;
        }
      return null;
    }

    @GetMapping("/todolist")
    public List<Todo> getTask(String status, String date) throws ParseException {
        if(status == null && date == null){
            return todorepository.findAll();
        }
        else if(date == null){
            List<Todo> todotask = todorepository.getTaskByStatus(status);
            return (List<Todo>) todotask;
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = sdf.parse(date);
            List<Todo> todotask = (List<Todo>) todorepository.getTaskByStatusByDate(status, parsedDate);
            return (List<Todo>) todotask;
        }
    }

//    @GetMapping("/todolist")
//    public List<Todo> getAllTask(){
//        return todorepository.findAll();
//    }

}
