package com.scraper.news.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "newsDetail")
@Data
public class NewsDetail {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "scrapUrl", nullable = false)
    private String scrapUrl;

    @Column(name = "detailPageUrl", nullable = false)
    private String detailPageUrl;

    @Column(name = "contentLocation", nullable = false)
    private String contentLocation;

    @CreatedDate
    @Column(name = "createdDate", nullable = false)
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "lastModifiedDate", nullable = false)
    private Date lastModifiedDate;

    @Column(name = "isParsed", nullable = true)
    private Boolean isParsed;
}
