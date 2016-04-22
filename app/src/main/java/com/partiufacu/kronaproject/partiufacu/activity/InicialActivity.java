package com.partiufacu.kronaproject.partiufacu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.partiufacu.kronaproject.partiufacu.GoogleServices.RegistrationGCMkey;
import com.partiufacu.kronaproject.partiufacu.R;

public class InicialActivity extends BaseActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        // muda a activity depois de 1 segundo
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // os dados do login salva no SharedPreferences
                if (verificaGoogleService()) {
                    finish();
                }
                Log.i("PartiuFacu", "Google Play Service INSTALADO, TUDO OK!!");

                SharedPreferences sharedPreferences = getSharedPreferences("Cadastro", MODE_PRIVATE);
                String nome = sharedPreferences.getString("nome", null);

                if (nome == null || nome.equals("")) {
                    startActivity(new Intent(InicialActivity.this, LoginActivity.class));
                } else {
                    //Verifica se o Key do aparelho já foi enviado.
                    if(! sharedPreferences.getBoolean("keyStatus", false)){
                        Intent it = new Intent(getApplicationContext(), RegistrationGCMkey.class);
                        Bundle b = new Bundle();
                        b.putString("id", sharedPreferences.getString("id", "1"));
                        sharedPreferences.edit().putBoolean("keyStatus", true).commit();
                        Log.i("PartiuFacu", "Chave geranda com sucesso");
                        it.putExtras(b);
                        startService(it);
                    }
                    // tela de opcoes
                    startActivity(new Intent(InicialActivity.this, MainActivity.class));
                }
                finish();
            }
        }, 1500);
    }

    /**
     * Este metodo verifica se a google play esta instalado no aparelho.
     *
     * @return true - Caso não exista nenhuma google Service instalado, ou
     *         false -Caso a google play service esteja acessivel.
     */
    private boolean verificaGoogleService() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (result !=ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(result)){
                GooglePlayServicesUtil.getErrorDialog(result,this, 9000).show();
                return true;
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Este aplicativo necessita do Google Play Service, por favor instale ou atualize!");
                builder.setTitle("Não podemos continuar ...");
                builder.setPositiveButton("Ok, vou instalar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
                return true;

            }
        }else {
            return false;
        }
    }
}
