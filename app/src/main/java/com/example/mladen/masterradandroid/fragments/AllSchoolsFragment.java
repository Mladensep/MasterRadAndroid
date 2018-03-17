package com.example.mladen.masterradandroid.fragments;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mladen.masterradandroid.R;
import com.example.mladen.masterradandroid.adapter.DataAdapter;
import com.example.mladen.masterradandroid.database.RealmHelper;
import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.model.SchoolRealmModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AllSchoolsFragment extends Fragment {
    private DataAdapter dataAdapter;
    private RealmHelper realmHelper;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_all_schools, container, false);

        ButterKnife.bind(this, view);

        realmHelper = new RealmHelper(getActivity());

        showAllSchool();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void showAllSchool() {
        List<SchoolRealmModel> result = realmHelper.findAll();
        ArrayList<SchoolModel> schoolModel = new ArrayList<>();

        for(SchoolRealmModel sc : result) {
            SchoolModel model = new SchoolModel();

            model.setNaziv(sc.getNaziv());
            model.setId(sc.getId());
            model.setMesto(sc.getMesto());
            model.setWww(sc.getWww());
            model.setVrsta(sc.getVrsta());
            model.setPbroj(sc.getPbroj());
            model.setAdresa(sc.getAdresa());
            model.setSuprava(sc.getSuprava());
            model.setFax(sc.getFax());
            model.setGps(sc.getGps());
            model.setOdeljenja(sc.getOdeljenja());
            model.setNaziv(sc.getNaziv());
            model.setOkrug(sc.getOkrug());
            model.setOpstina(sc.getOpstina());
            model.setTel(sc.getTel());

            schoolModel.add(model);
        }
        initRecyclerView();

        dataAdapter = new DataAdapter(schoolModel);
        recyclerView.setAdapter(dataAdapter);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(manager);
    }
}