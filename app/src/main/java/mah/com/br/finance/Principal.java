package mah.com.br.finance;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_principal);

        Funcoes.adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return Juros.newInstance(position + 1);
                case 1:
                    return Descontos.newInstance(position + 1);
                case 2:
                    return Financiamentos.newInstance(position + 1);
                case 3:
                    return Porcentagem.newInstance(position + 1);
                case 4:
                    return Sobre.newInstance(position + 1);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
