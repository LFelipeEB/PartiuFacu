package com.partiufacu.kronaproject.partiufacu.objetos;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Está classe define o objeto Carona, e por ela padronizamos as caronas no APP.
 * <p/>
 * Created by lfelipeeb on 05/12/15.
 */

public class Carona {
    public static List<Carona> buscas = null;

    private long id;
    private String nome;
    private String nome_user;
    private String tel;
    private int vagas;
    private String data;
    private String horario;
    private double preco;
    private String lugarSaida, lugarChegada;


    public Carona(String nome, int vagas, String data, String horario, double preco, String lugarSaida, String lugarChegada, String tel) {
        this.nome = nome;
        this.vagas = vagas;
        this.data = data;
        this.horario = horario;
        this.preco = preco;
        this.lugarSaida = lugarSaida;
        this.lugarChegada = lugarChegada;
        this.tel = tel;
    }

    public Carona() {
    }

    private static Carona fromJson(String sjson) {
        JSONObject json = null;
        try {
            json = new JSONObject(sjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Carona c = new Carona();
        try {
            c.setId(json.getInt("cod_carona"));
            Log.e("Carona", "fromJson: " + c.getId());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Carona", "fromJson ID_OFERTA");
        }
        try {
            c.setNome(json.getString("nome_carona"));
            Log.e("Carona", "fromJson: " + c.getNome());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Carona", "fromJson NOME");
        }
        try {
            c.setVagas(json.getString("vagas"));
            Log.e("Carona", "fromJson: " + c.getVagas());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Carona", "fromJson VAGAS");
        }
        try {
            c.setPreco(json.getString("preco"));
            Log.e("Carona", "fromJson: " + c.getPreco());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Carona", "fromJson PREÇO");
        }
        try {
            String[] dt = json.getString("data").split("-");
            String data = dt[2]+" / "+dt[1]+" / "+dt[0];
            c.setData(data);
            Log.e("Carona", "fromJson: " + c.getData());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Carona", "fromJson DATA");
        }
        try {
            String[] hrr = json.getString("horario").split(":");
            String horario = hrr[0]+" : "+hrr[1];
            c.setHorario(horario);
            Log.e("Carona", "fromJson: " + c.getHorario());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("CArona", "fromJson HORARIO");
        }
        try {
            c.setNome_user(json.getString("nome"));
            Log.e("Carona", "fromJson: " + c.getNome());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Carona", "fromJson NOME USER");
        }
        try {
            c.setTel(json.getString("tel"));
            Log.e("Carona", "fromJson: " + c.getTel());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Carona", "fromJson TEL");
        }
        try {
            c.setLugarSaida(json.getString("local_saida"));
            Log.e("Carona", "fromJson: " + c.getLugarSaida());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Carona", "fromJson LOCAL SAIDA");
        }
        try {
            c.setLugarChegada(json.getString("local_chegada"));
            Log.e("Carona", "fromJson: " + c.getLugarChegada());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Carona", "fromJson LOCAL CHEGADA");
        }



        return c;
    }

    public static void fromJsonArray(String sArray) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(sArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Carona> caronas = new ArrayList<>();
        Carona c = null;
        for (int i = 0; i < jsonArray.length(); ++i) {
            try {
                c = fromJson(jsonArray.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            caronas.add(c);
        }
        buscas = caronas;
        ;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    private void setPreco(String preco) {
        this.preco = Double.parseDouble(preco);
    }

    public String getHorario() {
        String[] ht = horario.split(":");
        return ht[0]+":"+ht[1];
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public void setVagas(String vagas) {
        this.vagas = Integer.parseInt(vagas);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPrecoString() {
        return Double.toString(preco);
    }

    public String getNome_user() {
        return nome_user;
    }

    public void setNome_user(String nome_user) {
        this.nome_user = nome_user;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLugarChegada() {
        return lugarChegada;
    }

    public void setLugarChegada(String lugarChegada) {
        this.lugarChegada = lugarChegada;
    }

    public String getLugarSaida() {
        return lugarSaida;
    }

    public void setLugarSaida(String lugarSaida) {
        this.lugarSaida = lugarSaida;
    }

}
