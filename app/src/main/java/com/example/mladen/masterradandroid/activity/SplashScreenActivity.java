package com.example.mladen.masterradandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.database.RealmHelper;
import com.example.mladen.masterradandroid.fragments.AllSchoolsFragment;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.model.SchoolRealmModel;
import com.example.mladen.masterradandroid.retrofit.RestApi;
import com.example.mladen.masterradandroid.retrofit.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    //private CompositeDisposable compositeDisposable;
    private final int SPLASH_DISPLAY_LENGTH = 3;
    private RealmHelper realmHelper;
    private RestApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        realmHelper = new RealmHelper(this);
        List<SchoolRealmModel> result = realmHelper.findAll();

        SharedPreferences sharedPref = this.getSharedPreferences("facebook", Context.MODE_PRIVATE);
        String mailfb = sharedPref.getString("emailData", "");
        String tokenfb = sharedPref.getString("tokenData", "");

        //rx
//        if(result.size() == 0) {
//
//            apiService = RestClient.test();
//            compositeDisposable = new CompositeDisposable();
//
//            initRecyclerView();
//
//            getSchoolData();
//        }

        if(result.size() == 0) {
            apiService = RestClient.getClient().create(RestApi.class);
            getSchoolData();
        }

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                if(tokenfb == "") {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                } else {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, HomeTabsActivity.class);
                    SplashScreenActivity.this.startActivity(mainIntent);
                    SplashScreenActivity.this.finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void getSchoolData() {
        Call<List<SchoolModel>> call = apiService.getAllSchoolData();
        call.enqueue(new Callback<List<SchoolModel>>() {

            @Override
            public void onResponse(Call<List<SchoolModel>> call, Response<List<SchoolModel>> response) {

                final ArrayList<SchoolModel> list = (ArrayList<SchoolModel>) response.body();

                realmHelper.addSchools(list);
                Toast.makeText(SplashScreenActivity.this, "Добављене и смештене школе", Toast.LENGTH_SHORT).show();

//                initRecyclerView();
//
//                dataAdapter = new DataAdapter(list);
//                recyclerView.setAdapter(dataAdapter);
            }
            @Override
            public void onFailure(Call<List<SchoolModel>> call, Throwable t) {
                Toast.makeText(SplashScreenActivity.this, "Грешка са сервиса", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //rx
//    private void getSchoolData() {
//        compositeDisposable.add(apiService.getAllSchoolData()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::handleResponse,this::handleError));
//    }
//
//
//    private void handleResponse(List<SchoolModel> androidList) {
//
//        mAndroidArrayList = new ArrayList<>(androidList);
//
//        realmHelper.addSchools(mAndroidArrayList);
//        Toast.makeText(AllSchoolsFragment.this, "Added to realm", Toast.LENGTH_SHORT).show();
//
//        dataAdapter = new DataAdapter(mAndroidArrayList);
//        recyclerView.setAdapter(dataAdapter);
//    }
//
//    private void handleError(Throwable error) {
//
//        Toast.makeText(this, "Error "+error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//    }
}
