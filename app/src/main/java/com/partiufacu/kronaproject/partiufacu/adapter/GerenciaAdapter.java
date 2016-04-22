package com.partiufacu.kronaproject.partiufacu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.activity.DetalhesCaronaActivity;
import com.partiufacu.kronaproject.partiufacu.objetos.Gerenciamento;

import java.util.List;

/**
 * Created by lfelipeeb on 10/02/16.
 */
public class GerenciaAdapter extends RecyclerView.Adapter<GerenciaAdapter.HolderGerencia> {
    public OnClickGerencia clickRv;
    List<Gerenciamento> listaGerenciamento;
    Context ctx;

    public GerenciaAdapter(List<Gerenciamento> lista, Context context, OnClickGerencia click) {
        this.listaGerenciamento = lista;
        this.ctx = context;
        this.clickRv = click;
    }

    @Override
    public HolderGerencia onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderGerencia(LayoutInflater.from(ctx).inflate(R.layout.adapter_gerencia, parent, false));
    }

    @Override
    public void onBindViewHolder(HolderGerencia holder, final int position) {
        final Gerenciamento g = listaGerenciamento.get(position);

        holder.horaGerencia.setText("Horario: "+g.horaGerencia);
        holder.diaGerencia.setText("Data: "+g.diaGerencia);
        holder.nomeGerencia.setText(g.nomeGerencia);
        holder.precoGerencia.setText("Preço: "+g.precoGerencia);
        holder.nomeUserGerencia.setText(g.nomeUserGerencia);
        holder.saidaGerencia.setText("Saida: "+g.saidaGerencia);
        holder.chegadaGerencia.setText("Chegada: " + g.chegadaGerencia);
        holder.vagasGerencia.setText("Vagas: "+g.vagasGerencia);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle parms = new Bundle();
                parms.putString("id", Long.toString(g.idCarona));
                parms.putString("nome_dono", g.nomeUserGerencia);
                Intent it = new Intent(ctx, DetalhesCaronaActivity.class);
                it.putExtras(parms);
                ctx.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i("SizeGerencia", listaGerenciamento.size()+" ");
        return listaGerenciamento.size();
    }

    public interface OnClickGerencia {
        public View.OnClickListener OnClickGerencia(View v, int idx);
    }

    public class HolderGerencia extends RecyclerView.ViewHolder {
        protected TextView nomeGerencia, diaGerencia, horaGerencia, vagasGerencia, precoGerencia, nomeUserGerencia, saidaGerencia, chegadaGerencia;
        protected View v;
        protected int idCarona;
        public HolderGerencia(View itemView) {
            super(itemView);
            this.v = itemView;

            nomeGerencia = (TextView) itemView.findViewById(R.id.nomeGerencia);
            diaGerencia = (TextView) itemView.findViewById(R.id.diaGerencia);
            horaGerencia = (TextView) itemView.findViewById(R.id.horaGerencia);
            vagasGerencia = (TextView) itemView.findViewById(R.id.vagasGerencia);
            precoGerencia = (TextView) itemView.findViewById(R.id.precoGerencia);
            nomeUserGerencia = (TextView) itemView.findViewById(R.id.nomeUserGerencia);
            saidaGerencia = (TextView) itemView.findViewById(R.id.saidaGerencia);
            chegadaGerencia = (TextView) itemView.findViewById(R.id.chegadaGerencia);

        }
    }
}
