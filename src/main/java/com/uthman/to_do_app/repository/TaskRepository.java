package com.uthman.to_do_app.repository;

import com.uthman.to_do_app.entity.Task;
import com.uthman.to_do_app.entity.User;
import com.uthman.to_do_app.enums.Priority;
import com.uthman.to_do_app.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository
        extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);

    Optional<Task> findByIdAndUser(Long id, User user);

    List<Task> findByUserAndStatus(
            User user,
            Status status
    );

    List<Task> findByUserAndPriority(
            User user,
            Priority priority
    );

    List<Task> findByUserAndDueDateBefore(
            User user,
            LocalDate date
    );

    List<Task> findByUserAndTitleContainingIgnoreCase(
            User user,
            String keyword
    );

    long countByUser(User user);

    long countByUserAndCompleted(
            User user,
            Boolean completed
    );

    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId AND t.user = :user")
    List<Task> findByCategoryIdAndUser(@Param("categoryId") Long categoryId, @Param("user") User user);
}
