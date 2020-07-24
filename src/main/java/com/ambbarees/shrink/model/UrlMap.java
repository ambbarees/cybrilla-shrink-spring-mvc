package com.ambbarees.shrink.model;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UrlMap {

	@javax.persistence.Id
	String id;
	@Column(unique = true)
	String url;
}
