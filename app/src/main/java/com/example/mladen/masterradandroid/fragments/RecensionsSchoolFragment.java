package com.example.mladen.masterradandroid.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.model.RecensionModel;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.retrofit.RestApi;
import com.example.mladen.masterradandroid.retrofit.RestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecensionsSchoolFragment extends Fragment {

    //private CompositeDisposable compositeDisposable;

    @BindView(R.id.ratingBarPost) RatingBar ratingBar;
    @BindView(R.id.ratingBarGet) RatingBar ratingGet;
    @BindView(R.id.sendRecension) Button submitButton;
    private float recension;
    private SchoolModel sc;
    private RecensionModel recensionModel;
    private RestApi apiService;
    private int school_id;
    private ArrayList<RecensionModel> lista;

    private String mailfb;
    private String namefb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recensions_school, container, false);

        ButterKnife.bind(this, view);

        //compositeDisposable = new CompositeDisposable();

        Bundle bundle = getActivity().getIntent().getExtras();
        sc = bundle.getParcelable("data");
        school_id = Integer.parseInt(sc.getId());

        SharedPreferences sharedPref = getActivity().getSharedPreferences("facebook", Context.MODE_PRIVATE);

        mailfb = sharedPref.getString("emailData", "");
        namefb = sharedPref.getString("nameData", "");

        recensionModel = new RecensionModel();


        apiService = RestClient.getClient().create(RestApi.class);
        //rx
        //apiService = RestClient.test();


        getRecensionData();

        if(mailfb != "") {
            submitButton.setBackgroundColor(getResources().getColor(R.color.tab_background));
            submitButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            submitButton.setBackgroundColor(getResources().getColor(R.color.disable_button));
            submitButton.setTextColor(getResources().getColor(R.color.light_gray2));
        }

        return view;
    }

    private void getRecensionData() {
        Call<List<RecensionModel>> call = apiService.getAllRecension(school_id);
        call.enqueue(new Callback<List<RecensionModel>>() {
            @Override
            public void onResponse(Call<List<RecensionModel>> call, Response<List<RecensionModel>> response) {
                if(response.isSuccessful()) {
                    List<RecensionModel> lista = response.body();

                    int size = lista.size();

                    double xa = 0;
                    for(int i = 0; i<size; i++) {

                         xa += lista.get(i).getRec();
                    }

                    double count = xa/size;
                    ratingGet.setRating((float) count);

                    Toast.makeText(getActivity(), "Добављене рецензије. " , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecensionModel>> call, Throwable t) {

            }
        });
    }

//    private void getRecensionData() {
//        compositeDisposable.add(apiService.getAllRecension(school_id)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::handleResponse, this::handleEror));
//    }
//
//    private void handleResponse(List<RecensionModel> recensionModels) {
//        lista = new ArrayList<>(recensionModels);
//
//
//        int size = lista.size();
//
//        double xa = 0;
//        for (int i = 0; i < size; i++) {
//
//            xa += lista.get(i).getRec();
//        }
//
//        double count = xa / size;
//        ratingGet.setRating((float) count);
//
//        Toast.makeText(getActivity(), "Добављене рецензије. " , Toast.LENGTH_SHORT).show();
//    }
//
//    private void handleEror(Throwable error) {
//        Toast.makeText(getActivity(), "Рецензије нису добављене. ", Toast.LENGTH_SHORT).show();
//    }


    @OnClick(R.id.sendRecension)
    public void send() {
        if(mailfb != "") {
            recension = ratingBar.getRating();

            recensionModel.setRec(recension);
            recensionModel.setSchool_id(school_id);

            Call<Void> call = apiService.postRecension(recensionModel);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(getActivity(), "Успешно посалто", Toast.LENGTH_SHORT).show();
                    getRecensionData();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getActivity(), "Није послато", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Морате бити улоговани", Toast.LENGTH_SHORT).show();
        }
    }


//    @OnClick(R.id.sendRecension)
//    public void send() {
//        recension = ratingBar.getRating();
//
//        recensionModel.setRec(recension);
//        recensionModel.setSchool_id(school_id);
//
//        compositeDisposable.add(apiService.postRecension(recensionModel)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::handleResponse, this::handleEror2));
//    }
//
//    private void handleResponse(Void aVoid) {
//        Toast.makeText(getActivity(), "Успешно послато", Toast.LENGTH_SHORT).show();
//    }
//
//    private void handleEror2(Throwable throwable) {
//        Toast.makeText(getActivity(), "Није послато", Toast.LENGTH_SHORT).show();
//    }
}