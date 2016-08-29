package com.test.book_barn.interfaces;


import com.test.book_barn.googleApiModels.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by audreyeso on 8/9/16.
 */
public interface GoogleBooksAPI {

    @GET("volumes")
    Call<Example> getBookDescription(@Query("q") String ISBN);
}
