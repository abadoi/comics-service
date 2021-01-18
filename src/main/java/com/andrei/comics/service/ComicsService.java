package com.codecool.comics.service;

import com.codecool.comics.model.Comic;

import java.util.List;

public interface ComicsService {

    int getXkcdLastComic();
    Comic getXkcdComicById(int id);
    List<Comic> getPDLComics();

}
