package com.scraper.news.util;

import com.scraper.news.model.NewsDetail;
import com.scraper.news.repository.NewsDetailRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Component
public class ScraperUtility {

    private final NewsDetailRepository newsDetailRepository;

    ScraperUtility(final NewsDetailRepository newsDetailRepository){
        this.newsDetailRepository = newsDetailRepository;
    }
    public static String sha1Hash(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(str.getBytes());
        return Base64.getUrlEncoder().encodeToString(md.digest());
    }

    public void saveNewsDetailPage(String scrapUrl, String detailUrl, String content) throws IOException, NoSuchAlgorithmException {
        String fileName = System.getProperty("user.home") + "/news/" + sha1Hash(scrapUrl + detailUrl) + "_" +System.currentTimeMillis() + ".html";
        FileWriter fileWriter = new FileWriter(fileName);
        fileWriter.write(content);
        fileWriter.close();

        NewsDetail newsDetail = new NewsDetail();
        newsDetail.setScrapUrl(scrapUrl);
        newsDetail.setContentLocation(fileName);
        newsDetail.setDetailPageUrl(detailUrl);
        newsDetail.setCreatedDate(new Date());
        newsDetail.setLastModifiedDate(new Date());
        newsDetailRepository.save(newsDetail);
    }

    public static String getHTTPSHTMLContent(HttpsURLConnection connection, boolean gzip) {
        String bufferLine;
        String htmlBuffer = "";
        BufferedReader br;
        try {
            if(gzip)
                br = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream())));
            else
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (Exception e) {
            try {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (Exception e1) {
                System.out.println("Exception....." + e1);
                return "";
            }
        }
        try {
            System.out.println("Response Code :" + connection.getResponseCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (connection.getResponseCode() != 200) {
                System.out.println("Response Code :" + connection.getResponseCode());
            } else {
                while ((bufferLine = br.readLine()) != null) {
                    htmlBuffer = htmlBuffer + bufferLine;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlBuffer;
    }

    public static String jsonValueExtractor(String input,String getString) {
        JSONParser json    = new JSONParser();
        Object obj         = null;
        JSONObject jobject;
        String returnStr   = "";
        if (input == null)
            return returnStr;
        try{
            obj = json.parse(input);
        }catch(Exception e) {
            e.printStackTrace();
        }
        jobject = (JSONObject)obj;
        try {
            if (jobject != null) {
                returnStr = jobject.get(getString).toString().trim();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return returnStr;
    }
    public static List<String> jsonArrayValueExtractor(String input, String getString) {
        JSONParser json    = new JSONParser();
        Object obj         = null;
        String var   	   = "";
        String str;
        List<String> values = new ArrayList<>();
        JSONObject jobject;
        try{
            obj = json.parse(input);
        } catch(Exception e) {
            e.printStackTrace();
        }
        jobject = (JSONObject)obj;
        try{
            if (jobject != null) {
                var = jobject.get(getString).toString().trim();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            obj = json.parse(var);
        }catch(Exception e){
            e.printStackTrace();
        }
        JSONArray jarray = (JSONArray)obj;
        for (int j = 0; j < (jarray != null ? jarray.size() : 0); j++)
        {
            JSONObject obj1    	= (JSONObject) jarray.get(j);
            str	   				= obj1.toString().trim();
            values.add(str);
        }
        return values;
    }
    public static String extractvalues(String Details, String Leftboundary, String Rightboundary, int Count) {
        String[] returntext = new String[999999];
        String returnstring = "";
        try {
            returntext = getstringbettags(Details, Leftboundary, Rightboundary, Count);
            returnstring = returntext[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnstring;
    }
    public static String[] getstringbettags(String text, String start, String end, int cou) {
        String[] returntext = new String[999999];
        int startindx = 0;
        for (int j = 0; j <= cou; j++) {
            startindx = text.indexOf(start, startindx);
            returntext[j] = "";
            if (startindx != -1) {
                text = text.substring(startindx + start.length());
                startindx = start.length();
                int endindx = text.indexOf(end);
                if (endindx != -1) {
                    String temp = text.substring(0, endindx);
                    returntext[j] = temp;
                    startindx += start.length();
                }
            }
        }
        return returntext;
    }
}
