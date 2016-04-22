package com.partiufacu.kronaproject.partiufacu.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.partiufacu.kronaproject.partiufacu.R;
import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class PerfilDetalhesActivityFragment extends Fragment {

    private static String idUser = "1";
    private TextView nome, email, curso, telefone;
    private ImageView image;

    public PerfilDetalhesActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_perfil_detalhes, container, false);


        nome = (TextView) layout.findViewById(R.id.nome_usuario_perfil);
        email = (TextView) layout.findViewById(R.id.email_nome_perfil);
        curso = (TextView) layout.findViewById(R.id.curso_perfil);
        telefone = (TextView) layout.findViewById(R.id.telefone_perfil);
        image = (ImageView) layout.findViewById(R.id.icon_perfil);

            SharedPreferences prefs = getContext().getSharedPreferences("Cadastro", Context.MODE_PRIVATE);

            nome.setText(prefs.getString("nome", "CURSO"));
            email.setText(prefs.getString("email", "CURSO"));
            curso.setText(prefs.getString("curso", "CURSO"));
            telefone.setText(prefs.getString("telefone", "CURSO"));
            Picasso.with(getContext()).load(prefs.getString("foto", "CURSO"))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(image);
        return layout;
    }


    public void setIdUser(String id) {
        idUser = id;
    }

    public String getIdUser() {
        return idUser;
    }
}
