package com.blog.project.app.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

//@Entity
public class Category_hashtag  implements Serializable {

	@Id
	@Column(unique = true, nullable = false)
	private int hashtag_id;
	

	@Id
	@Column(unique = true, nullable = false)
	private int category_id;
}
