package com.scraper.news.repository;

import com.scraper.news.model.NewsDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsDetailRepository extends JpaRepository<NewsDetail, Long> {
    String findContentLocationByScrapUrl(String scrapUrl);

    List<NewsDetail> findByScrapUrl(String scrapeUrl);
}
