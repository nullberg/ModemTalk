package com.nullberg.modemtalk;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import android.telephony.TelephonyManager;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;


import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QueryTelephony {

    public ArrayList<String> labels = new ArrayList<>();
    public ArrayList<String> values = new ArrayList<>();
    public String labelStr, valueStr;

    public QueryTelephony(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try {

            List<CellInfo>  cellInfoList  = null;
            CellInfoLte     lteCell       = null;
            CellIdentityLte lteCellID     = null;
            int[] bandlist                = null;

            int ci     = -1;
            int eNBid  = -1;
            int pci    = -1;
            int earfcn = -1;
            int bw     = -1; // bandwidth

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                cellInfoList = tm.getAllCellInfo(); // No lint warning here
            }

            addTMquery("cellInf.size()", String.valueOf(cellInfoList.size()));

            for (CellInfo cellInf : cellInfoList) {
                if (cellInf instanceof CellInfoLte && cellInf.isRegistered()) {
                    lteCell   = (CellInfoLte) cellInf;
                    lteCellID = lteCell.getCellIdentity();
                    ci        = lteCellID.getCi(); // get Cell Identity (CI)
                    eNBid     = ci >> 8;   // eNB ID (upper 20 bits)
                    pci       = ci & 0xFF; // PCI (lower 8 bits)
                    earfcn    = lteCellID.getEarfcn();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        bandlist  = lteCellID.getBands();
                        bw        = lteCellID.getBandwidth();
                        // note, bw ~ 15000   (I guess it's 15000 kHz ;  i.e. 15 MHz)
                    }

                }
            }

            if (lteCell != null) {
                addTMquery("EARFCN",       tostr( earfcn ));
                addTMquery("Band",         tostr( EarfcnMapper.getBandFromEarfcn(earfcn) ));
                addTMquery("getBands",     intArrToStr( bandlist ));
                addTMquery("getCi()",      tostr( ci ));
                addTMquery("eNB ID",       tostr( eNBid ));
                addTMquery("pci from Ci",  tostr( pci ));
                addTMquery("getPci()",     tostr( lteCellID.getPci() ));
                addTMquery("dB (LTE)",     tostr( lteCell.getCellSignalStrength().getDbm()));
                addTMquery("Bandwidth",    tostr( bw ));
                addTMquery("MCC (mobile country code)",   tostr( lteCellID.getMcc() ));
                addTMquery("MNC (mobile network code)",   tostr( lteCellID.getMnc() ));
                addTMquery("TAC (tracking area code)",    tostr( lteCellID.getTac() ));
            }

            // ARFCN = Absolute Radio Frequency Channel Number

            // EARFCN = E-UTRA ARFCN
            // E-UTRA = Evolved UMTS Terrestrial Radio Access

            // UARFCN = UTRA ARFCN
            // → Used for UMTS/WCDMA (3G)

            // ARFCN = ARFCN
            // → Used for GSM (2G)

        } catch (Exception e) {
            addTMquery("ALL LTE QUERIES", "<error>");
        }


        try {
            addTMquery("Device Software Version", tm.getDeviceSoftwareVersion() );
        } catch (Exception e) {
            addTMquery("Device Software Version", "<error>");
        }

        try {
            addTMquery("Network Operator Name", tm.getNetworkOperatorName() );
        } catch (Exception e) {
            addTMquery("Network Operator Name", "<error>");
        }

        try {
            addTMquery("SIM Operator Name", tm.getSimOperatorName());
        } catch (Exception e) {
            addTMquery("SIM Operator Name", "<error>");
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

    private boolean permissionGrantedAccessFineLocation(Context ctxt) {

        return ActivityCompat.checkSelfPermission(ctxt, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private String intArrToStr(int[] arr) {
        return Arrays.stream(arr).mapToObj(String::valueOf).collect(Collectors.joining(","));
    }

    private <T> String tostr(T genInput) {
        return String.valueOf(genInput);
    }


}
