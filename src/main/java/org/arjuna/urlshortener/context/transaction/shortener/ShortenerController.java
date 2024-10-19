package org.arjuna.urlshortener.context.transaction.shortener;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("v1/transaction/shortener")
public class ShortenerController {

	@Autowired
	private ShortenerService generatorService;
	
	@PostMapping(path="/", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Object> login(@RequestBody ShortenerReqModel generatorReqModel) {
		
		System.out.println(generatorReqModel.getUrlOriginal());
		
		ResponseEntity<Object> responseEntity = null;

        try {
        
            var responseModel = this.generatorService.generateAndSave(generatorReqModel);
            responseEntity = ResponseEntity.status(responseModel.getHttpStatusCode())
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(responseModel);

        }
        catch(Exception ex) {
        	ex.printStackTrace();
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(ex.getMessage());
        }

        return responseEntity;

	}
	
	@GetMapping(path="/{urlShort}")
	public ResponseEntity<Object> redirect(@PathVariable("urlShort") String urlShort) {
		
		String urlOriginal = "";
		
		try {
			urlOriginal = this.generatorService.findByUrlShort(urlShort);
			
			 //RedirectView
			if (urlOriginal.toUpperCase().equals("410")) {
				return ResponseEntity.status(HttpStatus.GONE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("URL has been expired");
			}
			else {
				return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlOriginal)).build();
			}
            
		}
		catch(Exception ex) {
        	ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(ex.getMessage());
        }

	}

}
