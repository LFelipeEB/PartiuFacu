package com.partiufacu.kronaproject.partiufacu.network;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by lfelipeeb on 06/12/15.
 */
public class RequestJsonArray extends Request<JSONArray> {

    private JSONArray json;
    private Map<String, String> parametros;
    private Response.Listener<JSONArray> responde;

    public RequestJsonArray(String url, Response.ErrorListener listener, Map<String, String> parametros, Response.Listener<JSONArray> responde) {
        super(Method.POST, url, listener);
        this.parametros = parametros;
        this.responde = responde;
    }

    /**
     * Requisição é feita aqui
     *
     * @param response
     * @return
     */
    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        String js = null;
        try {
            //Leio o JSONArray que o servidor respondeu, e trnasformo em uma string
            js = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Log.i("Request", js);
            json = new JSONArray(js);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //Crio o JSONArray para retornar, junto com os Headers do serve
        return (Response.success(json, HttpHeaderParser.parseCacheHeaders(response)));
    }


    /**
     * Finalização(entrega) da requisição
     *
     * @param response
     */
    @Override
    protected void deliverResponse(JSONArray response) {
        this.responde.onResponse(response);
    }

    @Override
    public Map<String, String> getParams() {
        return this.parametros;
    }

    public JSONArray getJson() {
        return json;
    }

}
