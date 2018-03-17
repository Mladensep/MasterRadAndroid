package com.example.mladen.masterradandroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.adapter.DataAdapter;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class Test extends AppCompatActivity {

    @BindView(R.id.naziv) TextView button;
    @BindView(R.id.mesto) TextView mesto;
    @BindView(R.id.opstina) TextView opstina;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

//        Button button = (Button) findViewById(R.id.button);
//        RxView.clicks(button)
//                .subscribe(aVoid -> {
//                    //Perform some work here//
//                });


        RxView.clicks(button)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println("Click");
                        // startActivity(new Intent(SelectionActivity.this, NetworkingActivity.class));

                        Toast.makeText(getApplicationContext(), "radi", Toast.LENGTH_SHORT).show();
                    }

                });
    }
}
