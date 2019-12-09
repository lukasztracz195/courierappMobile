package com.project.courierapp.model.di.modules;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.project.courierapp.model.store.TokenStore;

import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

//  NAGROK ALOW CREATE ON 8H SERVER PROXY FOR YOUR LOCAL SERVER AND APPLICATION
//  REQUEST WAY   ANDROID APP -> INTERNET ( NAGROK PROXY SERVER ) -> LOCAL SERVER
//  ON COMMAND nagrok http 8080 COPY URL WITH HTTPS AND PAST URL BELOW

    //  BASE URL TO APPLICATION ON HEROKU
    private static final String BASE_URL = "https://ancient-sierra-85534.herokuapp.com";

    @Singleton
    @Provides
    public HttpLoggingInterceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Named("auth")
    @Singleton
    @Provides
    public OkHttpClient authClient(HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);
        builder.readTimeout(1, TimeUnit.MINUTES);
        builder.addInterceptor(chain -> {
            okhttp3.Request request = chain.request();
            String authToken = TokenStore.getToken();
            Headers headers = request.headers().newBuilder().
                    add("Authorization", "Bearer " + authToken)
                    .build();
            request = request.newBuilder().headers(headers).build();
            return chain.proceed(request);
        });
        return builder.build();
    }

    @Named("no_auth")
    @Singleton
    @Provides
    public OkHttpClient client(HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(loggingInterceptor);
        builder.readTimeout(1, TimeUnit.MINUTES);
        return builder.build();
    }


    @Named("auth")
    @Singleton
    @Provides
    public Retrofit authRetrofit(@Named("auth") OkHttpClient client) {
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                        LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(),
                                ISODateTimeFormat.dateTimeParser())).create();

        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Named("no_auth")
    @Singleton
    @Provides
    public Retrofit retrofit(@Named("no_auth") OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }
}
