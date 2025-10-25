package com.midterm.dogapp.viewmodel;

import com.midterm.dogapp.model.DogBreed;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface DogApi {
    @GET("DevTides/DogsApi/master/dogs.json")
    Single<List<DogBreed>> getDogs();
}
