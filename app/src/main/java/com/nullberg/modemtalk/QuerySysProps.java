package com.nullberg.modemtalk;

import android.telephony.TelephonyManager;

import java.util.ArrayList;

public class QuerySysProps {

    public ArrayList<String> syspropList = new ArrayList<>();

    public String syspropLbls, syspropVals;


    public QuerySysProps() {

        syspropList.add("ro.product.model");
        syspropList.add("ro.product.brand");
        syspropList.add("ro.product.manufacturer");
        syspropList.add("ro.product.device");
        syspropList.add("ro.product.name");
        syspropList.add("ro.build.id");
        syspropList.add("ro.build.version.release");
        syspropList.add("ro.build.version.sdk");
        syspropList.add("ro.build.fingerprint");
        syspropList.add("ro.bootloader");
        syspropList.add("ro.boot.hardware");
        syspropList.add("ro.boot.serialno");
        syspropList.add("ro.build.type");
        syspropList.add("ro.build.tags");
        syspropList.add("ro.secure");
        syspropList.add("ro.debuggable");
        syspropList.add("ro.hardware");
        syspropList.add("ro.board.platform");
        syspropList.add("ro.serialno");
        syspropList.add("ro.chipname");


        //        syspropList.add("");

        syspropList.add("gsm.version.baseband");
        syspropList.add("gsm.network.type");
        syspropList.add("gsm.operator.alpha"); // name of current operator
        syspropList.add("gsm.operator.iso-country");
        syspropList.add("gsm.operator.numeric");
        syspropList.add("gsm.sim.operator.alpha");
        syspropList.add("gsm.sim.operator.numeric");
        syspropList.add("gsm.sim.state");
        syspropList.add("gsm.version.ril-impl");
        syspropList.add("gsm.network.type");
//        syspropList.add("telephony.lteOnCdmaDevice");

        this.syspropLbls = getSysPropLbls(syspropList);
        this.syspropVals = getSysPropVals(syspropList);

    }

    private String getSysPropLbls(ArrayList<String> inlist) {

        String[] lblarr = new String[inlist.size()];

        for (int i=0; i < inlist.size(); i++) {
            lblarr[i] = inlist.get(i);
        }

        return String.join("\n", lblarr);

    }

    private String getSysPropVals(ArrayList<String> inlist) {

        String[] roArrVals = new String[inlist.size()];

        for (int i=0; i < inlist.size(); i++) {
            roArrVals[i] = getSysProp(inlist.get(i));
        }

        return String.join("\n", roArrVals);

    }

    private String getSysProp(String strInput) {

        String retVal;

        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            java.lang.reflect.Method getMethod = systemProperties.getMethod("get", String.class);

            retVal = (String) getMethod.invoke(null, strInput);

            if (retVal.isEmpty()) {
                retVal = "<empty>";
            }

            return retVal;

        } catch (Exception e) {
            e.printStackTrace();
            return "Unknown";
        }

    }

}
