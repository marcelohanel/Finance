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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Porcentagem extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private EditText edtValor;
    private EditText edtPercentual;
    private EditText edtResultado;
    private EditText edtValorAcrescimo;
    private EditText edtValorDesconto;

    public Porcentagem() {
    }

    public static Porcentagem newInstance(int sectionNumber) {

        Porcentagem fragment = new Porcentagem();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.porcentagem, container, false);

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

        edtValor = (EditText) rootView.findViewById(R.id.edtValor);
        edtPercentual = (EditText) rootView.findViewById(R.id.edtPercentual);
        edtResultado = (EditText) rootView.findViewById(R.id.edtResultado);
        edtValorAcrescimo = (EditText) rootView.findViewById(R.id.edtValorAcrescimo);
        edtValorDesconto = (EditText) rootView.findViewById(R.id.edtValorDesconto);

        Button btnLimpar = (Button) rootView.findViewById(R.id.btnLimpar);
        Button btnCalcular = (Button) rootView.findViewById(R.id.btnCalcular);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calcula();

                getActivity();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtValor.getWindowToken(), 0);
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edtValor.setText("");
                edtPercentual.setText("");
                edtResultado.setText("");
                edtValorAcrescimo.setText("");
                edtValorDesconto.setText("");

                getActivity();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtValor.getWindowToken(), 0);
            }
        });

        return rootView;
    }

    @SuppressLint("DefaultLocale")
    private void calcula() {

        double vlrValor;
        double vlrPercentual;
        double vlrResultado;
        double vlrValorAcrescimo;
        double vlrValorDesconto;

        vlrValor = Double.parseDouble(Funcoes.formataCampo(edtValor.getText().toString()));
        vlrPercentual = Double.parseDouble(Funcoes.formataCampo(edtPercentual.getText().toString()));

        try {
            vlrResultado = (vlrValor / 100) * vlrPercentual;
            vlrValorAcrescimo = vlrValor + vlrResultado;
            vlrValorDesconto = vlrValor - vlrResultado;

            edtValor.setText(String.format("%,.2f", vlrValor));
            edtPercentual.setText(String.format("%,.2f", vlrPercentual));
            edtResultado.setText(String.format("%,.2f", vlrResultado));
            edtValorAcrescimo.setText(String.format("%,.2f", vlrValorAcrescimo));
            edtValorDesconto.setText(String.format("%,.2f", vlrValorDesconto));

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
