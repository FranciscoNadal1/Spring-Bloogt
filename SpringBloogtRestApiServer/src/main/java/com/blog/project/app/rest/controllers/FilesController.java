package com.blog.project.app.rest.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.blog.project.app.models.dao.IFiles;
import com.blog.project.app.models.service.IFileService;

import net.minidev.json.JSONObject;

@RestController
@CrossOrigin
@RequestMapping("/api/files")
public class FilesController {

	@Autowired
	IFileService fileService;

	@Autowired
	IFiles fileDao;

	@GetMapping(
			value="/view/image/{id}",
			produces = {
					MediaType.IMAGE_PNG_VALUE, 
					MediaType.IMAGE_JPEG_VALUE, 
					MediaType.IMAGE_GIF_VALUE})
	@ResponseBody
	public ResponseEntity<Resource> handleFileUpload(@PathVariable(value = "id") String id) throws MalformedURLException {

		HttpHeaders headers = new HttpHeaders();
		Resource resource = fileService.load(id);
		
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	@PostMapping("/upload")
	public JSONObject handleFileUpload(@RequestParam("uploaded-file") List<MultipartFile> uploadedFiles)
			throws IOException {
		
		JSONObject responseJson = new JSONObject();
		responseJson.appendField("status", "OK");
		responseJson.appendField("message", "File was uploaded");
		responseJson.appendField("fileId", fileService.uploadNewFile(uploadedFiles.get(0)));
		return responseJson;
	}

}
