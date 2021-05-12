package com.blog.project.app.models.service.implementation;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.blog.project.app.entities.FileStorage;
import com.blog.project.app.models.dao.IFiles;
import com.blog.project.app.models.service.IFileService;

@Service
public class FileServiceImpl implements IFileService{
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	IFiles fileDao;
	
	private final static String UPLOADS_FOLDER = "uploads";
//////////////////////////////////////////////////////////////////////////	
	
	public String uploadNewFile(MultipartFile uploadedFiles) throws IOException {

	     String documentId = UUID.randomUUID().toString();		 
	     String fileName = documentId;		    
	     String documentType = uploadedFiles.getContentType();	
	     
	     String type = "";
	     
	     if(documentType.startsWith("image"))
		      type = "/images/";
	     
	     String uploadDir = type + fileName;
			Path rootPath = getPath(documentId);
			Files.copy(uploadedFiles.getInputStream(), rootPath);

	     FileStorage newFile = new FileStorage(documentId, fileName, documentType,  uploadDir);
	     fileDao.save(newFile);	
	     
	     return documentId;
	}
	/////////////////////////////////////////////////////////////////////
	@Override
	public Resource load(String filename) throws MalformedURLException {
		Path pathFoto = getPath(filename);
		log.info("pathFoto: " + pathFoto);

		Resource recurso = new UrlResource(pathFoto.toUri());

		if (!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Image can't be loaded: " + pathFoto.toString());
		}
		return recurso;
	}
/*
	@Override
	public String copy(MultipartFile file, String id) throws IOException {
		//String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
		String uniqueFilename = id;
		Path rootPath = getPath(uniqueFilename);

		log.info("rootPath: " + rootPath);

		Files.copy(file.getInputStream(), rootPath);

		return uniqueFilename;
	}
*/
	@Override
	public boolean delete(String filename) {
		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();

		if (archivo.exists() && archivo.canRead()) {
			if (archivo.delete()) {
				return true;
			}
		}
		return false;
	}

	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());

	}

	@Override
	public void init() throws IOException {
		// TODO Auto-generated method stub
		Files.createDirectory(Paths.get(UPLOADS_FOLDER));
	}
	@Override
	public String copy(MultipartFile file, String id) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}
