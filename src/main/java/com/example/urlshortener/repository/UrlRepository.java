package com.example.urlshortener.repository;

import com.example.urlshortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findBylongUrl(String longUrl);

    @Transactional
    @Modifying
    @Query("delete from Url u where u.expirationDate < :currentDate")
    void deleteExpiredUrl(@Param("currentDate") Date currentDate);
}
