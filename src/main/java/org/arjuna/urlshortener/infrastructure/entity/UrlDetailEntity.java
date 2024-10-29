package org.arjuna.urlshortener.infrastructure.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@Entity
@Table(name="url_detail")
public class UrlDetailEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private int urlId;
	
	@NotBlank(message="originalUrl cannot be empty")
	private String originalUrl;
	
	@NotBlank(message="shortUrl cannot be empty")
	private String shortUrl;
	
	private int expireDuration;
	
	@CreationTimestamp
	private Timestamp createdAt;
	
	private int stepCreated;
}
