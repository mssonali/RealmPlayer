package com.sonali.myrealmdatabase.Controller;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class Controller extends Application {

    //public static RealmConfiguration realmConfig;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("myplayers.realm")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
