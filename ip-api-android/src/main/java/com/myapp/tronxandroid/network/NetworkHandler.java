package com.myapp.tronxandroid.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by smartron on 1/11/17.
 */

/**
 * Handles network instances
 */
public class NetworkHandler {
    private static final NetworkHandler ourInstance = new NetworkHandler();
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            okhttp3.Response response = chain.proceed(request);
            if (response.isSuccessful())
                return response;
            switch (response.code()) {
                case 400:
                    break;
            }
            return response;
        }

    };

    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(90000, TimeUnit.MILLISECONDS)
            .connectTimeout(90000, TimeUnit.MILLISECONDS)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(interceptor)
            .build();

    private Retrofit customerRetrofit = new Retrofit.Builder()
            .baseUrl(IpApi.REST_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(StringConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private IpApi ipApi = customerRetrofit.create(IpApi.class);

    private NetworkHandler() {
    }

    public static NetworkHandler instance() {
        return ourInstance;
    }

    public IpApi customerApi() {
        return ipApi;
    }
}
