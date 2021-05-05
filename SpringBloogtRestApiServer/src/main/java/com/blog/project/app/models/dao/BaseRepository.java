package com.blog.project.app.models.dao;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
interface BaseRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {


}
