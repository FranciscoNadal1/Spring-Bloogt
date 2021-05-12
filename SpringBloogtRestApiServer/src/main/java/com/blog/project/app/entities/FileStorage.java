package com.blog.project.app.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FileStorage")
@Data  @NoArgsConstructor
public class FileStorage {
	
    
    public FileStorage(String documentId, String fileName, String documentType, String uploadDir) {
		this.documentId = documentId;
		this.fileName = fileName;
		this.documentType = documentType;
		this.uploadDir = uploadDir;
	}

	@Id 
/*	
 * @GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid",strategy = "uuid")
	*/
    private String documentId;
        
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "document_type")
    private String documentType;
    
    @Column(name = "document_format")
    private String documentFormat;
    
    @Column(name = "upload_dir")
    private String uploadDir;
    
    //////////////////////////////
    
    
}
