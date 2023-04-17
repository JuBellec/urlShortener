package com.example.urlshortener.controller;

import com.example.urlshortener.dto.LongUrlRequest;
import com.example.urlshortener.dto.UrlResponse;
import com.example.urlshortener.exception.ExpirationDateException;
import com.example.urlshortener.service.UrlServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static com.example.urlshortener.message.ErrorMessages.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class UrlControllerTest {
    @InjectMocks
    UrlController urlController;

    @Mock
    UrlServiceImpl urlService;

    private static final String LONG_URL = "https://thenewstack.io/ask-a-developer-code-coverage-uncovered/";
    private static final String SHORTEN_URL = "shortenUrl";

    @Test
    public void convertToShortUrlTest(){
        LongUrlRequest request = new LongUrlRequest();
        request.setLongUrl(LONG_URL);

        when(urlService.convertToShortUrl(request)).thenReturn(SHORTEN_URL);

        ResponseEntity result = urlController.convertToShortUrl(request);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        UrlResponse body = (UrlResponse) result.getBody();
        if(body != null){
            assertThat(body.getUrl()).isEqualTo(SHORTEN_URL);
        }

    }

    @Test
    public void convertToShortUrlInvalidUrlTest(){
        LongUrlRequest request = new LongUrlRequest();
        request.setLongUrl("https://thenewstack. io/ask-a-developer-code-coverage-uncovered/");

        ResponseEntity result = urlController.convertToShortUrl(request);

        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
        assertThat(result.getBody()).isEqualTo(INVALID_URL);
    }

    @Test
    public void getByShortUrlTest() throws ExpirationDateException {

        when(urlService.getOriginalUrl(SHORTEN_URL)).thenReturn(LONG_URL);

        ResponseEntity result = urlController.getOriginalUrl(SHORTEN_URL);

        assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();
        UrlResponse body = (UrlResponse) result.getBody();
        if(body != null){
            assertThat(body.getUrl()).isEqualTo(LONG_URL);
        }
    }

    @Test
    public void getByShortUrlNotFoundTest() throws ExpirationDateException {

        when(urlService.getOriginalUrl(SHORTEN_URL)).thenThrow(new EntityNotFoundException(URL_NOT_FOUND + SHORTEN_URL));

        ResponseEntity result = urlController.getOriginalUrl(SHORTEN_URL);

        assertThat(result.getStatusCode().is4xxClientError()).isTrue();
        assertThat(result.getBody()).isEqualTo(URL_NOT_FOUND + SHORTEN_URL);
    }

    @Test
    public void getByShortUrlExpiredTest() throws ExpirationDateException {

        when(urlService.getOriginalUrl(SHORTEN_URL)).thenThrow(new ExpirationDateException(URL_EXPIRED));

        ResponseEntity result = urlController.getOriginalUrl(SHORTEN_URL);

        assertThat(result.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(result.getBody()).isEqualTo(URL_EXPIRED);
    }
}
