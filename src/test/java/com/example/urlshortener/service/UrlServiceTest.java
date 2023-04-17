package com.example.urlshortener.service;

import com.example.urlshortener.dto.LongUrlRequest;
import com.example.urlshortener.entity.Url;
import com.example.urlshortener.exception.ExpirationDateException;
import com.example.urlshortener.repository.UrlRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.urlshortener.message.ErrorMessages.URL_EXPIRED;
import static com.example.urlshortener.message.ErrorMessages.URL_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class UrlServiceTest {
    @InjectMocks
    UrlServiceImpl urlService;

    @Mock
    UrlRepository urlRepository;

    private static final String LONG_URL = "https://thenewstack.io/ask-a-developer-code-coverage-uncovered/";
    private static final String SHORTEN_URL = "1";

    private static final Date EXPIRATION_DATE = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));

    @Test
    public void convertToShortUrlTest(){
        LongUrlRequest longUrlRequest = new LongUrlRequest();
        longUrlRequest.setLongUrl(LONG_URL);
        Url url = new Url();
        url.setShortUrl(1);
        url.setLongUrl(LONG_URL);
        url.setExpirationDate(EXPIRATION_DATE);

        when(urlRepository.findBylongUrl(LONG_URL)).thenReturn(Optional.of(url));


        String result = urlService.convertToShortUrl(longUrlRequest);
        assertThat(result).isEqualTo(SHORTEN_URL);

    }

    @Test
    public void convertToShortUrlAlreadyExistTest(){
        LongUrlRequest longUrlRequest = new LongUrlRequest();
        longUrlRequest.setLongUrl(LONG_URL);

        Url urlSaved = new Url();
        urlSaved.setShortUrl(1L);
        urlSaved.setLongUrl(LONG_URL);
        urlSaved.setExpirationDate(EXPIRATION_DATE);

        when(urlRepository.findBylongUrl(LONG_URL)).thenReturn(Optional.empty());
        when(urlRepository.save(any(Url.class))).thenReturn(urlSaved);

        String result = urlService.convertToShortUrl(longUrlRequest);

        assertThat(result).isEqualTo(SHORTEN_URL);
    }

    @Test
    public void getOriginalUrlTest() throws ExpirationDateException {
        Url url = new Url();
        url.setShortUrl(1L);
        url.setLongUrl(LONG_URL);
        url.setExpirationDate(EXPIRATION_DATE);

        when(urlRepository.findById(1L)).thenReturn(Optional.of(url));

        String result = urlService.getOriginalUrl(SHORTEN_URL);

        assertThat(result).isEqualTo(LONG_URL);
    }

    @Test
    public void getOriginalUrlNotFoundTest(){

        when(urlRepository.findById(1L)).thenReturn(Optional.empty());

        try {
            urlService.getOriginalUrl(SHORTEN_URL);
        }catch (EntityNotFoundException e){
            assertThat(e.getMessage()).isEqualTo(URL_NOT_FOUND + SHORTEN_URL);
        } catch (ExpirationDateException e) {
            assertThat(e.getMessage()).isEqualTo(URL_EXPIRED);
        }
    }

    @Test
    public void getOriginalUrlExpiredTest(){
        Url url = new Url();
        url.setShortUrl(1L);
        url.setLongUrl(LONG_URL);
        url.setExpirationDate(new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(5)));

        when(urlRepository.findById(1L)).thenReturn(Optional.of(url));

        try {
            urlService.getOriginalUrl(SHORTEN_URL);
        }catch (EntityNotFoundException e){
            assertThat(e.getMessage()).isEqualTo(URL_NOT_FOUND + SHORTEN_URL);
        } catch (ExpirationDateException e) {
            assertThat(e.getMessage()).isEqualTo(URL_EXPIRED);
        }
    }
}
