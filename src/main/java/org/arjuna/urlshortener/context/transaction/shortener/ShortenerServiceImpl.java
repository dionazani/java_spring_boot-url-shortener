package org.arjuna.urlshortener.context.transaction.shortener;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.arjuna.urlshortener.infrastructure.entity.UrlDetailEntity;
import org.arjuna.urlshortener.infrastructure.entity.UrlEntity;
import org.arjuna.urlshortener.infrastructure.model.ResponseModel;
import org.arjuna.urlshortener.infrastructure.repository.UrlDetailRepository;
import org.arjuna.urlshortener.infrastructure.repository.UrlRepository;
import org.arjuna.urlshortener.utils.common.UniqueUrlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.google.common.hash.Hashing;
import jakarta.transaction.Transactional;

@Service
public class ShortenerServiceImpl implements ShortenerService {

	@Value("${shortUrl.url}")
    private String shortUrl;
	
	@Value("${shortUrl.expirationDuration}")
    private int expirationDuration;
	
	@Autowired
	private UrlRepository urlRepository;
	
	@Autowired
	private UrlDetailRepository urlDetailRepository;
	
	@Autowired
	private UniqueUrlGenerator uniqueUrlGenerator;
	
	@Override
	@Transactional
	public ResponseModel generateAndSave(ShortenerReqModel generatorReqModel) {

		System.out.println(generatorReqModel.getUrlOriginal());
		
		ResponseModel responseModel = null;
		
		var urlEntity = new UrlEntity();
		this.urlRepository.save(urlEntity);
		int urlId = urlEntity.getId();

		// generate hash and check to database, loop until find unique url_short.
		LocalDateTime time = LocalDateTime.now();
		String shortUrl = Hashing.murmur3_32().hashString(time.toString().concat(String.valueOf(urlId)), StandardCharsets.UTF_8).toString();
		
		var urlDetailEntity = new UrlDetailEntity();
		urlDetailEntity.setUrlId(urlId);
		urlDetailEntity.setOriginalUrl(generatorReqModel.getUrlOriginal());
		urlDetailEntity.setShortUrl(shortUrl);
		urlDetailEntity.setExpireDuration(expirationDuration);
		urlDetailRepository.save(urlDetailEntity);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("shortUrl", shortUrl);
		
		responseModel = new ResponseModel();
		responseModel.setHttpStatusCode(200);
		responseModel.setResponseCode("000");
		responseModel.setResponseMessage("success");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		responseModel.setTimeStamp(timestamp);
		responseModel.setData(map);
		
		return responseModel;
	}
	
	private String getShortUrl(int urlId) {
		return uniqueUrlGenerator.idToShortUrl(urlId);
	}
	
	private boolean isUrlShortExist(String urlShort) {
		Optional<UrlDetailEntity> urlEntity = this.urlDetailRepository.findByShortUrl(urlShort);
		return urlEntity.isPresent();
	}

	/*
	@Override
	public String findByUrlShort(String urlShort) {
		
		int urlId = uniqueUrlGenerator.shortUrlToID(urlShort);
		
		String urlOriginal = "";
		var urlDetailEntity = this.urlDetailRepository.findByUrlId(urlId);
		
		LocalDateTime tsCurrent = LocalDateTime.now();
		LocalDateTime tsCreatedAt = urlDetailEntity.get().getCreatedAt().toLocalDateTime();
		Duration duration = Duration.between(tsCreatedAt, tsCurrent);
		if (duration.toHours() > expirationDuration) {
			urlOriginal = "410";
		}
		else {
			urlOriginal = urlDetailEntity.get().getOriginalUrl();
		}
		
		return urlOriginal;
	}
	*/
	
	public String findByUrlShort(String urlShort) {
		
		String urlOriginal = "";
		var urlDetailEntity = this.urlDetailRepository.findByShortUrl(urlShort);
		
		LocalDateTime tsCurrent = LocalDateTime.now();
		LocalDateTime tsCreatedAt = urlDetailEntity.get().getCreatedAt().toLocalDateTime();
		Duration duration = Duration.between(tsCreatedAt, tsCurrent);
		if (duration.toHours() > expirationDuration) {
			urlOriginal = "410";
		}
		else {
			urlOriginal = urlDetailEntity.get().getOriginalUrl();
		}
		
		return urlOriginal;
	}

}
