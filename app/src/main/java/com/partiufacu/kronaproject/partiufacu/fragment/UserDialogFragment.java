package com.partiufacu.kronaproject.partiufacu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.interfaces.Constantes;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by evari on 03/04/2016.
 */
public class UserDialogFragment extends DialogFragment {
    private TextView nome, email, curso, telefone;
    private ImageView image;


    public UserDialogFragment(){
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getArguments();
        Log.i("Partiu: Args", b.toString());
        View v = inflater.inflate(R.layout.fragment_perfil_detalhes,container);
        nome = (TextView) v.findViewById(R.id.nome_usuario_perfil);
        email = (TextView) v.findViewById(R.id.email_nome_perfil);
        curso = (TextView) v.findViewById(R.id.curso_perfil);
        telefone = (TextView) v.findViewById(R.id.telefone_perfil);
        image = (ImageView) v.findViewById(R.id.icon_perfil);
        Log.i("Partiu: Create", "Bundle :"+b.getString("id"));
        getInfosFromWeb(b.getString("id"));
        return v;
    }

    public void show(FragmentManager fm, String tag) {
        super.show(fm, tag);
    }


    public void getInfosFromWeb(final String idUser) {
        StringRequest request = new StringRequest(Request.Method.POST, Constantes.buscar, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(" Partiu: Detalhes", response);
                try {
                    JSONObject json = new JSONObject(response);
                    nome.setText(json.getString("nome"));
                    email.setText(json.getString("email"));
                    curso.setText(json.getString("curso"));
                    telefone.setText(json.getString("telefone"));
                    Picasso.with(getContext()).load(json.getString("perfil_foto"))
                            .placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("opc", "3");
                parms.put("user", idUser);
                return parms;
            }
        };

        Volley.newRequestQueue(getContext()).add(request);
    }

}
