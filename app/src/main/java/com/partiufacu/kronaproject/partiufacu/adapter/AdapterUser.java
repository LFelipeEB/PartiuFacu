package com.partiufacu.kronaproject.partiufacu.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.fragment.UserDialogFragment;
import com.partiufacu.kronaproject.partiufacu.interfaces.Constantes;
import com.partiufacu.kronaproject.partiufacu.objetos.Usuarios;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lfelipeeb on 10/02/16.
 */
public class AdapterUser extends RecyclerView.Adapter<AdapterUser.UserHolder> {
    private List<Usuarios> listaUsers;
    private Context ctx;
    private AppCompatActivity act;
    private String nomeDono, idCarona;

    public AdapterUser(List<Usuarios> lista, AppCompatActivity act,String nomeDono, String idCarona) {
        this.listaUsers = lista;
        this.ctx = act.getBaseContext();
        this.act = act;
        this.nomeDono = nomeDono;
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserHolder(LayoutInflater.from(ctx).inflate(R.layout.adapter_user, parent, false));
    }

    @Override
    public void onBindViewHolder(UserHolder holder, final int position) {
        final Usuarios u = listaUsers.get(position);

        holder.lugar.setText(u.lugar);
        holder.nome.setText(u.nome);
        Picasso.with(ctx).load(u.foto).into(holder.foto);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("id", u.idUser);
                UserDialogFragment udf = new UserDialogFragment();
                udf.setArguments(b);
                udf.show(act.getSupportFragmentManager(), "Detalhes");
            }
        });

        Log.i("Partiu: situacao", u.situacao+ " ");
        String dono = act.getSharedPreferences("Cadastro", Context.MODE_PRIVATE).getString("nome", "1");

        Log.i("Partiu: User", "dono: "+dono);
        Log.i("Partiu: User", "nome dono: "+nomeDono);

        if(dono.equals(nomeDono)){
            Log.i("Partiu: User", "Dono olhando carana");
            //Aceito
            if (u.situacao.equals("1")){
                holder.aguardando.setVisibility(View.GONE);
                holder.recusa.setVisibility(View.GONE);
                holder.aceito.setVisibility(View.VISIBLE);
                holder.aceito.setText("Vaga Confirmada !");
            }
            //Recusado
            else if(u.situacao.equals("0")){
                holder.aguardando.setVisibility(View.GONE);
                holder.recusa.setVisibility(View.GONE);
                holder.aceito.setVisibility(View.VISIBLE);
                holder.aceito.setText("Vaga Recusada!");
            }
            //Aguardando
            else{
                holder.aceito.setVisibility(View.VISIBLE);
                holder.aceito.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enviaDecisao(1, u.idReserva);
                    }
                });
                holder.recusa.setVisibility(View.VISIBLE);
                holder.recusa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enviaDecisao(0, u.idReserva);
                    }
                });
                holder.aguardando.setVisibility(View.GONE);
            }
        }else{
            Log.i("Partiu: User", "Passageiro olhando carana");

            //Aceito
            if (u.situacao.equals("1")){
                holder.aguardando.setVisibility(View.GONE);
                holder.recusa.setVisibility(View.GONE);
                holder.aceito.setVisibility(View.VISIBLE);
                holder.aceito.setText("Vaga Confirmada !");
            }
            //Recusado
            else if(u.situacao.equals("0")){
                holder.aguardando.setVisibility(View.GONE);
                holder.recusa.setVisibility(View.VISIBLE);
                holder.aceito.setVisibility(View.GONE);
                holder.recusa.setText("Vaga Recusada !");
            }
            //Aguardando
            else{
                holder.aguardando.setVisibility(View.GONE);
                holder.recusa.setVisibility(View.GONE);
                holder.aceito.setVisibility(View.VISIBLE);
                holder.aceito.setText("Aguardando Resposta!");
            }
        }

    }

    @Override
    public int getItemCount() {
        return listaUsers.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        protected TextView nome, lugar;
        protected ImageView foto;
        protected View v;
        protected Button aceito, aguardando, recusa;

        public UserHolder(View itemView) {
            super(itemView);
            this.v = itemView;
            this.nome = (TextView) itemView.findViewById(R.id.NomeUser);
            this.lugar = (TextView) itemView.findViewById(R.id.localUser);
            this.foto = (ImageView) itemView.findViewById(R.id.fotoUser);
            this.aceito = (Button) itemView.findViewById(R.id.userAceita);
            this.recusa = (Button) itemView.findViewById(R.id.userRecusar);
            this.aguardando = (Button) itemView.findViewById(R.id.useResposta);

        }
    }

    public void enviaDecisao(final int decisao, final String idReserva){
        StringRequest request = new StringRequest(Request.Method.POST, Constantes.Reserva,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Partiu: Decisao", " "+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("opc", "2");
                params.put("situacao", Integer.toString(decisao));
                params.put("cod_reserva", idReserva);
                return params;
            }
        };

        Volley.newRequestQueue(ctx).add(request);
    }
}
