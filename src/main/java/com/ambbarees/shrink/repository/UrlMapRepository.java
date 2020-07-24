package com.ambbarees.shrink.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ambbarees.shrink.model.UrlMap;

@Repository
public interface UrlMapRepository extends CrudRepository<UrlMap, String>,UrlMapRepositoryCustom {

	UrlMap findByUrl(String url);

}
