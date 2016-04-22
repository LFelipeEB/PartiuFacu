package com.partiufacu.kronaproject.partiufacu.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.partiufacu.kronaproject.partiufacu.GoogleServices.RegistrationGCMkey;
import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.interfaces.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity{

    private ProgressDialog pd;
    private CallbackManager callbackManager;
    private AccessToken token;
    private LinearLayout layoutCadastro, layoutLogin;
    private EditText edNome, edEmail, edSenha, edCurso, edEndereco, edTelefone, edEmailLogin, edSenhaLogin;
    private Button confirmaCadastro, btnLogin;
    private String idFacebook, nome, email, foto, senha, idUser, curso, telefone, endereco;

    @Override
    protected void onCreate(Bundle savedInstaceState){
        super.onCreate(savedInstaceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        layoutCadastro =(LinearLayout) findViewById(R.id.layout_cadastro);
        layoutLogin =(LinearLayout) findViewById(R.id.layout_logins);

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_facebook);
        loginButton.setReadPermissions("public_profile", "email");
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        requestData(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

        token = AccessToken.getCurrentAccessToken();

        edEmailLogin = (EditText) findViewById(R.id.userLogin);
        edSenhaLogin = (EditText) findViewById(R.id.senhaLogin);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Fazendo Login\nAguarde ...");
                pd.show();
                email = edEmailLogin.getText().toString();
                senha = edSenhaLogin.getText().toString();
                efetuaLogin(email, senha);
            }
        });
    }

    private void efetuaLogin(final String email, final String senha) {
        StringRequest request = new StringRequest(Request.Method.POST, Constantes.Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("efetuaLogin", response);
                try{
                    JSONObject json = new JSONObject(response);
                    idUser = json.getString("result");
                    if(idUser.isEmpty() || idUser.equals("Usuario não identificado!")){
                        Snackbar.make(btnLogin, "Algo está errado confira os dados ¹", Snackbar.LENGTH_SHORT).show();
                    }else{
                        verificaInfos();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pd.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<>();
                parms.put("opc", "3");
                Log.i("efetuaLogin() Params:", email);
                parms.put("email", email);
                parms.put("senha", senha);
                return parms;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void verificaInfos() {
        StringRequest request = new StringRequest(Request.Method.POST, Constantes.Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VerificaInfos", response);
                try {
                    JSONObject infos = new JSONObject(response);
                    if((infos.getInt("cod_user") > 0)) {
                        nome = infos.getString("nome");
                        telefone = infos.getString("telefone");
                        foto = infos.getString("foto");
                        endereco = infos.getString("local");
                        email = infos.getString("email");
                        curso = infos.getString("curso");
                        idFacebook = infos.getString("acces_facebook");
                        salvaPrefs();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getParams() {
                Map<String, String> parms = new HashMap<>();
                parms.put("opc", "4");
                Log.i("verificaLogin()", email);
                parms.put("id", idUser);
                return parms;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void requestData(AccessToken accesToken){
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.show();
        dialog.setMessage("Carregando ... ");

        GraphRequest request = GraphRequest.newMeRequest(accesToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("requestData()", "Objeto: " + object.toString());
                        Log.i("requestData()", "Graph: " + response.toString());
                        try {
                            idFacebook = object.getString("id");
                            nome = object.getString("name");
                            email = object.getString("email");
                            foto = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            verificaEmail(email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
        Bundle parametros = new Bundle();
        parametros.putString("fields", "id,name,email,picture.type(large)");
        request.setParameters(parametros);
        request.executeAsync();
    }

    private void enviarInfos(final ProgressDialog pd) {
        StringRequest request = new StringRequest(Request.Method.POST, Constantes.Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("enviaInfos()", response);
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Snackbar.make(confirmaCadastro, json.getString("result"), Snackbar.LENGTH_SHORT).show();
                    idUser = json.getString("id");
                    salvaPrefs();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                salvaPrefs();
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("enviaInfos()","Erro: "+error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getParams(){
                Map<String, String> parms = new HashMap<>();
                parms.put("opc", "1");
                parms.put("email", email);
                parms.put("nome", nome);
                parms.put("telefone", edTelefone.getText().toString());
                parms.put("endereco", edEndereco.getText().toString());
                parms.put("curso", edCurso.getText().toString());
                parms.put("foto_perfil", foto);
                parms.put("senha", edSenha.getText().toString());
                parms.put("id_fb", idFacebook);
                return parms;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void salvaPrefs() {
        SharedPreferences.Editor edit = getSharedPreferences("Cadastro", MODE_PRIVATE).edit();
        edit.putString("idFacebook", idFacebook);
        edit.putString("id", idUser);
        edit.putString("nome", nome);
        edit.putString("email", email);
        edit.putString("foto", foto);
        edit.putString("cod_user", idUser);
        edit.putString("endereco", endereco);
        edit.putString("curso", curso);
        edit.putString("telefone", telefone);
        edit.commit();

        //Login em novo Device então envia a key para nosso serve;
        Intent it = new Intent(this, RegistrationGCMkey.class);
        Bundle b = new Bundle();
        b.putString("id",idUser);
        it.putExtras(b);
        startService(it);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private boolean verificaEmail(final String email) {
        final boolean[] returno = {false};
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Carregando ... ");
        pd.show();

        StringRequest request = new StringRequest(Request.Method.POST, Constantes.Login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("verificarEmail()", response);
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    returno[0] = jObj.getBoolean("result");
                    Log.i("VerificaEmailRetorno", Boolean.toString(returno[0]));
                    if(returno[0]){
                        idUser = jObj.getString("id");
                        verificaInfos();
                    }else{
                        fazCadastro();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VerificaEmail()","Erro: "+ error.getMessage()+" ");
                pd.dismiss();
            }
        }){
            @Override
            public Map<String, String> getParams(){
                Map<String, String> parms = new HashMap<>();
                parms.put("opc", "5");
                parms.put("email", email);
                return parms;
            }
        };
        Volley.newRequestQueue(this).add(request);
        return returno[0];
    }

    private void fazCadastro(){
        Log.i("RequestData", "email não encontrado na base de dados");
        layoutLogin.setVisibility(View.GONE);
        layoutCadastro.setVisibility(View.VISIBLE);
        edNome = (EditText) findViewById(R.id.cadastro_nome);
        edCurso = (EditText) findViewById(R.id.cadastro_curso);
        edEmail = (EditText) findViewById(R.id.cadastro_email);
        edEndereco = (EditText) findViewById(R.id.cadastro_endereço);
        edSenha = (EditText) findViewById(R.id.cadastro_senha);
        edTelefone = (EditText) findViewById(R.id.cadastro_telefone);
        confirmaCadastro = (Button) findViewById(R.id.btnConfirmar);
        confirmaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog pd = new ProgressDialog(getBaseContext());
                pd.setMessage("Carregando ... ");
                enviarInfos(pd);
            }
        });
        edNome.setText(nome);
        edEmail.setText(email);

    }
}