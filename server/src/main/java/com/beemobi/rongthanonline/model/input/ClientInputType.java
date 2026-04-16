package com.beemobi.rongthanonline.model.input;

public class ClientInputType {
    public static final int INPUT_CREATE_CLAN = 0;
    public static final int INPUT_TANG_KIM_CUONG = 1;
    public static final int INPUT_SET_DISCIPLE_NAME = 2;
    public static final int INPUT_CREATE_DHVT = 3;
    public static final int INPUT_TRAO_THUONG_DHVT = 4;
    public static final int INPUT_TRAO_THUONG_SINH_TON = 5;
    public static final int INPUT_GIFT_CODE = 6;
    public static final int INPUT_DOI_TEN = 7;
    public static final int INPUT_EVENT = 8;
    public static final int INPUT_LOI_DAI = 9;
    public static final int INPUT_DOI_TEN_DAC_BIET = 10;
    public static final int INPUT_SET_DISCIPLE_NAME_DAC_BIET = 11;
    public static final int NHAP_MA_BAO_VE = 12;
    public static final int DOI_MA_BAO_VE = 13;

    public int type;

    public Object[] elements;

    public ClientInputType(int type) {
        this.type = type;
    }
}
