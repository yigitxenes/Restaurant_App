package com.example.restaurantApp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    // build.gradle'dan gelen IP adresini burada kullanıyoruz
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // API Servisini almak için kısayol metodu
    public static RestaurantApiService getApiService() {
        return getClient().create(RestaurantApiService.class);
    }
}