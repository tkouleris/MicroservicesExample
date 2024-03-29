package io.tkouleris.ratingsservice.controller;

import io.tkouleris.ratingsservice.dto.request.RatingDto;
import io.tkouleris.ratingsservice.dto.response.ApiResponse;
import io.tkouleris.ratingsservice.entity.Rating;
import io.tkouleris.ratingsservice.entity.User;
import io.tkouleris.ratingsservice.exception.NotFoundException;
import io.tkouleris.ratingsservice.repository.IRatingRepository;
import io.tkouleris.ratingsservice.service.LoggedUserService;
import io.tkouleris.ratingsservice.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/ratings")
public class RatingsController {

    @Autowired
    private IRatingRepository ratingRepository;

    @Autowired
    private RatingService ratingService;

    @GetMapping(path="/all", produces = "application/json")
    public ResponseEntity<Object> getAll(){
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User loggedInUser = loggedUserService.getLoggedInUser();
        if(loggedInUser == null){
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Ratings");
            return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
        }
        List<Rating> ratings = ratingRepository.findRatingByUser((long)loggedInUser.getId());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(ratings);
        apiResponse.setMessage("Ratings");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

    @PostMapping(path="/rate", produces = "application/json")
    public ResponseEntity<Object> store(@RequestBody RatingDto ratingDto) throws NotFoundException {
        User loggedInUser = LoggedUserService.getInstance().getLoggedInUser();
        Rating rating = this.ratingService.createOrUpdate(loggedInUser,ratingDto);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(rating);
        apiResponse.setMessage("rating");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

    @GetMapping(path="/movie/{movie_id}", produces = "application/json")
    public ResponseEntity<Object> getMovieRating(@PathVariable("movie_id") long movie_id){
        LoggedUserService loggedUserService = LoggedUserService.getInstance();
        User loggedInUser = loggedUserService.getLoggedInUser();
        if(loggedInUser == null){
            System.out.println("============================= ERROR =================================");
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("Ratings");
            return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
        }
        Rating rating = ratingRepository.findRatingByUserAndMovie((long)loggedInUser.getId(),movie_id).orElse(null);
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(ratings);
        apiResponse.setMessage("Movie Rating");
        return new ResponseEntity<>(apiResponse.getBodyResponse(), HttpStatus.OK);
    }

}
