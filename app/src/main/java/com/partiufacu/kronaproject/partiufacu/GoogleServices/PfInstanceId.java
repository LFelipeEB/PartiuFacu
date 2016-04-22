package com.partiufacu.kronaproject.partiufacu.GoogleServices;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by evari on 01/04/2016.
 */
public class PfInstanceId extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        SharedPreferences shared = this.getSharedPreferences("Cadastro", MODE_PRIVATE);
        shared.edit().putBoolean("keyOnServe", false).apply();

        Intent it = new Intent(this, RegistrationGCMkey.class);
        Bundle b = new Bundle();
        b.putString("id", shared.getString("id","1"));
        it.putExtras(b);
        startService(it);
    }
}
