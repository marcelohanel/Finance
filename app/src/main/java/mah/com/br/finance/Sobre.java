package mah.com.br.finance;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class Sobre extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public Sobre() {
    }

    public static Sobre newInstance(int sectionNumber) {

        Sobre fragment = new Sobre();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sobre, container, false);

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

        EditText edtVersao = (EditText) rootView.findViewById(R.id.edtVersao);

        edtVersao.setText(Funcoes.getVersionName(getActivity()));

        return rootView;
    }
}
