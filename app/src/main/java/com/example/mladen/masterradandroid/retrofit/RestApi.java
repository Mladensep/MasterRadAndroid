package com.example.mladen.masterradandroid.retrofit;


import com.example.mladen.masterradandroid.model.CommentModel;
import com.example.mladen.masterradandroid.model.RecensionModel;
import com.example.mladen.masterradandroid.model.SchoolModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {
    //@GET("/get.php?dataset=skole&lang=sr&term=json")
    @GET("/Api/Schools")
    Call<List<SchoolModel>> getAllSchoolData();

    @POST("/Api/Comment") //radi slanje komentara
    Call<String> postOrder(@Body CommentModel sendComment);

    @GET("/Api/Comment/")
    Call<List<CommentModel>> getAllComment(@Query("id") int id);
    //Observable<List<CommentModel>> getAllComment(@Query("id") int id);

    @POST("/Api/Recension") //radi slanje recenzija
    Call<Void> postRecension(@Body RecensionModel recensionModel);

    @GET("/Api/Recension/")
    Call<List<RecensionModel>> getAllRecension(@Query("schoolId") int school_id);
}
