package com.example.mladen.masterradandroid.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mladen.masterradandroid.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
    }

    @OnClick(R.id.back_icon)
    public void back() {
        finish();
    }
}
