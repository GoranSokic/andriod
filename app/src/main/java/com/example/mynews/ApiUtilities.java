package com.example.mynews;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiUtilities {

    private static Retrofit retrofit = null;

    public static ApiInterface getApiInterface(){

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request requestWithUserAgent = originalRequest.newBuilder()
                            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")
                            .build();
                    return chain.proceed(requestWithUserAgent);
                })
                .build();

        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(ApiInterface.BASE_URL).addConverterFactory(GsonConverterFactory.create())
                    .client(client).build();
        }
        return retrofit.create(ApiInterface.class);

    }

}
