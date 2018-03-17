package com.example.mladen.masterradandroid.database;


import android.app.Activity;
import android.util.Log;

import com.example.mladen.masterradandroid.model.SchoolModel;
import com.example.mladen.masterradandroid.model.SchoolRealmModel;

import java.util.List;

import javax.inject.Inject;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {

    @Inject Realm realmDb;
    private static final String TAG = "myApp";

    public RealmHelper(Activity activity) {
        ((App) activity.getApplication()).getComponent().inject(this);
    }

    public boolean addAllSchool (final List<SchoolModel> scModel) {

        try {
            realmDb.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    for (SchoolModel model : scModel) {
                        String id = model.getId();

                        SchoolRealmModel scRealmModel = realm.createObject(SchoolRealmModel.class, id);

                        scRealmModel.setNaziv(model.getNaziv());
                        scRealmModel.setAdresa(model.getAdresa());
                        scRealmModel.setPbroj(model.getPbroj());
                        scRealmModel.setMesto(model.getMesto());
                        scRealmModel.setOpstina(model.getOpstina());
                        scRealmModel.setOkrug(model.getOkrug());
                        scRealmModel.setSuprava(model.getSuprava());
                        scRealmModel.setWww(model.getWww());
                        scRealmModel.setTel(model.getTel());
                        scRealmModel.setFax(model.getFax());
                        scRealmModel.setVrsta(model.getVrsta());
                        scRealmModel.setOdeljenja(model.getOdeljenja());
                        scRealmModel.setGps(model.getGps());
                    }

                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.v(TAG, "upisano");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {

                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.v(TAG, "greskaa ");

            return false;
        }
    }

    public boolean addSchools(List<SchoolModel> list) {

        try {
            realmDb.beginTransaction();
            realmDb.delete(SchoolRealmModel.class);

            for (SchoolModel schoolModel : list) {

                //String id = schoolModel.getId();
                SchoolRealmModel realmModel = realmDb.createObject(SchoolRealmModel.class);

                realmModel.setId(schoolModel.getId());
                realmModel.setMesto(schoolModel.getMesto());
                realmModel.setWww(schoolModel.getWww());
                realmModel.setVrsta(schoolModel.getVrsta());
                realmModel.setPbroj(schoolModel.getPbroj());
                realmModel.setAdresa(schoolModel.getAdresa());
                realmModel.setSuprava(schoolModel.getSuprava());
                realmModel.setFax(schoolModel.getFax());
                realmModel.setGps(schoolModel.getGps());
                realmModel.setOdeljenja(schoolModel.getOdeljenja());
                realmModel.setNaziv(schoolModel.getNaziv());
                realmModel.setOkrug(schoolModel.getOkrug());
                realmModel.setOpstina(schoolModel.getOpstina());
                realmModel.setTel(schoolModel.getTel());
            }
            realmDb.commitTransaction();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public RealmResults<SchoolRealmModel> findAll() {
        RealmResults<SchoolRealmModel> result =  realmDb.where(SchoolRealmModel.class).findAll();

        return result;
    }

    public RealmResults<SchoolRealmModel> filter(SchoolRealmModel scModel) {

        RealmResults<SchoolRealmModel> result = realmDb.where(SchoolRealmModel.class)
            .beginsWith("naziv", scModel.getNaziv(), Case.INSENSITIVE)
            .beginsWith("mesto", scModel.getMesto())
            .contains("vrsta", scModel.getVrsta())
            .findAll();

        return result;
    }
}
