package com.ambbarees.shrink.repository;

import com.ambbarees.shrink.model.UrlMap;

public interface UrlMapRepositoryCustom {

	UrlMap saveWithRetry(UrlMap entity) throws Exception;

}
