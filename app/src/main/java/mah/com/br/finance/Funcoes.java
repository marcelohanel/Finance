package mah.com.br.finance;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;

import com.google.android.gms.ads.AdRequest;

class Funcoes {

    static AdRequest adRequest;

    static String getVersionName(Context context) {
        try {
            ComponentName comp = new ComponentName(context, context.getClass());
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
            return pinfo.versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    static String formataCampo(String campo) {

        try {

            if (!campo.isEmpty()) {
                if (campo.contains(",") & campo.contains(".")) {
                    campo = campo.replaceAll("[.]", "").replaceAll("[,]", ".");
                } else {
                    if (campo.contains(",")) {
                        campo = campo.replaceAll("[,]", ".");
                    }
                }
            } else {
                campo = "0.00";
            }

            return campo;

        } catch (Exception e) {
            return "0.00";
        }
    }

}
