package org.arjuna.urlshortener.context.transaction.shortener;

import org.arjuna.urlshortener.infrastructure.model.ResponseModel;
import org.springframework.stereotype.Service;

@Service
public interface ShortenerService {

	ResponseModel generateAndSave(ShortenerReqModel generatorReqModel);
	String findByUrlShort(String urlShort);
}
