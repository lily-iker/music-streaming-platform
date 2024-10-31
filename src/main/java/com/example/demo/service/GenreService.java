package com.example.demo.service;

import com.example.demo.dto.response.SearchGenreResponse;

import java.util.List;

public interface GenreService {
    List<SearchGenreResponse> getAllGenres();
}
