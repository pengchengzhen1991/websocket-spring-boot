package com.siwei.darwin.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractRepositoryCustom {

    @Autowired
    protected JPAQueryFactory queryFactory;

}
