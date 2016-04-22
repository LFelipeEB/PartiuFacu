package com.partiufacu.kronaproject.partiufacu.objetos;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.partiufacu.kronaproject.partiufacu.interfaces.Constantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lfelipeeb on 10/02/16.
 */
public class Gerenciamento {

    public String nomeGerencia, diaGerencia, horaGerencia,precoGerencia, nomeUserGerencia, saidaGerencia, chegadaGerencia ;
    public int idUser, idCarona, vagasGerencia;
    private Context ctx;

    public Gerenciamento(String nome, String dia, String horario) {
        this.nomeGerencia = nome;
        this.diaGerencia = dia;
        this.horaGerencia = horario;
    }

    public Gerenciamento(Context ctx) {
        this.ctx = ctx;
    }

    public List<Gerenciamento> fromWebService() {
        final List<Gerenciamento> list = new ArrayList<>();
        Map<String, String> parms = new HashMap<>();
        parms.put("id", Integer.toString(idUser));
        parms.put("opc", Integer.toString(5));
        StringRequest sRequest = new StringRequest(Request.Method.POST, Constantes.Gerencia, new Response.Listener<String>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("Gerenca", response);

                    JSONArray array2 = new JSONArray(response);
                    for (int i = 0; i < array2.length(); i++) {
                        JSONObject obj = array2.getJSONArray(i).getJSONObject(0);
                        Log.e("Gerenca", " " +i);
                        Log.e("Gerenca",obj.toString());
                        Gerenciamento g = new Gerenciamento(obj.getString("nome_carona"), obj.getString("data"), obj.getString("horario"));
                        g.vagasGerencia = obj.getInt("vagas");
                        g.precoGerencia = obj.getString("preco");
                        g.nomeUserGerencia = obj.getString("nome");
                        g.saidaGerencia = obj.getString("local_saida");
                        g.chegadaGerencia = obj.getString("local_chegada");
                        g.idCarona = obj.getInt("cod_carona");
                        g.toString();
                        list.add(g);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Gerenca", error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getParams() {
                    Map<String, String> parms = new HashMap<>();
                    parms.put("id", Integer.toString(1));
                    return parms;
                }
            };
            RequestQueue qr = Volley.newRequestQueue(ctx);
            qr.add(sRequest);
            return list;
        }

                ;
    }
