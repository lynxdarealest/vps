package com.beemobi.rongthanonline.model.input;

public class TextField {
    public static int TYPE_NORMAL = 0;
    public static int TYPE_PASSWORD = 1;
    public static int TYPE_NUMBER = 2;
    public static int TYPE_USERNAME = 3;

    public String name;
    public int type;

    public TextField() {

    }

    public TextField(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public TextField(String name) {
        this.name = name;
        this.type = 0;
    }
}
