package com.andrei.comics.controller;

import com.andrei.comics.model.Comic;
import com.andrei.comics.service.ComicsService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
public class ComicsRestController {

    @Autowired
    private ComicsService xkcdComicsAPIService;

    @GetMapping("/")
    public String getNumComics()
    {
        List<Comic> results = new ArrayList<>();

        // get last 10 comics from Xkcd
        int lastComicId = xkcdComicsAPIService.getXkcdLastComic();
        int i = 10;
        while (lastComicId > 0 && i > 0) {
            results.add(xkcdComicsAPIService.getXkcdComicById(lastComicId--));
            i--;
        }

        // get last 10 comics from Pdl RSS feed
        List<Comic> pdlComics = xkcdComicsAPIService.getPDLComics();
        results.addAll(pdlComics);

        // sort in descending order by year
        Collections.sort(results, new Comparator<Comic>(){
            public int compare(Comic c1, Comic c2) {
                return  (int) ( c2.getYear() - c1.getYear() );
            }
        });

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(results);

        return json;
    }

}
