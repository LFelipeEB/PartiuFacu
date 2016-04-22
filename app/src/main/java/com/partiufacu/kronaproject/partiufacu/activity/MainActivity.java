package com.partiufacu.kronaproject.partiufacu.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.fragment.AdicionarCaronaFragment;
import com.partiufacu.kronaproject.partiufacu.fragment.BuscarCaronasFragment;
import com.partiufacu.kronaproject.partiufacu.fragment.DesenvolvedoresFragment;
import com.partiufacu.kronaproject.partiufacu.fragment.GerenciaFragment;
import com.partiufacu.kronaproject.partiufacu.fragment.ParceriasFragment;
import com.partiufacu.kronaproject.partiufacu.fragment.PerfilDetalhesActivityFragment;
import com.partiufacu.kronaproject.partiufacu.fragment.ProjetoCaronaFragment;
import com.squareup.picasso.Picasso;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String nome, email, fotoUser;
    DrawerLayout mDrawerLayout;
    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getUser();

        mFragmentManager = getSupportFragmentManager();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View v = View.inflate(this, R.layout.nav_header_main, navigationView);
        TextView nome = (TextView) v.findViewById(R.id.nome_usuario_drawer);
        nome.setText(this.nome);
        TextView email = (TextView) v.findViewById(R.id.email_nome_drawer);
        email.setText(this.email);

        ImageView image = (ImageView) v.findViewById(R.id.profile_image);
        Picasso.with(this).load(this.fotoUser)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(image);

        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.perfil_nav);
        linearLayout.setOnClickListener(onCLickPerfil());
        mudarFragment(new ParceriasFragment());
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_adicionar_carona) {
            mudarFragment(new AdicionarCaronaFragment());

        } else if (id == R.id.nav_buscar_caronas) {
            mudarFragment(new BuscarCaronasFragment());

        } else if (id == R.id.nav_gerenciar_caronas) {
            mudarFragment(new GerenciaFragment());
        } else if (id == R.id.nav_projeto_carona) {
            mudarFragment(new ProjetoCaronaFragment());

        } else if (id == R.id.nav_desenvolvedores) {
            mudarFragment(new DesenvolvedoresFragment());

        } else if (id == R.id.nav_parcerias) {
            mudarFragment(new ParceriasFragment());

        } else if (id == R.id.nav_sair) {
            logOut();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        SharedPreferences preferences = getSharedPreferences("Cadastro", MODE_PRIVATE);
        preferences.edit().clear().commit();
        LoginManager login = LoginManager.getInstance();
        login.logOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private void mudarFragment(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .replace(R.id.frame_fragment, fragment)
                .commit();
    }

    public void getUser() {
        SharedPreferences prefs = getSharedPreferences("Cadastro", MODE_PRIVATE);
        nome = prefs.getString("nome", "NOME");
        email = prefs.getString("email", "email@email");
        fotoUser = prefs.getString("foto", "partiufacu,com/icone.png");
    }

    public View.OnClickListener onCLickPerfil() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mudarFragment(new PerfilDetalhesActivityFragment());
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        };
    }

}
