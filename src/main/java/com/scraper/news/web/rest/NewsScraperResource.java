package com.scraper.news.web.rest;

import com.scraper.news.service.ScrapService;
import com.scraper.news.web.vm.RequestVM;
import com.scraper.news.web.vm.ResponseVM;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
public class NewsScraperResource {

    private final ScrapService scrapService;

    @Autowired
    public NewsScraperResource(ScrapService scrapService) {
        this.scrapService = scrapService;
    }

    @GetMapping("/scrapper")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ResponseVM> startScraping(@RequestBody RequestVM requestVM) throws IOException, ParseException, NoSuchAlgorithmException {
        ResponseVM response = new ResponseVM();
        response.setData(scrapService.startScraping(requestVM.getScrapUrl()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/parser")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ResponseVM> startParser(@RequestBody RequestVM requestVM) throws IOException, ParseException, NoSuchAlgorithmException {
        ResponseVM response = new ResponseVM();
        response.setData(scrapService.startParsing(requestVM.getScrapUrl()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/scrapper/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ResponseVM> getNewsById(@PathVariable Long id) {
        ResponseVM response = new ResponseVM();
        response.setData(scrapService.getNewsById(id).toString());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
