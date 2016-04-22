package com.partiufacu.kronaproject.partiufacu.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.activity.DetalhesCaronaActivity;
import com.partiufacu.kronaproject.partiufacu.interfaces.Constantes;
import com.partiufacu.kronaproject.partiufacu.objetos.Carona;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta classe tem intuito de criar os Adapter do RecclerView.
 * Os adapter são as linhas da Lista tipo RecyclerView.
 * <p>
 * Dentro implementamos o BuscarHolder, que é a tecnica usado no RecyclerView.
 * <p>
 * Temos que passar com View uma view do Tipo adapter_buscar.xml
 * <p>
 * Created by lfelipeeb on 06/12/15.
 */
public class BuscarAdapter extends RecyclerView.Adapter<BuscarAdapter.BuscarHolder> {
    private Context ctx;
    private List<Carona> lista;
    private int userId;
    private AlertDialog alert;

    public BuscarAdapter(Context ctx, List<Carona> lista) {
        this.ctx = ctx;
        this.lista = lista;
        userId = Integer.parseInt(ctx.getSharedPreferences("Cadastro", Context.MODE_PRIVATE).getString("id", "0"));
    }

    /**
     * Cria e infla a view no Layout. É chamada pela classe RecyclerView nao precisa ter a chamada explicita.
     *
     * @param parent
     * @param viewType
     * @return View Holder, ou o Adapter que foi criado.
     */
    @Override
    public BuscarHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BuscarHolder(LayoutInflater.from(ctx).inflate(R.layout.adapter_buscar, parent, false));
    }

    /**
     * Este metodo é chamado por dentro da classe, ele recebe o indice do elemento, e atualiza as views que estão no HOLDER.
     *
     * @param holder   View a ser Atualizada
     * @param position Posição da VIEW.
     */
    @Override
    public void onBindViewHolder(final BuscarHolder holder, final int position) {
        final Carona carona = lista.get(position);

        holder.nome.setText(carona.getNome());
        holder.dia.setText(carona.getData());
        holder.hora.setText(carona.getHorario());
        holder.vagas.setText("Vagas: "+Integer.toString(carona.getVagas()));
        holder.preco.setText("Preço: "+carona.getPrecoString());
        holder.lugares.setText("Saida: "+carona.getLugarSaida());
        holder.lugarChegada.setText("Chegada: "+carona.getLugarChegada());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle parms = new Bundle();
                parms.putString("id", Long.toString(carona.getId()));
                parms.putString("nome_dono", carona.getNome_user());
                Intent it = new Intent(ctx, DetalhesCaronaActivity.class);
                it.putExtras(parms);
                ctx.startActivity(it);
            }
        });

        holder.btnReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReserva(holder, carona);
            }
        });
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

        Volley.newRequestQueue(ctx).add(request);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setList(List<Carona> lista) {
        this.lista = lista;
    }

    public class BuscarHolder extends RecyclerView.ViewHolder {
        public TextView nome, dia, hora, vagas, preco, lugares, lugarChegada;
        public Button btnReserva;
        public View v;

        public BuscarHolder(View itemView) {
            super(itemView);
            this.v = itemView;

            nome = (TextView) v.findViewById(R.id.adapterNome);
            dia = (TextView) v.findViewById(R.id.adapterDia);
            hora = (TextView) v.findViewById(R.id.adapterHora);
            vagas = (TextView) v.findViewById(R.id.adapterVagas);
            preco = (TextView) v.findViewById(R.id.adapterPreco);
            lugares = (TextView) v.findViewById(R.id.adapterLugares);
            lugarChegada = (TextView) v.findViewById(R.id.adapterLugarChegada);

            btnReserva = (Button) v.findViewById(R.id.btnBuscarReserva);
        }
    }

    public void dialogReserva(final BuscarHolder holder, final Carona carona){
        View v = LayoutInflater.from(ctx).inflate(R.layout.pop_reserva,null);
        final EditText ed = (EditText) v.findViewById(R.id.edReserva);
        Button btnOk = (Button) v.findViewById(R.id.btnReservaOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pDialog = new ProgressDialog(ctx);
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
                                    Snackbar.make(holder.v, res, Snackbar.LENGTH_SHORT).show();
                                    if(res.equals("Carona reservada com sucesso!")){
                                        enviaNotificacao(Integer.toString((int) carona.getId()));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Snackbar.make(holder.v, "Algo de errado aconteceu !", Snackbar.LENGTH_SHORT).show();
                        Log.i("Reserva", error.getMessage());
                    }
                }){
                    @Override
                    public Map<String, String> getParams() {
                        Map<String, String> parms = new HashMap<String, String>();
                        parms.put("opc", "1");
                        parms.put("id", Integer.toString(userId));
                        parms.put("obs", ed.getText().toString());
                        parms.put("id_carona", Integer.toString((int) carona.getId()));
                        return parms;
                    }
                };

                Volley.newRequestQueue(ctx).add(request);
            }
        });

        Button btnNao = (Button) v.findViewById(R.id.btnReservaN);
        btnNao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Observação: ");
        builder.setView(v);
        alert = builder.create();
        alert.show();
    }
}
