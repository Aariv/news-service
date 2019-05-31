package com.scraper.news.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "news")
@Data
public class News {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description", length = 512, nullable = false)
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(name = "postedDate", nullable = false)
    private Date postedDate;

}
