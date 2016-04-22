package com.partiufacu.kronaproject.partiufacu.fragment;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.activity.DetalhesCaronaActivity;
import com.partiufacu.kronaproject.partiufacu.adapter.GerenciaAdapter;
import com.partiufacu.kronaproject.partiufacu.interfaces.Constantes;
import com.partiufacu.kronaproject.partiufacu.objetos.Gerenciamento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class GerenciaFragment extends Fragment implements GerenciaAdapter.OnClickGerencia {

    private RecyclerView rv;
    private List<Gerenciamento> lista;

    public GerenciaFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ProgressDialog dp = new ProgressDialog(getContext());
        dp.setMessage("Buscando ...");
        dp.show();

        final View layout = inflater.inflate(R.layout.fragment_gerencia, container, false);

        lista = new ArrayList<>();
        final GerenciaAdapter adapter = new GerenciaAdapter(lista, getContext(), this);

        StringRequest sRequest = new StringRequest(Request.Method.POST, Constantes.Gerencia, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    if((! response.isEmpty()) || !response.equals("Não há regristos!") ){
                        Log.i("Gerencia", response);
                        JSONArray array2 = new JSONArray(response);
                        for (int i = 0; i < array2.length(); i++) {
                            JSONObject obj = array2.getJSONArray(i).getJSONObject(0);
                            Log.e("Gerenca", " " + i);
                            Log.e("Gerenca", obj.toString());
                            Gerenciamento g = new Gerenciamento(obj.getString("nome_carona"), obj.getString("data"), obj.getString("horario"));
                            g.vagasGerencia = obj.getInt("vagas");
                            g.precoGerencia = obj.getString("preco");
                            g.nomeUserGerencia = obj.getString("nome");
                            g.saidaGerencia = obj.getString("local_saida");
                            g.chegadaGerencia = obj.getString("local_chegada");
                            g.idCarona = obj.getInt("cod_carona");
                            g.toString();
                            lista.add(g);
                        }
                    }else{
                        dp.dismiss();
                        Snackbar.make(layout,"Você não esta em nenhuma carona !", Snackbar.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                    dp.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Gerenca","Erro: "+ error.getMessage());
                dp.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<>();
                parms.put("opc", "1");
                parms.put("id", getContext().getSharedPreferences("Cadastro", Context.MODE_PRIVATE).getString("id", "0"));
                Log.i("Gerencia", "ID: "+ parms.get("id"));
                return parms;
            }
        };
        RequestQueue qr = Volley.newRequestQueue(getContext());
        qr.add(sRequest);

        rv = (RecyclerView) layout.findViewById(R.id.rvGerencia);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        return layout;
    }

    @Override
    public View.OnClickListener OnClickGerencia(View v, final int idx) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getContext(), DetalhesCaronaActivity.class);
                it.putExtra("nome", lista.get(idx).nomeGerencia);
                it.putExtra("hora", lista.get(idx).horaGerencia);
                it.putExtra("dia", lista.get(idx).diaGerencia);

                startActivity(it);
            }
        };
    }
}
