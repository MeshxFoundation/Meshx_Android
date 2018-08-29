package com.pgy.meshim.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * Created by burning on 2018/7/27.
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * -------------------------//┏┓　　　┏┓
 * -------------------------//┏┛┻━━━┛┻┓
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┃　　　━　　　┃
 * -------------------------//┃　┳┛　┗┳　┃
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┃　　　┻　　　┃
 * -------------------------//┃　　　　　　　┃
 * -------------------------//┗━┓　　　┏━┛
 * -------------------------//┃　　　┃  神兽保佑
 * -------------------------//┃　　　┃  代码无BUG！
 * -------------------------//┃　　　┗━━━┓
 * -------------------------//┃　　　　　　　┣┓
 * -------------------------//┃　　　　　　　┏┛
 * -------------------------//┗┓┓┏━┳┓┏┛
 * -------------------------// ┃┫┫　┃┫┫
 * -------------------------// ┗┻┛　┗┻┛
 */
public class ResiurceWraUtils {
    public final static int LANGUAGE_SN = 2;
    public final static int LANGUAGE_CN = 1;
    public final static int LANGUAGE_EN = 0;
    public static String LocaleSpKey = "LocaleSpKey";
    public static String SpKey = "MESHX";

    public static ContextWrapper wrapForChinese(Context context) {
        return wrap(context, Locale.CHINESE);
    }

    public static ContextWrapper wrap(Context context, Locale newLocale) {
        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.setLocale(newLocale);
            context = context.createConfigurationContext(configuration);

        }
        return new ContextWrapper(context);
    }

    public static ContextWrapper wrapForSp(Context context) {
        int type = getLanguageType(context);
        switch (type) {
            case LANGUAGE_CN:
                return wrap(context, Locale.SIMPLIFIED_CHINESE);
            case LANGUAGE_SN:
                return wrap(context, Locale.forLanguageTag("es-ES"));
            default:
                return wrap(context, Locale.ENGLISH);
        }
    }

    public static int getLanguageType(Context context) {
        int type = context.getSharedPreferences(SpKey, 0).getInt(LocaleSpKey, -1);
        if (type == -1) {
            Locale language = context.getResources().getConfiguration().locale;
            if (language.equals(Locale.SIMPLIFIED_CHINESE))
                return LANGUAGE_CN;
            else if (language.equals(Locale.ENGLISH))
                return LANGUAGE_EN;
            else
                return LANGUAGE_SN;
        }
        return type;
    }

    public static boolean setLanguage(Context context, int i) {
        return context.getSharedPreferences(SpKey, 0).edit().putInt(LocaleSpKey, i).
                commit();
    }
}
