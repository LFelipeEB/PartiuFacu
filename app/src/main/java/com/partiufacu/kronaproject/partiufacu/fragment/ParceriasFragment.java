package com.partiufacu.kronaproject.partiufacu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.partiufacu.kronaproject.partiufacu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParceriasFragment extends Fragment {

    private FragmentManager mFragmentManager;
    private AdView adView1, adView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_parcerias, container, false);
        ImageView parceiro1 = (ImageView) layout.findViewById(R.id.parceiro1);
        parceiro1.setImageResource(R.drawable.logo_fxnet);

        adView1 = (AdView) layout.findViewById(R.id.adView1);
        AdRequest requestAd = new AdRequest.Builder()
                .addTestDevice("C75A3936043C50E2249FE286CB93E554")
                .build();
        adView1.loadAd(requestAd);

        Button btnBusca = (Button) layout.findViewById(R.id.btnBuscaParcerias);
        btnBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mFragmentManager = getFragmentManager();
                mFragmentManager.beginTransaction()
                        .replace(R.id.frame_fragment, new BuscarCaronasFragment())
                        .commit();
            }
        });

        Button btnAdd = (Button) layout.findViewById(R.id.btnDivulgaParcerias);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager mFragmentManager = getFragmentManager();
                mFragmentManager.beginTransaction()
                        .replace(R.id.frame_fragment, new AdicionarCaronaFragment())
                        .commit();
            }
        });

        return layout;
    }

}
