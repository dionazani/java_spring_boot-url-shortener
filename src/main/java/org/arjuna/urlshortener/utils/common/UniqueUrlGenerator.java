package org.arjuna.urlshortener.utils.common;

import org.springframework.stereotype.Service;

@Service
public class UniqueUrlGenerator {

	public String idToShortUrl(int n) {
		char map[] =  "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
		StringBuffer shortUrl = new StringBuffer();
		
		// convert given integer id to base 62 number
		while (n > 0) {
			// use above map to store actual character in short URL.
			shortUrl.append(map[n % 62]);
			n = n/62;
		}
		
		return shortUrl.reverse().toString();
	}
	
	public int shortUrlToID(String shortURL) {
		int id = 0; // initialize result 
	    
        // A simple base conversion logic 
        for (int i = 0; i < shortURL.length(); i++) 
        { 
            if ('a' <= shortURL.charAt(i) && 
                       shortURL.charAt(i) <= 'z') 
            id = id * 62 + shortURL.charAt(i) - 'a'; 
            if ('A' <= shortURL.charAt(i) && 
                       shortURL.charAt(i) <= 'Z') 
            id = id * 62 + shortURL.charAt(i) - 'A' + 26; 
            if ('0' <= shortURL.charAt(i) && 
                       shortURL.charAt(i) <= '9') 
            id = id * 62 + shortURL.charAt(i) - '0' + 52; 
        } 
        return id; 
	}
	
}
