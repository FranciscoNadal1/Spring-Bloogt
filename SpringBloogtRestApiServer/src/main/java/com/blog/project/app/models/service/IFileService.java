package com.blog.project.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

	public Resource load(String filename) throws MalformedURLException;
	public String copy(MultipartFile file, String id) throws IOException;
	public boolean delete(String filename);
	public void deleteAll();
	public void init() throws IOException;
	
	///////////////////////////////////////////////////////////////////////
	public String uploadNewFile(MultipartFile uploadedFiles) throws IOException;
}
