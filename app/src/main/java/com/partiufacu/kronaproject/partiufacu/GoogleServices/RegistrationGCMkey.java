package com.partiufacu.kronaproject.partiufacu.GoogleServices;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.iid.InstanceID;
import com.partiufacu.kronaproject.partiufacu.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe responsavel por gerar a Key do usuario pelo google além de enviar ela quando gerado para o nosso servidor.
 *
 * Created by evari on 01/04/2016.
 */
public class RegistrationGCMkey extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public RegistrationGCMkey() {
        super("PARTIU FACU");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized ("PARTIUFACU"){
            InstanceID id = InstanceID.getInstance(this);
            try {
                String key = id.getToken(getString(R.string.sender_id), null);
                enviaKey(key, intent.getExtras().getString("id"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Metodo envia a key e quem é o responsavel por esta key para o servidor.
     * @param key - Token gerado pelo Google que identifica este celular e meu aplicativo
     * @param id - id do usuario PartiuFacu
     */
    private void enviaKey(final String key, final String id) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://partiufacu.com/AppFunctions/gcmService.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("SendRegistration", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("opc", "1");
                params.put("user",id);
                params.put("api",key);

                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
}
