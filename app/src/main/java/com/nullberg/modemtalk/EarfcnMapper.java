package com.nullberg.modemtalk;

public class EarfcnMapper {

    /**
     * Returns the LTE band number corresponding to the given EARFCN.
     * Returns -1 if the EARFCN does not match any known band.
     */

    public static int getBandFromEarfcn(int earfcn) {
        if (earfcn >= 0 && earfcn <= 599) return 1;
        if (earfcn >= 1200 && earfcn <= 1949) return 3;
        if (earfcn >= 2750 && earfcn <= 3449) return 7;
        if (earfcn >= 3450 && earfcn <= 3799) return 8;
        if (earfcn >= 6150 && earfcn <= 6449) return 20;
        if (earfcn >= 9210 && earfcn <= 9659) return 28;
        if (earfcn >= 37750 && earfcn <= 38249) return 38;
        if (earfcn >= 38650 && earfcn <= 39649) return 40;
        if (earfcn >= 39650 && earfcn <= 41589) return 41;
        if (earfcn >= 66436 && earfcn <= 67335) return 66;

        // Unknown or unsupported band
        return -1;
    }

    private EarfcnMapper() {
        // Prevent instantiation
    }

}
