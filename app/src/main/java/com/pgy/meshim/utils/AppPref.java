package com.pgy.meshim.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by admin on 2018/1/4.
 */

public class AppPref {
    public final static String KEY_APP_LANGUAGE = "key_app_language";
    public final static String KEY_CURRENCY_TYPE = "key_currency_type";
    public final static String NETWORKLESS_STATUS = "networkless_status";
    public final static String KEY_ETH_WALLET_ADDRESS = "key_eth_wallet_address";
    private final static String APP_PREF = "app_preferences";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public AppPref(Context context) {
        sp = context.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public String getWalletAddress() {
        return sp.getString(KEY_ETH_WALLET_ADDRESS, null);
    }

    public void setWalletAddress(String walletAddress) {
        if (TextUtils.isEmpty(walletAddress)) {
            editor.remove(KEY_ETH_WALLET_ADDRESS);
        } else {
            editor.putString(KEY_ETH_WALLET_ADDRESS, walletAddress);
        }
        editor.commit();
    }

    public int getCurrencyType() {
        return sp.getInt(KEY_CURRENCY_TYPE, -1);
    }

    public void setCurrencyType(int type) {
        if (type <= 0) {
            editor.remove(KEY_CURRENCY_TYPE);
        } else {
            editor.putInt(KEY_CURRENCY_TYPE, type);
        }
        editor.commit();
    }

    public boolean getNetworklessStatus() {
        return sp.getBoolean(NETWORKLESS_STATUS, true);
    }

    public void setNetworklessStatus(boolean status) {
        editor.putBoolean(NETWORKLESS_STATUS, status);
        editor.commit();
    }

}
