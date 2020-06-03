package com.example.frame;


import com.example.data.BaseInfo;
import com.example.data.MainAdEntity;
import com.example.data.SpecialtyChooseEntity;
import com.example.data.TestInfo;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface IService {
    @GET("/")
    Observable<TestInfo> getTestData(@QueryMap Map<String, Object> params, @Query("page_id") int pageId);

    @GET("ad/getAd")
    Observable<BaseInfo<MainAdEntity>> getAdvert(@QueryMap Map<String, Object> pMap);

    @GET("lesson/getAllspecialty")
    Observable<BaseInfo<List<SpecialtyChooseEntity>>> getSubjectList();
}
