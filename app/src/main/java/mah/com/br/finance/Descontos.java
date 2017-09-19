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

@SuppressWarnings("StringEquality")
public class Descontos extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private Spinner cbxTipoCalculo;
    private Spinner cbxTipoDesconto;

    private EditText edtValorNominal;
    private EditText edtTaxa;
    private EditText edtTempo;
    private EditText edtValorDesconto;
    private EditText edtValorCalculado;

    public Descontos() {
    }

    public static Descontos newInstance(int sectionNumber) {

        Descontos fragment = new Descontos();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.descontos, container, false);

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

        cbxTipoCalculo = (Spinner) rootView.findViewById(R.id.cbxTipoCalculo);
        cbxTipoDesconto = (Spinner) rootView.findViewById(R.id.cbxTipoDesconto);

        edtValorNominal = (EditText) rootView.findViewById(R.id.edtValorNominal);
        edtTaxa = (EditText) rootView.findViewById(R.id.edtTaxaJuros);
        edtTempo = (EditText) rootView.findViewById(R.id.edtTempo);
        edtValorDesconto = (EditText) rootView.findViewById(R.id.edtValorDesconto);
        edtValorCalculado = (EditText) rootView.findViewById(R.id.edtValorCalculado);

        Button btnLimpar = (Button) rootView.findViewById(R.id.btnLimpar);
        Button btnCalcular = (Button) rootView.findViewById(R.id.btnCalcular);

        String[] lisTipoCalculo = {getString(R.string.desconto_simples), getString(R.string.desconto_composto)};
        ArrayAdapter<String> dataAdapterTipoCalculo = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lisTipoCalculo);
        dataAdapterTipoCalculo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cbxTipoCalculo.setAdapter(dataAdapterTipoCalculo);

        String[] lisTipoDesconto = {getString(R.string.desconto_comercial), getString(R.string.desconto_racional)};
        ArrayAdapter<String> dataAdapterTipoDesconto = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lisTipoDesconto);
        dataAdapterTipoDesconto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cbxTipoDesconto.setAdapter(dataAdapterTipoDesconto);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                calcula();

                getActivity();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cbxTipoCalculo.getWindowToken(), 0);
            }
        });

        btnLimpar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                edtValorNominal.setText("");
                edtTaxa.setText("");
                edtTempo.setText("");
                edtValorDesconto.setText("");
                edtValorCalculado.setText("");

                getActivity();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cbxTipoCalculo.getWindowToken(), 0);
            }
        });

        return rootView;
    }

    private void calcula() {

        Integer iTipoCalculo = 0;
        Integer iTipoDesconto = 0;
        Integer iTipo = 0;

        if (cbxTipoCalculo.getSelectedItem().toString() == getString(R.string.desconto_simples))
            iTipoCalculo = 1;
        else if (cbxTipoCalculo.getSelectedItem().toString() == getString(R.string.desconto_composto))
            iTipoCalculo = 2;

        if (cbxTipoDesconto.getSelectedItem().toString() == getString(R.string.desconto_comercial))
            iTipoDesconto = 1;
        else if (cbxTipoDesconto.getSelectedItem().toString() == getString(R.string.desconto_racional))
            iTipoDesconto = 2;

        double valorNominal;
        double taxa;
        double tempo;
        double valorDesconto;
        double valorCalculado;

        valorNominal = Double.parseDouble(Funcoes.formataCampo(edtValorNominal.getText().toString()));
        taxa = Double.parseDouble(Funcoes.formataCampo(edtTaxa.getText().toString()));
        tempo = Double.parseDouble(Funcoes.formataCampo(edtTempo.getText().toString()));
        valorCalculado = Double.parseDouble(Funcoes.formataCampo(edtValorCalculado.getText().toString()));
        valorDesconto = Double.parseDouble(Funcoes.formataCampo(edtValorDesconto.getText().toString()));

        if ((valorNominal != 0) &
                (tempo != 0) &
                (taxa != 0))
            iTipo = 1;
        else if ((iTipoCalculo == 1) &
                (valorCalculado != 0) &
                (valorNominal != 0) &
                (tempo != 0))
            iTipo = 2;
        else if ((iTipoCalculo == 1) &
                (valorCalculado != 0) &
                (valorNominal != 0) &
                (taxa != 0))
            iTipo = 3;

        calcDescontos(iTipo, iTipoCalculo, iTipoDesconto, valorNominal, taxa, tempo, valorDesconto, valorCalculado);
    }

    @SuppressLint("DefaultLocale")
    private void calcDescontos(Integer iTipo, Integer iTipoCalculo, Integer iTipoDesconto, double valorNominal, double taxa, double tempo, double valorDesconto, double valorCalculado) {

        try {
            if (iTipo == 1) { // Cálculo do desconto
                if (iTipoCalculo == 1) { // Desconto Simples
                    if (iTipoDesconto == 1) { // Comercial
                        valorDesconto = (valorNominal * taxa / 100) * tempo;
                        valorCalculado = valorNominal - valorDesconto;
                    } else if (iTipoDesconto == 2) { // Racional
                        valorDesconto = (valorNominal * tempo * (taxa / 100)) / (1 + (taxa / 100) * tempo);
                        valorCalculado = valorNominal - valorDesconto;
                    }
                } else if (iTipoCalculo == 2) { // Desconto Composto
                    if (iTipoDesconto == 1) { // Comercial
                        valorDesconto = valorNominal * (1 - (Math.pow((1 - (taxa / 100)), tempo)));
                        valorCalculado = valorNominal - valorDesconto;
                    } else if (iTipoDesconto == 2) { // Racional
                        valorDesconto = valorNominal * (1 - (Math.pow((1 + (taxa / 100)), (tempo * -1))));
                        valorCalculado = valorNominal - valorDesconto;
                    }
                }
            } else if (iTipo == 2) { // taxa
                if (iTipoCalculo == 1) { // Desconto Simples
                    if (iTipoDesconto == 1) { // Comercial
                        taxa = (valorNominal - valorCalculado) / (valorNominal * tempo);
                        taxa = taxa * 100;
                        valorDesconto = valorNominal - valorCalculado;
                    } else if (iTipoDesconto == 2) { // Racional
                        taxa = ((valorNominal / valorCalculado) - 1) / tempo;
                        taxa = taxa * 100;
                        valorDesconto = valorNominal - valorCalculado;
                    }
                }
            } else if (iTipo == 3) { // tempo
                if (iTipoCalculo == 1) { // Desconto Simples
                    if (iTipoDesconto == 1) { // Comercial
                        tempo = (valorNominal - valorCalculado) / (valorNominal * (taxa / 100));
                        valorDesconto = valorNominal - valorCalculado;
                    } else if (iTipoDesconto == 2) { // Racional
                        tempo = ((valorNominal / valorCalculado) - 1) / (taxa / 100);
                        valorDesconto = valorNominal - valorCalculado;
                    }
                }
            }

            edtValorNominal.setText(String.format("%,.2f", valorNominal));
            edtTaxa.setText(String.format("%,.2f", taxa));
            edtTempo.setText(String.format("%,.0f", tempo));
            edtValorDesconto.setText(String.format("%,.2f", valorDesconto));
            edtValorCalculado.setText(String.format("%,.2f", valorCalculado));

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
