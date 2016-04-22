package com.partiufacu.kronaproject.partiufacu.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.adapter.AdapterUser;
import com.partiufacu.kronaproject.partiufacu.interfaces.Constantes;
import com.partiufacu.kronaproject.partiufacu.objetos.Carona;
import com.partiufacu.kronaproject.partiufacu.objetos.Usuarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetalhesCaronaActivity extends BaseActivity {

    private RecyclerView rvDetalhes;
    private TextView nome, data, horario, saida, chegada, preco, nomeUser, obs;
    private String idCarona;
    private AdapterUser userAdapter;
    private ProgressDialog pd;
    private boolean user=false, infos=false;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_carona);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pd = new ProgressDialog(this);
        pd.setCancelable(true);

        pd.setMessage("Aguarde estamos buscando as informações ... ");
        pd.setTitle("PartiuFacu Avisa");
        pd.show();

        Intent it = getIntent();
        Bundle bdl = it.getExtras();

        idCarona = bdl.getString("id");

        nome = (TextView) findViewById(R.id.nomeGerencia);
        data = (TextView) findViewById(R.id.diaGerencia);
        horario = (TextView) findViewById(R.id.horaGerencia);
        this.saida = (TextView) findViewById(R.id.saidaGerencia);
        this.chegada = (TextView) findViewById(R.id.chegadaGerencia);
        this.preco = (TextView) findViewById(R.id.precoGerencia);
        this.nomeUser =(TextView) findViewById(R.id.nome_user_Gerencia);
        this.obs = (TextView) findViewById(R.id.obsDetalhe);

        getCaronasInfos(idCarona);

        userAdapter = new AdapterUser(getUsers(), this, bdl.getString("nome_dono"), idCarona);

        rvDetalhes = (RecyclerView) findViewById(R.id.rvCaronas);
        rvDetalhes.setLayoutManager(new LinearLayoutManager(this));
        rvDetalhes.setItemAnimator(new DefaultItemAnimator());
        rvDetalhes.setAdapter(userAdapter);

        Button btnReserva = (Button) findViewById(R.id.btnReservaDetalhes);
        btnReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReserva();
            }
        });
    }


    public void dialogReserva(){
        View v = LayoutInflater.from(this).inflate(R.layout.pop_reserva,null);
        final EditText ed = (EditText) v.findViewById(R.id.edReserva);
        Button btnOk = (Button) v.findViewById(R.id.btnReservaOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ProgressDialog pDialog = new ProgressDialog(DetalhesCaronaActivity.this);
                pDialog.setTitle("Fazendo Reserva ...");
                StringRequest request = new StringRequest(Request.Method.POST, Constantes.Reserva,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("Partiu: Reserva", response);
                                JSONObject resposta = null;
                                try {
                                    resposta = new JSONObject(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                pDialog.dismiss();
                                try {
                                    String res = resposta.getString("result");
                                    Snackbar.make(v, res, Snackbar.LENGTH_SHORT).show();
                                    if(res.equals("Carona reservada com sucesso!")){
                                        enviaNotificacao(idCarona);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Snackbar.make(v, "Algo de errado aconteceu !", Snackbar.LENGTH_SHORT).show();
                        Log.i("Reserva", error.getMessage());
                    }
                }){
                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> parms = new HashMap<String, String>();
                        parms.put("opc", "1");
                        parms.put("id", getSharedPreferences("Cadastro",MODE_PRIVATE).getString("id","0"));
                        parms.put("obs", ed.getText().toString());
                        parms.put("id_carona",idCarona);
                        return parms;
                    }
                };

                Volley.newRequestQueue(DetalhesCaronaActivity.this).add(request);
            }
        });

        Button btnNao = (Button) v.findViewById(R.id.btnReservaN);
        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(DetalhesCaronaActivity.this);
        builder.setTitle("Observação: ");
        builder.setView(v);
        alert = builder.create();
        alert.show();
    }

    private List<Usuarios> getUsers(){
        final List<Usuarios> users = new ArrayList<>();

        Log.i("getUsers", "GetUsers");

        StringRequest request = new StringRequest(Request.Method.POST, Constantes.Gerencia,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("getUsers", response);
                        JSONArray array = null;
                        try {
                            array = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                        for(int i=0; i<array.length(); i++){
                            Usuarios use = new Usuarios();
                            try {
                                JSONObject obj = array.getJSONObject(i);
                                use.foto= obj.getString("foto");
                                use.nome = obj.getString("nome");
                                use.idUser = obj.getString("id");
                                use.situacao = obj.getString("situacao");
                                use.idReserva = obj.getString("cod_reserva");
                                use.lugar = obj.getString("obs");

                                users.add(use);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                user = true;
                            }
                        }
                        userAdapter.notifyDataSetChanged();

                        if(infos)
                            pd.cancel();

                        user=true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<>();
                parms.put("opc", "2");
                parms.put("cod", idCarona);
                return parms;
            }
        };

        Volley.newRequestQueue(this).add(request);

        return users;
    }

    private void getCaronasInfos(final String idCarona){
          StringRequest request = new StringRequest(Request.Method.POST, Constantes.buscar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("PartiuFacu", response);
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            preco.setText("Preço: "+json.getString("preco"));
                            nome.setText(json.getString("nome_carona"));
                            data.setText("Data: "+json.getString("data"));
                            horario.setText("Horario: "+json.getString("horario"));
                            nomeUser.setText("Quem Publicou: "+json.getString("nome"));
                            saida.setText("Local de saida: "+json.getString("local_saida"));
                            chegada.setText("Local de chegada: "+json.getString("local_chegada"));
                            obs.setText("Observações: \n"+ json.getString("obs"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            infos = true;
                        }
                        if(user)
                            pd.cancel();

                        infos=true;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Detalhes", " "+ error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<>();
                parms.put("opc", "2");
                parms.put("id_carona", idCarona);
                return parms;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }
    private void enviaNotificacao(final String idCarona) {
        StringRequest request = new StringRequest(Request.Method.POST, Constantes.Reserva,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Partiu: notificacao", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Reserva", error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<String, String>();
                parms.put("opc", "3");
                parms.put("id_carona", idCarona);
                return parms;
            }
        };

        Volley.newRequestQueue(DetalhesCaronaActivity.this).add(request);
    }


}
