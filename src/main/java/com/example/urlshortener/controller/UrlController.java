package com.example.urlshortener.controller;

import com.example.urlshortener.dto.LongUrlRequest;
import com.example.urlshortener.dto.UrlResponse;
import com.example.urlshortener.exception.ExpirationDateException;
import com.example.urlshortener.service.UrlServiceImpl;
import com.example.urlshortener.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.urlshortener.message.ErrorMessages.INVALID_URL;

@RestController
@RequestMapping("/api")
public class UrlController {
    @Autowired
    UrlServiceImpl urlService;

    @PostMapping
    public ResponseEntity convertToShortUrl(@RequestBody LongUrlRequest request) {

        if(Utils.isUrlValid(request.getLongUrl())){
            UrlResponse urlResponse = new UrlResponse();
            urlResponse.setUrl(urlService.convertToShortUrl(request));

            return ResponseEntity.status(HttpStatus.CREATED).body(urlResponse);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_URL);
        }
    }

    @GetMapping(value = "/{shortUrl}")
    public ResponseEntity getOriginalUrl(@PathVariable String shortUrl) {
        try {
            UrlResponse urlResponse = new UrlResponse();
            urlResponse.setUrl(urlService.getOriginalUrl(shortUrl));

            return ResponseEntity
                    .status(HttpStatus.FOUND)
                    .body(urlResponse);
        }catch (EntityNotFoundException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }catch (ExpirationDateException e){
            return ResponseEntity
                    .status(HttpStatus.MOVED_PERMANENTLY)
                    .body(e.getMessage());
        }
    }
}