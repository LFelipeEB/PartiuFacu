package com.partiufacu.kronaproject.partiufacu.fragment;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.midi.MidiOutputPort;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.partiufacu.kronaproject.partiufacu.R;
import com.partiufacu.kronaproject.partiufacu.interfaces.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdicionarCaronaFragment extends Fragment {

    private RequestQueue rq;
    private Map<String, String> parms;
    private int idUser;
    private String localSaida, localChegada;
    private EditText nome, preco, vagas, obs;
    private Button dia, hora, enviaCarona;
    private Time time;
    private Date date;
    private Spinner saidaSp, chegadaSp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        idUser = Integer.parseInt(this.getActivity().getSharedPreferences("Cadastro", Context.MODE_PRIVATE).getString("id", "0"));
        View layout = inflater.inflate(R.layout.fragment_adicionar_carona, container, false);

        rq = Volley.newRequestQueue(getContext());

        nome = (EditText) layout.findViewById(R.id.edNome);
        preco = (EditText) layout.findViewById(R.id.edPreco);
        vagas = (EditText) layout.findViewById(R.id.edVagas);
        obs = (EditText) layout.findViewById(R.id.obsAdd);

        dia = (Button) layout.findViewById(R.id.addDia);
        dia.setOnClickListener(dataPick(dia));

        hora = (Button) layout.findViewById(R.id.addHora);
        hora.setOnClickListener(timePick(hora));

        saidaSp = (Spinner) layout.findViewById(R.id.spinnerSaida);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.locais_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saidaSp.setAdapter(adapter);
        saidaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                localSaida = adapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        chegadaSp = (Spinner) layout.findViewById(R.id.spinnerChegada);
        chegadaSp.setAdapter(adapter);
        chegadaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                localChegada = adapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        enviaCarona = (Button) layout.findViewById(R.id.btnCadastrar);
        enviaCarona.setOnClickListener(enviarCarona());

        parms = new HashMap<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Atenção");
        builder.setMessage("As caronas postadas neste app deve ter caracter solidadrios e quem oferece não deve aferir lucro, cobrem apenas os gastos dividos pelo numero de passageiros!");
        builder.setPositiveButton("Ok, entendi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Não concordo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                android.support.v4.app.FragmentManager mFragmentManager = getFragmentManager();
                mFragmentManager.beginTransaction()
                        .replace(R.id.frame_fragment, new ParceriasFragment())
                        .commit();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        return layout;
    }

    public View.OnClickListener enviarCarona() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(nome.getText().toString().isEmpty() ||
                        vagas.getText().toString().isEmpty() ||
                        date.toString().isEmpty() ||
                        time.toString().isEmpty() ||
                        (localChegada == localSaida)
                        ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Está faltando algo, volte e confira ...");
                    builder.setTitle("Atenção");
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    parms.put("opc", "1");
                    parms.put("id", Integer.toString(idUser));
                    parms.put("nome", nome.getText().toString());
                    parms.put("vagas", vagas.getText().toString());
                    parms.put("local-saida", localSaida);
                    parms.put("local-chegada", localChegada);
                    parms.put("data", date.toString());
                    parms.put("preco", preco.getText().toString());
                    parms.put("horario", time.toString());
                    parms.put("obs", obs.getText().toString());

                    StringRequest request = new StringRequest(Request.Method.POST, Constantes.insere, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("Inserir", response);
                            JSONObject jobj = null;
                            try {
                                jobj = new JSONObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                Snackbar.make(v, jobj.getString("result"), Snackbar.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            limpaCampos();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Inserir", "Erro: " + error.getMessage());
                        }
                    }) {
                        @Override
                        public Map<String, String> getParams() {
                            return parms;
                        }
                    };

                    rq.add(request);
                }
            }
        };
    }

    public void limpaCampos(){
        nome.setText("");
        vagas.setText("");
        preco.setText("");
        dia.setText("XX / XX / XXXX");
        date = null;
        hora.setText("XX : XX");
        hora = null;
        obs.setText("");

    }

    public View.OnClickListener dataPick(final Button btn) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarDatePickerDialogFragment cdpdf = new CalendarDatePickerDialogFragment()
                        .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                            @Override
                            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                                date = Date.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                btn.setText(dayOfMonth+" / "+ (monthOfYear+1)+" / "+ year);
                            }
                        })
                        .setFirstDayOfWeek(Calendar.SUNDAY);
                cdpdf.show(getFragmentManager(), "DATA PICKER");
            }
        };
    }

    public View.OnClickListener timePick(final Button btn) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadialTimePickerDialogFragment rtpdf = new RadialTimePickerDialogFragment()
                        .setOnTimeSetListener(new RadialTimePickerDialogFragment.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
                                time = Time.valueOf(hourOfDay + ":" + minute + ":00");
                                btn.setText(hourOfDay+" : "+minute);
                            }
                        });
                rtpdf.show(getFragmentManager(), "TIME PICKER");
            }
        };
    }
}

