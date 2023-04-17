package com.example.urlshortener.scheduledtask;


import com.example.urlshortener.repository.UrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class DeleteExpiratedUrlTask {
    @Autowired
    UrlRepository urlRepository;

    private static final Logger log = LoggerFactory.getLogger(DeleteExpiratedUrlTask.class);

    @Scheduled(fixedRate = 300000)
    public void reportCurrentTime() {
        log.info("Delete expired url");
       urlRepository.deleteExpiredUrl(new Date(System.currentTimeMillis()));
    }
}

