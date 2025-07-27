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

import java.util.ArrayList;
import java.util.List;


public class CellInfMgr {

    public String cellInfTypeReg;

    public int regidx, cisize, ci, eNBid, pci, getpci, earfcn, band, bw, dB, MCC, MNC, TAC;

    public int[] bandlist;
    public List<String> cellInfAll = new ArrayList<>();

    private List<CellInfo> cellInfoList;
    private CellInfo cellInfReg;

    CellInfMgr(Context context, TelephonyManager tm) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            cellInfoList = tm.getAllCellInfo(); // No lint warning here
        }

        cisize = cellInfoList.size();
        regidx = getRegIdx();
        cellInfTypeReg = getCellInfType(cellInfoList.get(regidx));

        cellInfReg = cellInfoList.get(regidx);

        for (CellInfo cellInf : cellInfoList) {
            cellInfAll.add( getCellInfType( cellInf ) );
        }


        populateParamsMaster();

    }


    private int getRegIdx() {
        int idx_registered = -1;

        for (int i = 0; i < cellInfoList.size(); i++) {
            if (cellInfoList.get(i).isRegistered()) {
                idx_registered = i;
            }
        }
        return idx_registered;
    }


    private String getCellInfType(CellInfo cellInf) {

        if (cellInf instanceof CellInfoGsm) {
            return "GSM";
        } else if (cellInf instanceof CellInfoCdma) {
            return "CDMA";
        } else if (cellInf instanceof CellInfoWcdma) {
            return "WCDMA (UMTS)";
        } else if (cellInf instanceof CellInfoLte) {
            return "LTE";
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (cellInf instanceof CellInfoNr) {
                return "NR";
            } else if (cellInf instanceof CellInfoTdscdma) {
                return "TD-SCDMA";
            }
        } else {
            return "Unable to find cellInfType";
        }
        return null;
    }

    private String getRegisteredCellInfType() {

        int idx_registered = -1;

        for (int i = 0; i < cellInfoList.size(); i++) {
            if (cellInfoList.get(i).isRegistered()) {
                idx_registered = i;
            }
        }

        CellInfo cellInfReg = cellInfoList.get(idx_registered);

        if (cellInfReg instanceof CellInfoLte) {
            return "LTE";
        } else if (cellInfReg instanceof CellInfoGsm) {
            return "GSM";
        } else {
            return "Could NOT resolve Gen";
        }
    }


    private void populateParamsMaster() {

        if (cellInfReg instanceof CellInfoLte) {

            CellInfoLte     lteCell   = (CellInfoLte) cellInfReg;
            CellIdentityLte lteCellID = lteCell.getCellIdentity();
            ci = lteCellID.getCi(); // get Cell Identity (CI)
            eNBid = ci >> 8;   // eNB ID (upper 20 bits)
            pci = ci & 0xFF; // PCI (lower 8 bits)
            getpci = lteCellID.getPci();
            earfcn = lteCellID.getEarfcn();
            band = EarfcnMapper.getBandFromEarfcn(earfcn);
            dB = lteCell.getCellSignalStrength().getDbm();

            MCC = lteCellID.getMcc();
            MNC = lteCellID.getMnc();
            TAC = lteCellID.getTac();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                bandlist = lteCellID.getBands();
                bw = lteCellID.getBandwidth();
                // note, bw ~ 15000   (I guess it's 15000 kHz ;  i.e. 15 MHz)
            }
        }

    }


}
