package com.andrei.comics.service;

import com.andrei.comics.model.Comic;

import java.util.List;

public interface ComicsService {

    int getXkcdLastComic();
    Comic getXkcdComicById(int id);
    List<Comic> getPDLComics();

}
