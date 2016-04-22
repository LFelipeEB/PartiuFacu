package com.partiufacu.kronaproject.partiufacu.objetos;

/**
 * Created by lfelipeeb on 12/12/15.
 */
public class Usuarios {
    public String nome = "";
    public String foto;
    public String lugar = "";
    public String email;
    public String idUser;
    public String situacao;
    public String idReserva;

    public Usuarios() {
    }

    public Usuarios(String nome, String lugar) {
        this.nome = nome;
        this.lugar = lugar;
    }

}
