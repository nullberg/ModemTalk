package com.nullberg.modemtalk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class QueryTelephony {

    public ArrayList<String> labels = new ArrayList<>();
    public ArrayList<String> values = new ArrayList<>();
    public String labelStr, valueStr;

    public QueryTelephony(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            addTMquery("Device Software Version", tm.getDeviceSoftwareVersion());
        } catch (Exception e) {
            addTMquery("Device Software Version", "<error>");
        }

        try {
            addTMquery("Network Operator Name", tm.getNetworkOperatorName());
        } catch (Exception e) {
            addTMquery("Network Operator Name", "<error>");
        }

        try {
            addTMquery("SIM Operator Name", tm.getSimOperatorName());
        } catch (Exception e) {
            addTMquery("SIM Operator Name", "<error>");
        }

        try {
            addTMquery("Line 1 Number", tm.getLine1Number()); // <-- often throws
        } catch (Exception e) {
            addTMquery("Line 1 Number", "<error>");
        }

        try {
            addTMquery("Network Country ISO", tm.getNetworkCountryIso());
        } catch (Exception e) {
            addTMquery("Network Country ISO", "<error>");
        }

        try {
            addTMquery("SIM Country ISO", tm.getSimCountryIso());
        } catch (Exception e) {
            addTMquery("SIM Country ISO", "<error>");
        }

        try {
            addTMquery("Phone Type", getPhoneTypeString(tm.getPhoneType()));
        } catch (Exception e) {
            addTMquery("Phone Type", "<error>");
        }

        try {
            addTMquery("SIM State", getSimStateString(tm.getSimState()));
        } catch (Exception e) {
            addTMquery("SIM State", "<error>");
        }

        // Combine into newline-separated strings (optional)
        labelStr = String.join("\n", labels);
        valueStr = String.join("\n", values);
    }

    private void addTMquery(String label, String value) {
        labels.add(label);
        values.add((value != null && !value.isEmpty()) ? value : "<empty>");
    }

    private String getPhoneTypeString(int type) {
        switch (type) {
            case TelephonyManager.PHONE_TYPE_CDMA: return "CDMA";
            case TelephonyManager.PHONE_TYPE_GSM:  return "GSM";
            case TelephonyManager.PHONE_TYPE_SIP:  return "SIP";
            case TelephonyManager.PHONE_TYPE_NONE: return "None";
            default: return "Unknown";
        }
    }

    private String getSimStateString(int state) {
        switch (state) {
            case TelephonyManager.SIM_STATE_ABSENT: return "Absent";
            case TelephonyManager.SIM_STATE_READY: return "Ready";
            case TelephonyManager.SIM_STATE_PIN_REQUIRED: return "PIN Required";
            case TelephonyManager.SIM_STATE_PUK_REQUIRED: return "PUK Required";
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED: return "Network Locked";
            case TelephonyManager.SIM_STATE_UNKNOWN: return "Unknown";
            default: return "Other";
        }
    }
}
