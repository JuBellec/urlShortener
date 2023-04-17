package com.example.urlshortener.service;

import com.example.urlshortener.dto.LongUrlRequest;
import com.example.urlshortener.entity.Url;
import com.example.urlshortener.exception.ExpirationDateException;
import com.example.urlshortener.repository.UrlRepository;
import com.example.urlshortener.utils.Utils;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.urlshortener.message.ErrorMessages.URL_EXPIRED;
import static com.example.urlshortener.message.ErrorMessages.URL_NOT_FOUND;

@Service
public class UrlServiceImpl implements UrlService{
    @Autowired
    private UrlRepository urlRepository;

    private static final Logger log = LoggerFactory.getLogger(UrlServiceImpl.class);

    /**
     * This function convert the given url in shorten url with a 5 minutes validity
     * @param request The long url given
     * @return Shorten url as String
     */
    public String convertToShortUrl(LongUrlRequest request) {
        /*
        If url already exist, we send the short url
        else we save the url and send the short url
         */
        Optional<Url> urlRequest = urlRepository.findBylongUrl(request.getLongUrl());

        if(urlRequest.isPresent()){
            log.info("Url already exist");
            return Utils.encode(urlRequest.get().getShortUrl());
        }else{
            Url url = new Url();
            url.setLongUrl(request.getLongUrl());
            url.setExpirationDate(new Date(System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(5)));


            log.info("Saving url");
            Url urlSaved = urlRepository.save(url);

            return Utils.encode(urlSaved.getShortUrl());
        }
    }

    /**
     * This function get the original url from the shorten url
     * @param shortUrl the short url
     * @return the original url as String
     */
    public String getOriginalUrl(String shortUrl) throws ExpirationDateException {
        long id = Utils.decode(shortUrl);
        Url url = urlRepository.findById(id)
                .orElseThrow(() -> {
                    log.info("Url does not exist");
                    return new EntityNotFoundException(URL_NOT_FOUND + shortUrl);
                });

        if(url.getExpirationDate().after(new Date(System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(5)))){
            log.info("Url is expired");
            throw new ExpirationDateException(URL_EXPIRED);
        }

        return url.getLongUrl();
    }
}
