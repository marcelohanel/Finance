package mah.com.br.finance;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
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
import android.widget.TextView;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Financiamentos extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private Spinner cbxTipoCalculo;

    private EditText edtValorFinanciado;
    private EditText edtTaxa;
    private EditText edtParcelas;
    private EditText edtValorFuturo;
    private EditText edtValorJuros;
    private EditText edtValorParcela;

    private LinearLayout lstParcelas;

    public Financiamentos() {
    }

    public static Financiamentos newInstance(int sectionNumber) {

        Financiamentos fragment = new Financiamentos();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.financiamentos, container, false);

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
        edtValorFinanciado = (EditText) rootView.findViewById(R.id.edtValorFinanciado);
        edtTaxa = (EditText) rootView.findViewById(R.id.edtTaxaJuros);
        edtParcelas = (EditText) rootView.findViewById(R.id.edtParcelas);
        edtValorFuturo = (EditText) rootView.findViewById(R.id.edtValorFuturo);
        edtValorJuros = (EditText) rootView.findViewById(R.id.edtValorJuros);
        edtValorParcela = (EditText) rootView.findViewById(R.id.edtValorParcela);

        Button btnLimpar = (Button) rootView.findViewById(R.id.btnLimpar);
        Button btnCalcular = (Button) rootView.findViewById(R.id.btnCalcular);

        lstParcelas = (LinearLayout) rootView.findViewById(R.id.lstParcelas);

        lstParcelas.removeAllViews();

        String[] lisTipoCalculo = {getString(R.string.tabela_sac), getString(R.string.tabela_price)};
        ArrayAdapter<String> dataAdapterTipoCalculo = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, lisTipoCalculo);
        dataAdapterTipoCalculo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cbxTipoCalculo.setAdapter(dataAdapterTipoCalculo);

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

                lstParcelas.removeAllViews();

                edtValorFinanciado.setText("");
                edtTaxa.setText("");
                edtParcelas.setText("");
                edtValorFuturo.setText("");
                edtValorJuros.setText("");
                edtValorParcela.setText("");

                getActivity();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cbxTipoCalculo.getWindowToken(), 0);
            }
        });

        return rootView;
    }

    private void calcula() {

        Integer iTipoCalculo = 0;

        //noinspection StringEquality
        if (cbxTipoCalculo.getSelectedItem().toString() == getString(R.string.tabela_sac))
            iTipoCalculo = 1;
        else //noinspection StringEquality
            if (cbxTipoCalculo.getSelectedItem().toString() == getString(R.string.tabela_price))
                iTipoCalculo = 2;

        double valorFinanciado;
        double taxa;
        double parcelas;
        double valorFuturo = Double.parseDouble("0.00");
        double valorJuros = Double.parseDouble("0.00");
        double valorParcela = Double.parseDouble("0.00");

        valorFinanciado = Double.parseDouble(Funcoes.formataCampo(edtValorFinanciado.getText().toString()));
        taxa = Double.parseDouble(Funcoes.formataCampo(edtTaxa.getText().toString()));
        parcelas = Double.parseDouble(Funcoes.formataCampo(edtParcelas.getText().toString()));

        calcFinanciamentos(iTipoCalculo, valorFinanciado, taxa, parcelas, valorFuturo, valorJuros, valorParcela);
    }

    @SuppressLint("DefaultLocale")
    private void calcFinanciamentos(Integer iTipoCalculo, double valorFinanciado, double taxa, double parcelas, double valorFuturo, double valorJuros, double valorParcela) {

        try {

            double amortizacao = 0;
            double saldo = valorFinanciado;
            double juro;
            double pagto = 0;

            if (iTipoCalculo == 1) { // SAC
                amortizacao = saldo / parcelas;
            } else {
                if (iTipoCalculo == 2) { // PRICE

                    double aux;
                    double aux1;
                    double aux2;
                    double aux3;

                    aux = Math.pow((1 + (taxa / 100)), parcelas);
                    aux1 = aux * (taxa / 100);
                    aux2 = aux - 1;
                    aux3 = (aux1 / aux2);
                    pagto = valorFinanciado * aux3;
                }
            }

            lstParcelas.removeAllViews();

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            TextView lblAux = new TextView(getActivity());
            lblAux.setText(getString(R.string.label_parcelamento));

            if (Build.VERSION.SDK_INT < 23) {
                //noinspection deprecation
                lblAux.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            } else {
                lblAux.setTextAppearance(android.R.style.TextAppearance_Medium);
            }

            lblAux.setLayoutParams(layoutParams);
            lstParcelas.addView(lblAux);

            TextView lblAux7 = new TextView(getActivity());
            lblAux7.setText("");
            lblAux7.setLayoutParams(layoutParams);
            lstParcelas.addView(lblAux7);

            for (int iCont = 1; iCont <= parcelas; iCont++) {

                juro = saldo * taxa / 100;

                if (iTipoCalculo == 1) { // SAC
                    pagto = amortizacao + juro;
                } else if (iTipoCalculo == 2) { // PRICE
                    amortizacao = pagto - juro;
                }

                saldo = saldo - amortizacao;

                if (iTipoCalculo == 1) { // SAC
                    if (iCont == 1) {
                        valorParcela = pagto;
                    }
                } else if (iTipoCalculo == 2) { // PRICE
                    valorParcela = pagto;
                }

                valorJuros = valorJuros + juro;
                valorFuturo = valorJuros + valorFinanciado;

                TextView lblAux1 = new TextView(getActivity());
                lblAux1.setText(getString(R.string.numero_parcela) + " " + Integer.toString(iCont));
                lblAux1.setLayoutParams(layoutParams);
                lstParcelas.addView(lblAux1);

                TextView lblAux2 = new TextView(getActivity());
                lblAux2.setText(getString(R.string.valor_saldo) + " " + String.format("%,.2f", saldo));
                lblAux2.setLayoutParams(layoutParams);
                lstParcelas.addView(lblAux2);

                TextView lblAux3 = new TextView(getActivity());
                lblAux3.setText(getString(R.string.valor_amortizacao) + " " + String.format("%,.2f", amortizacao));
                lblAux3.setLayoutParams(layoutParams);
                lstParcelas.addView(lblAux3);

                TextView lblAux4 = new TextView(getActivity());
                lblAux4.setText(getString(R.string.valor_juros) + " " + String.format("%,.2f", juro));
                lblAux4.setLayoutParams(layoutParams);
                lstParcelas.addView(lblAux4);

                TextView lblAux5 = new TextView(getActivity());
                lblAux5.setText(getString(R.string.valor_parcela) + " " + String.format("%,.2f", pagto));
                lblAux5.setLayoutParams(layoutParams);
                lstParcelas.addView(lblAux5);

                TextView lblAux6 = new TextView(getActivity());
                lblAux6.setText("");
                lblAux6.setLayoutParams(layoutParams);
                lstParcelas.addView(lblAux6);
            }

            edtValorFinanciado.setText(String.format("%,.2f", valorFinanciado));
            edtTaxa.setText(String.format("%,.2f", taxa));
            edtParcelas.setText(String.format("%,.0f", parcelas));
            edtValorFuturo.setText(String.format("%,.2f", valorFuturo));
            edtValorJuros.setText(String.format("%,.2f", valorJuros));
            edtValorParcela.setText(String.format("%,.2f", valorParcela));

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
