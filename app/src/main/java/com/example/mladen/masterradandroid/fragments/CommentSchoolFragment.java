package com.example.mladen.masterradandroid.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.adapter.CommentAdapter;
import com.example.mladen.masterradandroid.model.CommentModel;
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
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CommentSchoolFragment extends Fragment {

    @BindView(R.id.editText) EditText editText;
    @BindView(R.id.list) ListView listView;
    @BindView(R.id.submitButton) Button submitButton;

    private String getComment;
    private int id;
    private CommentModel commentModel;
    private RestApi apiService;
    private CompositeDisposable compositeDisposable;
    private ArrayList<CommentModel> list;
    private CommentAdapter adapter;
    private ConnectivityManager conMgr;

    private String mailfb;
    private String namefb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_school, container, false);

        ButterKnife.bind(this, view);

        Bundle bundle = getActivity().getIntent().getExtras();
        SchoolModel sc = bundle.getParcelable("data");

        SharedPreferences sharedPref = getActivity().getSharedPreferences("facebook", Context.MODE_PRIVATE);

        mailfb = sharedPref.getString("emailData", "");
        namefb = sharedPref.getString("nameData", "");

        commentModel = new CommentModel();

        id = Integer.parseInt(sc.getId());

        conMgr = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

                //rx
            //apiService = RestClient.test();
            //compositeDisposable = new CompositeDisposable();
            apiService = RestClient.getClient().create(RestApi.class);
            getCommentData();

        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            Toast.makeText(getActivity(), "Нема интернет конекције. " , Toast.LENGTH_SHORT).show();
        }

        if(mailfb != "") {
            submitButton.setBackgroundColor(getResources().getColor(R.color.tab_background));
            submitButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            submitButton.setBackgroundColor(getResources().getColor(R.color.disable_button));
            submitButton.setTextColor(getResources().getColor(R.color.light_gray2));
        }

        return view;
    }


        private void getCommentData() { //bez rxjava2
        Call<List<CommentModel>> call = apiService.getAllComment(id);
        call.enqueue(new Callback<List<CommentModel>>() {
            @Override
            public void onResponse(Call<List<CommentModel>> call, Response<List<CommentModel>> response) {
                if(response.isSuccessful()) {
                    List<CommentModel> lista = response.body();

                    int xa = lista.size()*137;
                    listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, xa));

                    CommentAdapter adapter = new CommentAdapter(getActivity(), lista);
                    listView.setAdapter(adapter);

                    //int c = lista.size();
                    //System.out.println(c);
                }
            }

            @Override
            public void onFailure(Call<List<CommentModel>> call, Throwable t) {

            }
        });
    }

    //sa rx
//    private void getCommentData() {
//        compositeDisposable.add(apiService.getAllComment(id)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::handleResponse, this::handleEror));
//    }
//
//    private void handleResponse(List<CommentModel> commentModels) {
//        int xa = commentModels.size()*137;
//        listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, xa));
//
//        list = new ArrayList<>(commentModels);
//        adapter = new CommentAdapter(getActivity(), list);
//        listView.setAdapter(adapter);
//
//        Toast.makeText(getActivity(), "Добављени коментари. " , Toast.LENGTH_SHORT).show();
//    }
//
//    private void handleEror(Throwable error) {
//        Toast.makeText(getActivity(), "Коментари нису добављени. ", Toast.LENGTH_SHORT).show();
//    }

    @OnClick(R.id.submitButton) //bez rxjava2
    public void sendComment() {
        if(mailfb != "") {

            getComment = editText.getText().toString();
            String finalComment = namefb + ": " +getComment;

            commentModel.setCom(finalComment);
            commentModel.setSchool_id(id);

            Call<String> call = apiService.postOrder(commentModel);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Успешно посалто", Toast.LENGTH_SHORT).show();

                        getCommentData();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getActivity(), "Није послато", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "Морате бити улоговани", Toast.LENGTH_SHORT).show();
        }
    }









    //sa rx2
//    @OnClick(R.id.submitButton)
//    public void sendComment() {
//        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
//                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
//
//            getComment = editText.getText().toString();
//
//            commentModel.setCom(getComment);
//            commentModel.setSchool_id(id);
//
//            compositeDisposable.add(apiService.postOrder(commentModel)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(this::handleResponse, this::handleEror2));
//
//        }
//        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
//                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
//
//            Toast.makeText(getActivity(), "Нема интернет конекције. " , Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void handleResponse(Void aVoid) {
//        Toast.makeText(getActivity(), "Успешно послато", Toast.LENGTH_SHORT).show();
//    }
//
//    private void handleEror2(Throwable error) {
//        Toast.makeText(getActivity(), "Није послато ", Toast.LENGTH_SHORT).show();
//    }







    //sa rx
//    @OnClick(R.id.submitButton)
//    public void sendComment() {
//        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
//                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
//
//            getComment = editText.getText().toString();
//
//            commentModel.setCom(getComment);
//            commentModel.setSchool_id(id);
//
//            compositeDisposable.add(apiService.postOrder(commentModel)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//
//                    .subscribeWith(new DisposableObserver<String>() {
//
//                        @Override
//                        public void onNext(String value) {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Toast.makeText(getActivity(), "nije Успешно послато", Toast.LENGTH_SHORT).show();
//
//                            e.printStackTrace();
//
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            Toast.makeText(getActivity(), "Успешно послато", Toast.LENGTH_SHORT).show();
//
//                            getCommentData();
//                        }
//                    }));
//
//        }
//        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
//                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
//
//            Toast.makeText(getActivity(), "Нема интернет конекције. " , Toast.LENGTH_SHORT).show();
//        }
//    }
}
