package com.emobile.springtodo.repository;

import com.emobile.springtodo.entity.CommonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityRepository<T extends CommonEntity> extends JpaRepository<T, Long> {

    void deleteById(Long id);
}
