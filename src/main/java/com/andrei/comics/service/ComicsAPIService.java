package com.codecool.comics.service;

import com.codecool.comics.Utils;
import com.codecool.comics.model.Comic;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComicsAPIService implements ComicsService {

    @Override
    public int getXkcdLastComic(){
        final String URI = "https://xkcd.com/info.0.json";

        RestTemplate restTemplate = new RestTemplate();
        String result;

        try {
            result = restTemplate.getForObject(URI, String.class);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
            return -1;
        }

        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();

        return jsonObject.get("num").getAsInt();

    }

    @Override
    public Comic getXkcdComicById(int id) {
        final String URI = "https://xkcd.com/" + id + "/info.0.json";

        RestTemplate restTemplate = new RestTemplate();
        Comic result = new Comic(URI);

        try {
            result = restTemplate.getForObject(URI, Comic.class);
        } catch (RestClientException e) {
            System.out.println(e.getMessage());
        }

        return result;

    }


    @Override
    public List<Comic> getPDLComics() {

        List<Comic> results = new ArrayList<>();
        final String URI = "http://feeds.feedburner.com/PoorlyDrawnLines";
        String xmlString = Utils.readUrlToString(URI);
        JSONObject xmlJSONObj = XML.toJSONObject(xmlString);
        JSONArray items = xmlJSONObj.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");

        for(int n = 0; n < items.length() && n < 10; n++)
        {
            JSONObject object = items.getJSONObject(n);
            String webUrl = object.getJSONObject("guid").get("content").toString();
            String title = object.get("title").toString();

            String pubDate = object.get("pubDate").toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss +0000");
            LocalDate date =  LocalDate.parse(pubDate, formatter);
            int year = date.getYear();


            String htmlContent = object.get("content:encoded").toString();
            Document doc = Jsoup.parse(htmlContent);
            Elements link = doc.select("a");
            String img = link.first().attr("href");

            Comic newComic = new Comic(webUrl);
            newComic.setImg(img);
            newComic.setTitle(title);
            newComic.setYear(year);
            results.add(newComic);

        }

        return results;
    }

}
