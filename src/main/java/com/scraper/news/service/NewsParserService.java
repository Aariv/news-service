package com.scraper.news.service;

import com.scraper.news.model.News;
import com.scraper.news.model.NewsDetail;
import com.scraper.news.repository.NewsDetailRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

import static com.scraper.news.util.ScraperUtility.extractvalues;

@Service
public class NewsParserService {

    private final NewsDetailRepository newsDetailRepository;

    public NewsParserService(NewsDetailRepository newsDetailRepository) {
        this.newsDetailRepository = newsDetailRepository;
    }

    public void newsParserService(String scrapUrl) throws IOException {
        //String location = newsDetailRepository.findContentLocationByScrapUrl(scrapUrl);
        List<NewsDetail> newsDetailList = newsDetailRepository.findByScrapUrl(scrapUrl);
        for (NewsDetail newsDetailFromDb: newsDetailList) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(newsDetailFromDb.getContentLocation()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            }
            String tempContent = sb.toString();
            String content = extractvalues(tempContent.trim(), "<cite class=\"el-editorial-source\">", "<div class=\"zn-body__read-more-outbrain\">", 1).trim();
            String updatedDate = extractvalues(tempContent.trim(), "<p class=\"update-time\">", "<span id=\"js-pagetop_video_source\" class=\"video__source top_source\"></span>", 1).trim();
            String title = extractvalues(tempContent.trim(), "<h1 class=\"pg-headline\">", "</h1>", 1).trim();
            System.out.println("title..." + title);
            System.out.println("content..." + content.replaceAll("\\<.*?>","") );
            System.out.println("updated date..." + updatedDate);
        }
        // remove all html tags -> description
        /**
         * Todo: set description
         * set title
         * post date
         */

//        News news = new News();
//        news.setDescription(content);
    }

}
