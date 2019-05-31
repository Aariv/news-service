package com.scraper.news.service;

import com.scraper.news.model.NewsDetail;
import com.scraper.news.repository.NewsDetailRepository;
import com.scraper.news.util.ScraperUtility;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static com.scraper.news.util.ScraperUtility.*;

@Service
public class NewsAggregatorService {

    @Autowired
    private ScraperUtility scraperUtility;

    public void newsAggregatorMainPage(String scrapUrl) throws IOException, ParseException, NoSuchAlgorithmException {
        URL url;
        HttpsURLConnection connection = null;
        try {
            url = new URL(scrapUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            connection.addRequestProperty("Accept-Encoding","gzip, deflate, br");
            connection.addRequestProperty("Accept-Language","en-US,en;q=0.9");
            connection.addRequestProperty("Connection","keep-alive");
//            connection.addRequestProperty("Cookie","countryCode=IN; geoData=sira|KA|572137|IN|AS|530|broadband; FastAB=0=5341,1=1009,2=5496,3=3443,4=8900,5=0762,6=6296,7=2568,8=0896,9=6190; tryThing00=4153; tryThing01=1932; tryThing02=0258; optimizelyEndUserId=oeu1559238212348r0.76427239565349; cnprevpage_pn=cnn%3Ac%3Aedition%3A%2F2019%2F05%2F30%2Fpolitics%2Ftrump-russia-election-interference-help%2F; s_cc=true; s_fid=11E7A9C81E092F10-30FA4A4F7AF3DE2C; s_sq=%5B%5BB%5D%5D; ug=5cf016450777f50a3f86070014a858c3; ugs=1; s_vi=[CS]v1|2E780B2385037324-4000119740019CC0[CE]; __qca=P0-1204429906-1559238218835; gig_hasGmid=ver2; s_ppv=4; _cb_ls=1; _cb=Bl-X-s_pyztB28dmX; _chartbeat2=.1559238230678.1559238230678.1.ldEFiD_Lm15BlX1YGB_-9M4b6cV.1; _cb_svref=null; _v__chartbeat3=B6gcG9CV0PK1KF-ok; bounceClientVisit340v=N4IgNgDiBcIBYBcEQM4FIDMBBNAmAYnvgKYAmAlguQPYB2AdAMa0OPUC2RuADAIwCcRbgFYiGbkQjUwlco3QEEAJwCu7CAFpVKFOQCGG4mGKMqdDeVoJiSgGY3itRsQ1wjEIpdLEAHvUTsYCAANCBKMCAgAL5AA");
            connection.addRequestProperty("Upgrade-Insecure-Requests","1");
            connection.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.19 Safari/537.36");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String mainPageBuffer = getHTTPSHTMLContent(connection,true);
//        System.out.println("mainPageBuffer..." + mainPageBuffer);
        List<String> detailUrlList = getArticleListUrls(mainPageBuffer);
        for(String detailUrl : detailUrlList) {
            newsAggregatorDetailPage(scrapUrl, detailUrl);
        }
    }
    private static List<String> getArticleListUrls(String mainPageBuffer) throws ParseException {
        List<String> urlList = new ArrayList<>();
        String content = extractvalues(mainPageBuffer.trim(), ", siblings:    ", ", registryURL:", 1).trim();
//        System.out.println("content..." + content);
        List<String> articleInfoList = jsonArrayValueExtractor(content,"articleList");
        for(String articleInfo: articleInfoList) {
            urlList.add(jsonValueExtractor(articleInfo, "uri"));
        }
        return urlList;
    }

    private void newsAggregatorDetailPage(String scrapUrl, String detailUrl) throws IOException, NoSuchAlgorithmException {
        URL url = null;
        HttpsURLConnection connection = null;
        try {
            url = new URL(scrapUrl+detailUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            connection.addRequestProperty("Accept-Encoding","gzip, deflate, br");
            connection.addRequestProperty("Accept-Language","en-US,en;q=0.9");
            connection.addRequestProperty("Connection","keep-alive");
//            connection.addRequestProperty("Cookie","countryCode=IN; geoData=sira|KA|572137|IN|AS|530|broadband; FastAB=0=5341,1=1009,2=5496,3=3443,4=8900,5=0762,6=6296,7=2568,8=0896,9=6190; tryThing00=4153; tryThing01=1932; tryThing02=0258; optimizelyEndUserId=oeu1559238212348r0.76427239565349; cnprevpage_pn=cnn%3Ac%3Aedition%3A%2F2019%2F05%2F30%2Fpolitics%2Ftrump-russia-election-interference-help%2F; s_cc=true; s_fid=11E7A9C81E092F10-30FA4A4F7AF3DE2C; s_sq=%5B%5BB%5D%5D; ug=5cf016450777f50a3f86070014a858c3; ugs=1; s_vi=[CS]v1|2E780B2385037324-4000119740019CC0[CE]; __qca=P0-1204429906-1559238218835; gig_hasGmid=ver2; s_ppv=4; _cb_ls=1; _cb=Bl-X-s_pyztB28dmX; _chartbeat2=.1559238230678.1559238230678.1.ldEFiD_Lm15BlX1YGB_-9M4b6cV.1; _cb_svref=null; _v__chartbeat3=B6gcG9CV0PK1KF-ok; bounceClientVisit340v=N4IgNgDiBcIBYBcEQM4FIDMBBNAmAYnvgKYAmAlguQPYB2AdAMa0OPUC2RuADAIwCcRbgFYiGbkQjUwlco3QEEAJwCu7CAFpVKFOQCGG4mGKMqdDeVoJiSgGY3itRsQ1wjEIpdLEAHvUTsYCAANCBKMCAgAL5AA");
            connection.addRequestProperty("Referer","https://edition.cnn.com/");
            connection.addRequestProperty("Upgrade-Insecure-Requests","1");
            connection.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.19 Safari/537.36");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        scraperUtility.saveNewsDetailPage(scrapUrl, detailUrl, getHTTPSHTMLContent(connection,true));
       // String mainPageBuffer = getHTTPSHTMLContent(connection,true);
       // System.out.println("mainPageBuffer..." + mainPageBuffer);

    }
}
