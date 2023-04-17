package com.example.urlshortener.service;

import com.example.urlshortener.dto.LongUrlRequest;
import com.example.urlshortener.exception.ExpirationDateException;

public interface UrlService {
    String convertToShortUrl(LongUrlRequest request);
    String getOriginalUrl(String shortUrl) throws ExpirationDateException;
}
