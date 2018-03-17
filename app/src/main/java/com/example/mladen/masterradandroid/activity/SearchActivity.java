package com.example.mladen.masterradandroid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.database.App;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.model.SearchModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.edit_text_naziv) EditText editNaziv;
    @BindView(R.id.edit_text_mesto) EditText editMesto;
    @BindView(R.id.samo_osnovne_id) CheckBox samoOsnovne;
    @BindView(R.id.samo_srednje_id) CheckBox samoSrednje;
    @BindView(R.id.submit_button) Button button;

    @BindView(R.id.txt1) TextView textView;
    @BindView(R.id.txt2) TextView textView2;

    private int osnovne;
    private int srednje;
    private boolean isOsnovne;
    private boolean isSrednje;

    @Inject SearchModel searchModel;
    //SearchModel searchModel = new SearchModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        ((App) getApplicationContext()).getComponent().inject(this);

        ButterKnife.bind(this);

        SharedPreferences sharedPref = this.getSharedPreferences("facebook", Context.MODE_PRIVATE);
        String highScore = sharedPref.getString("emailData", "");
        String highScore2 = sharedPref.getString("tokenData", "");
        //long highScore3 = sharedPref5.getInt("edit3", 7);

        textView.setText(highScore);
        textView2.setText(highScore2);
    }


    @OnClick(R.id.back_icon)
    public void back() {
        finish();
    }

    @OnClick(R.id.submit_button)
    public void search() {
        osnovne = 0;
        srednje = 0;
        String naziv = editNaziv.getText().toString();
        String mesto = editMesto.getText().toString();

        isOsnovne = samoOsnovne.isChecked();
        if(isOsnovne)
            osnovne = 1;

        isSrednje = samoSrednje.isChecked();
        if (isSrednje)
            srednje = 1;

        searchModel.setNaziv(naziv);
        searchModel.setMesto(mesto);
        searchModel.setSamoOsnovne(osnovne);
        searchModel.setSamoSrednje(srednje);

//        SearchResultFragment fragment = new SearchResultFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("dataa", searchModel);
//        fragment.setArguments(bundle);

        //Utils.replaceFragment(this, fragment, "test");

        Intent intent = new Intent(this, SearchResultTabActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("dataa", searchModel);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }
}
