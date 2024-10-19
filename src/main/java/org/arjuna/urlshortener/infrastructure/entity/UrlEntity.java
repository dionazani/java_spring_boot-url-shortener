package org.arjuna.urlshortener.infrastructure.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@Entity
@Table(name="url")
public class UrlEntity {

	@Id
	private String id;
	
	@NotBlank(message="urlOriginal cannot be empty")
	private String urlOriginal;
	
	@NotBlank(message="urlShort cannot be empty")
	private String urlShort;
	
	@CreationTimestamp
	private Timestamp createdAt;
	
	private int stepCreated;
}
