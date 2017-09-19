package mah.com.br.finance;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Juros extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText edtValorPresente;
    private EditText edtTaxaJuros;
    private EditText edtNumeroParcelas;
    private EditText edtValorFuturo;
    private EditText edtValorJuros;
    private EditText edtValorParcela;

    private Spinner cbxJuros;

    public Juros() {
    }

    public static Juros newInstance(int sectionNumber) {

        Juros fragment = new Juros();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.juros, container, false);

        AdView adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.banner));

        LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.banner_layout);
        if (layout != null) {
            try {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                adView.setLayoutParams(layoutParams);
                layout.addView(adView);
                adView.loadAd(Funcoes.adRequest);
            } catch (Exception ignored) {
            }
        }

        cbxJuros = (Spinner) rootView.findViewById(R.id.cbxTipoCalculo);

        edtValorPresente = (EditText) rootView.findViewById(R.id.edtValorPresente);
        edtTaxaJuros = (EditText) rootView.findViewById(R.id.edtTaxaJuros);
        edtNumeroParcelas = (EditText) rootView.findViewById(R.id.edtNumeroParcelas);
        edtValorFuturo = (EditText) rootView.findViewById(R.id.edtValorFuturo);
        edtValorJuros = (EditText) rootView.findViewById(R.id.edtValorJuros);
        edtValorParcela = (EditText) rootView.findViewById(R.id.edtValorParcela);

        Button btnLimpar = (Button) rootView.findViewById(R.id.btnLimpar);
        Button btnCalcular = (Button) rootView.findViewById(R.id.btnCalcular);

        String[] lista = {getString(R.string.juros_compostos), getString(R.string.juros_simples)};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lista);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cbxJuros.setAdapter(dataAdapter);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calcula();

                getActivity();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cbxJuros.getWindowToken(), 0);
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edtValorPresente.setText("");
                edtTaxaJuros.setText("");
                edtNumeroParcelas.setText("");
                edtValorFuturo.setText("");
                edtValorJuros.setText("");
                edtValorParcela.setText("");

                getActivity();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cbxJuros.getWindowToken(), 0);
            }
        });

        return rootView;
    }

    private void calcula() {

        Integer iTipo = 0;
        Integer iCalculo = 1;

        //noinspection StringEquality
        if (cbxJuros.getSelectedItem().toString() == getString(R.string.juros_simples))
            iCalculo = 1;
        else //noinspection StringEquality
            if (cbxJuros.getSelectedItem().toString() == getString(R.string.juros_compostos))
                iCalculo = 2;

        double vp;
        double i;
        double n;
        double vf;
        double vj;
        double p;

        vp = Double.parseDouble(Funcoes.formataCampo(edtValorPresente.getText().toString()));
        i = Double.parseDouble(Funcoes.formataCampo(edtTaxaJuros.getText().toString()));
        n = Double.parseDouble(Funcoes.formataCampo(edtNumeroParcelas.getText().toString()));
        vf = Double.parseDouble(Funcoes.formataCampo(edtValorFuturo.getText().toString()));
        vj = Double.parseDouble(Funcoes.formataCampo(edtValorJuros.getText().toString()));
        p = Double.parseDouble(Funcoes.formataCampo(edtValorParcela.getText().toString()));

        if ((vp != 0) & (n != 0) & (i != 0)) iTipo = 1;
        else if ((vp != 0) & (n != 0) & (vj != 0)) iTipo = 2;
        else if ((vp != 0) & (n != 0) & (p != 0)) iTipo = 3;
        else if ((vf != 0) & (n != 0) & (i != 0)) iTipo = 4;
        else if ((vf != 0) & (n != 0) & (vj != 0)) iTipo = 5;
        else if ((vp != 0) & (vf != 0) & (n != 0)) iTipo = 7;
        else if ((vp != 0) & (vf != 0) & (p != 0)) iTipo = 8;
        else if ((vp != 0) & (p != 0)) iTipo = 9;
        else if ((n != 0) & (vj != 0) & (p != 0)) iTipo = 10;

        calcJuros(iTipo, iCalculo, vp, i, n, vf, vj, p);
    }

    @SuppressLint("DefaultLocale")
    private void calcJuros(Integer tipo, Integer calculo, double vp, double i, double n, double vf, double vj, double p) {

        try {

            if (tipo == 1) {

                if (calculo == 1) {
                    vf = vp + (vp / 100 * n * i);
                } else {
                    vf = vp * Math.pow((1 + i / 100), n);
                }

                vj = vf - vp;
                p = vf / n;
            }

            if (tipo == 2) {

                vf = vp + vj;
                p = vf / n;

                if (calculo == 1) {
                    i = vj * 100 / vp / n;
                } else {
                    i = (Math.pow((vf / vp), (1 / n)) - 1) * 100;
                }
            }

            if (tipo == 3) {

                vf = n * p;
                vj = vf - vp;

                if (calculo == 1) {
                    i = vj * 100 / vp / n;
                } else {
                    i = (Math.pow((vf / vp), (1 / n)) - 1) * 100;
                }
            }

            if (tipo == 4) {

                p = vf / n;

                if (calculo == 1) {
                    vp = vf / (100 + (i * n)) * 100;
                } else {
                    vp = vf / (Math.pow(1 + (i / 100), n));
                }

                vj = vf - vp;
            }

            if (tipo == 5) {

                p = vf / n;
                vp = vf - vj;

                if (calculo == 1) {
                    i = vj * 100 / vp / n;
                } else {
                    i = (Math.pow((vf / vp), (1 / n)) - 1) * 100;
                }
            }

            if (tipo == 6) {

                vj = vf - vp;

                if (calculo == 1) {
                    i = vj * 100 / vp / n;
                } else {
                    i = (Math.pow((vf / vp), (1 / n)) - 1) * 100;
                }
            }

            if (tipo == 7) {

                vj = vf - vp;
                p = vf / n;

                if (calculo == 1) {
                    i = vj * 100 / vp / n;
                } else {
                    i = (Math.pow((vf / vp), (1 / n)) - 1) * 100;
                }
            }

            if (tipo == 8) {

                vj = vf - vp;
                n = vf / p;

                if (calculo == 1) {
                    i = vj * 100 / vp / n;
                } else {
                    i = (Math.pow((vf / vp), (1 / n)) - 1) * 100;
                }
            }

            if (tipo == 9) {

                vf = vp + vj;
                n = vf / p;

                if (calculo == 1) {
                    i = vj * 100 / vp / n;
                } else {
                    i = (Math.pow((vf / vp), (1 / n)) - 1) * 100;
                }
            }

            if (tipo == 10) {

                vf = n * p;
                vp = vf - vj;

                if (calculo == 1) {
                    i = vj * 100 / vp / n;
                } else {
                    i = (Math.pow((vf / vp), (1 / n)) - 1) * 100;
                }
            }

            edtValorPresente.setText(String.format("%,.2f", vp));
            edtTaxaJuros.setText(String.format("%,.2f", i));
            edtNumeroParcelas.setText(String.format("%,.0f", n));
            edtValorFuturo.setText(String.format("%,.2f", vf));
            edtValorJuros.setText(String.format("%,.2f", vj));
            edtValorParcela.setText(String.format("%,.2f", p));

        } catch (Exception ex) {
            AlertDialog.Builder dlgAlert;
            dlgAlert = new AlertDialog.Builder(getActivity());

            dlgAlert.setMessage(getString(R.string.message_1));
            dlgAlert.setTitle(getString(R.string.title_message_1));
            dlgAlert.setPositiveButton("OK", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }
}
