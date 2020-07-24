package com.ambbarees.shrink.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import com.ambbarees.shrink.model.UrlMap;
import com.ambbarees.shrink.utils.utils;

@Repository
public class UrlMapRepositoryCustomImpl implements UrlMapRepositoryCustom {

	@Autowired
	public UrlMapRepository repo;

	public UrlMap saveWithRetry(UrlMap entity) throws Exception {

		int i = 0;
		while (i < 5) {
			try {
				i++;
				return repo.save(entity);
			} catch (DataIntegrityViolationException e) {
				entity.setId(utils.randomString());
				continue;
			}
		}
		throw new Exception("failed to save the API key");
	}
}
