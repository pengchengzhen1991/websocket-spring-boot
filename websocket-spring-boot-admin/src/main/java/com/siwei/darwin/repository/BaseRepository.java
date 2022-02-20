package com.siwei.darwin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<E, ID extends Serializable>
        extends JpaRepository<E, ID>, JpaSpecificationExecutor<E>, QuerydslPredicateExecutor<E> {

}
