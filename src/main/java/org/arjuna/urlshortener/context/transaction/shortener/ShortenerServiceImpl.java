package org.arjuna.urlshortener.context.transaction.shortener;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.arjuna.urlshortener.infrastructure.entity.UrlEntity;
import org.arjuna.urlshortener.infrastructure.model.ResponseModel;
import org.arjuna.urlshortener.infrastructure.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ShortenerServiceImpl implements ShortenerService {

	@Value("${shortUrl.url}")
    private String shortUrl;
	
	@Value("${shortUrl.expirationDuration}")
    private int expirationDuration;
	
	@Autowired
	private UrlRepository urlRepository;
	
	@Override
	@Transactional
	public ResponseModel generateAndSave(ShortenerReqModel generatorReqModel) {

		System.out.println(generatorReqModel.getUrlOriginal());
		
		ResponseModel responseModel = null;
		
		// generate hash and check to database, loop until find unique url_short.
		String urlShort = "";
		boolean isShortUrlExist = true;
		int step = 0;
		while(isShortUrlExist) {
			urlShort = this.getUrlShort();
			isShortUrlExist = this.isShortUrlExist(urlShort);
			step++;
		}
		
		var urlEntity = new UrlEntity();
		urlEntity.setId(String.valueOf(UUID.randomUUID()));
		urlEntity.setUrlOriginal(generatorReqModel.getUrlOriginal());
		urlEntity.setUrlShort(urlShort);
		urlEntity.setStepCreated(step);
		urlRepository.save(urlEntity);
		
		responseModel = new ResponseModel();
		responseModel.setHttpStatusCode(200);
		responseModel.setResponseCode("000");
		responseModel.setResponseMessage("success");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		responseModel.setTimeStamp(timestamp);
		
		return responseModel;
	}
	
	private String getUrlShort() {
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
		
		return uuidString.substring(uuidString.length() -7);
	}
	
	private boolean isShortUrlExist(String shortUrl) {
		Optional<UrlEntity> urlEntity = this.urlRepository.findByUrlShort(shortUrl);
		return urlEntity.isPresent();
	}

	@Override
	public String findByUrlShort(String urlShort) {
		
		String urlOriginal = "";
		var urlEntity = this.urlRepository.findByUrlShort(urlShort);
		
		LocalDateTime tsCurrent = LocalDateTime.now();
		LocalDateTime tsCreatedAt = urlEntity.get().getCreatedAt().toLocalDateTime();
		Duration duration = Duration.between(tsCreatedAt, tsCurrent);
		if (duration.toHours() > expirationDuration) {
			urlOriginal = "410";
		}
		else {
			urlOriginal = urlEntity.get().getUrlOriginal();
		}
		
		return urlOriginal;
	}

}
