package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.rating.RatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@Slf4j
@Validated
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @GetMapping
    public Collection<Rating> findAll() {
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public Rating getGenreById(@PathVariable("id") long ratingId) {
        return ratingService.getRating(ratingId);
    }
}
