package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends EntityRepository<Task> {

    Page<Task> findByUserId(Long userId, Pageable pageable);
}
