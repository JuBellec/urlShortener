package com.example.urlshortener.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "url")
@Data
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shortUrl;

    @Column(nullable = false)
    private String longUrl;

    @Column(nullable = false)
    private Date expirationDate;
}
