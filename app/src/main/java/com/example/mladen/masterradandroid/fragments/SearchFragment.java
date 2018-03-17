package com.example.mladen.masterradandroid.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.model.SearchModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFragment extends Fragment {
    @BindView(R.id.edit_text_naziv) EditText editNaziv;
    @BindView(R.id.edit_text_mesto) EditText editMesto;
    @BindView(R.id.samo_osnovne_id) CheckBox samoOsnovne;
    @BindView(R.id.samo_srednje_id) CheckBox samoSrednje;
    @BindView(R.id.submit_button) Button button;

    private int osnovne;
    private int srednje;
    private boolean isOsnovne;
    private boolean isSrednje;

    private SearchModel searchModel = new SearchModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.back_icon)
    public void back() {
        getActivity().finish();
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

        SearchResultFragment fragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("dataa", searchModel);
        fragment.setArguments(bundle);
    }
}
