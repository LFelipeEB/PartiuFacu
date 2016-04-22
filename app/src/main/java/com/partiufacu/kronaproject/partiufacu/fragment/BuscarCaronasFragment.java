package com.partiufacu.kronaproject.partiufacu.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.adapter.BuscarAdapter;
import com.partiufacu.kronaproject.partiufacu.interfaces.Constantes;
import com.partiufacu.kronaproject.partiufacu.network.RequestJsonArray;
import com.partiufacu.kronaproject.partiufacu.objetos.Carona;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuscarCaronasFragment extends Fragment {

    private RecyclerView rvBusca;
    private BuscarAdapter adapterBuscar;
    private RequestQueue rq;
    private List<Carona> listaCarona;
    private ProgressDialog pDialog;
    private Context context;
    private Map<String, String> parms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        pDialog = ProgressDialog.show(context, null, "Buscando Caronas ...", true, false);

        View layout = inflater.inflate(R.layout.fragment_buscar_caronas, container, false);

        rq = Volley.newRequestQueue(context);

        listaCarona = new ArrayList<>();

        adapterBuscar = new BuscarAdapter(context, listaCarona);

        rvBusca = (RecyclerView) layout.findViewById(R.id.rvBusca);
        rvBusca.setLayoutManager(new LinearLayoutManager(context));
        rvBusca.setItemAnimator(new DefaultItemAnimator());
        rvBusca.setAdapter(adapterBuscar);
        parms = new HashMap<>();
        parms.put("opc", "1");

        buscarCaronas();

        return layout;
    }

    private List<Carona> buscarCaronas() {
        RequestJsonArray request = new RequestJsonArray(Constantes.buscar,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Carona", "Response: " + error.getMessage());
                        pDialog.dismiss();
                    }
                }, parms, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    Log.e("Carona", "Response" + response.toString());
                    Carona.fromJsonArray(response.toString());
                    listaCarona.addAll(Carona.buscas);
                    adapterBuscar.notifyDataSetChanged();
                    Toast.makeText(context, "Lista Buscada", Toast.LENGTH_SHORT).show();
                }else{
                    Log.i("Partiu: response", "Responta nula");
                }
                pDialog.dismiss();
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> parm = new HashMap<>();
                parm.put("opt","data");
                parm.put("opc","1");
                return parm;
            }
        };
        rq.add(request);

        return Carona.buscas;
    }
}
