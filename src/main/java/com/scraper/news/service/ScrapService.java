package com.scraper.news.service;

import com.scraper.news.model.News;
import com.scraper.news.repository.NewsRepository;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class ScrapService {

    private final NewsRepository newsrepository;
    private final NewsAggregatorService aggregatorService;
    private final NewsParserService parserService;

    @Autowired
    public ScrapService (NewsRepository newsRepository, NewsAggregatorService aggregatorService,
    NewsParserService parserService) {
        this.newsrepository = newsRepository;
        this.aggregatorService = aggregatorService;
        this.parserService = parserService;
    }

    public String startScraping(String scrapUrl) throws IOException, ParseException, NoSuchAlgorithmException {
        aggregatorService.newsAggregatorMainPage(scrapUrl);
        return "Please see aggregator service logs for information";
    }

    public String startParsing(String scrapUrl) throws IOException {
        parserService.newsParserService(scrapUrl);
        return null;
    }

    public Optional<News> getNewsById(Long id) {
        return newsrepository.findById(id);
    }


}
