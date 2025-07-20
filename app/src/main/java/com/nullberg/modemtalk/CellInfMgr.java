package com.nullberg.modemtalk;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.util.List;


public class CellInfMgr {

    public String cellInfType;

    public int ci, eNBid, pci, earfcn, bw;

    private List<CellInfo> cellInfoList;
    private CellInfoLte lteCell;
    public CellIdentityLte lteCellID;
    public int[] bandlist;

    CellInfMgr(Context context, TelephonyManager tm) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            cellInfoList = tm.getAllCellInfo(); // No lint warning here
        }

        cellInfType = getCellInfType();

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
    }


    private String getCellInfType() {

        int idx_registered;

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
