package com.nullberg.modemtalk;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.util.List;


public class CellInfMgr {

    public String cellInfTypeReg;

    public int regidx, cisize, ci, eNBid, pci, getpci, earfcn, band, bw, dB, MCC, MNC, TAC;


    private List<CellInfo> cellInfoList;
    private CellInfoLte lteCell;
    public CellIdentityLte lteCellID;
    public int[] bandlist;

    public List<String> cellInfStringList;

    CellInfMgr(Context context, TelephonyManager tm) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
                cellInfoList = tm.getAllCellInfo(); // No lint warning here
        }

        cisize         = cellInfoList.size();
        regidx         = getRegIdx();
        cellInfTypeReg = getCellInfType( cellInfoList.get(regidx) );

        CellInfo cellInf = cellInfoList.get(regidx);


        if (cellInf instanceof CellInfoLte) {

            lteCell   = (CellInfoLte) cellInf;
            lteCellID = lteCell.getCellIdentity();
            ci        = lteCellID.getCi(); // get Cell Identity (CI)
            eNBid     = ci >> 8;   // eNB ID (upper 20 bits)
            pci       = ci & 0xFF; // PCI (lower 8 bits)
            getpci    = lteCellID.getPci();
            earfcn    = lteCellID.getEarfcn();
            band      = EarfcnMapper.getBandFromEarfcn(earfcn);
            dB        = lteCell.getCellSignalStrength().getDbm();

            MCC = lteCellID.getMcc();
            MNC = lteCellID.getMnc();
            TAC = lteCellID.getTac();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                bandlist  = lteCellID.getBands();
                bw        = lteCellID.getBandwidth();
                // note, bw ~ 15000   (I guess it's 15000 kHz ;  i.e. 15 MHz)
            }
        }
    }


    private int getRegIdx() {
        int idx_registered=-1;

        for (int i=0; i<cellInfoList.size(); i++) {
            if ( cellInfoList.get(i).isRegistered() ) {
                idx_registered = i;
            }
        }
        return idx_registered;
    }


    private String getCellInfType(CellInfo ci) {

        if (ci instanceof CellInfoGsm) {
            return "GSM";
        } else if (ci instanceof CellInfoCdma) {
            return "CDMA";
        } else if (ci instanceof CellInfoWcdma) {
            return "WCDMA (UMTS)";
        } else if (ci instanceof CellInfoLte ) {
            return "LTE";
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (ci instanceof CellInfoNr) {
                return "NR";
            } else if (ci instanceof CellInfoTdscdma) {
                return "TD-SCDMA";
            }
        } else {
            return "Unable to find cellInfType";
        }
        return null;
    }

    private String getRegisteredCellInfType() {

        int idx_registered=-1;

        for (int i=0; i<cellInfoList.size(); i++) {
            if ( cellInfoList.get(i).isRegistered() ) {
                idx_registered = i;
            }
        }

        CellInfo cellInfReg = cellInfoList.get(idx_registered);

        if (cellInfReg instanceof CellInfoLte) {
            return "LTE";
        } else if ( cellInfReg instanceof CellInfoGsm) {
            return "GSM";
        } else {
            return "Could NOT resolve Gen";
        }
    }


}
