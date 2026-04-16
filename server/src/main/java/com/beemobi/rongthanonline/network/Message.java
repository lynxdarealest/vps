package com.beemobi.rongthanonline.network;

import java.io.*;

public class Message {

    public byte id;
    private ByteArrayOutputStream os;
    private DataOutputStream dos;
    private ByteArrayInputStream is;
    public DataInputStream dis;

    public Message(int command) {
        this((byte) command);
    }

    public Message(byte command) {
        this.id = command;
        os = new ByteArrayOutputStream();
        dos = new DataOutputStream(os);
    }

    public Message(byte command, byte[] data) {
        this.id = command;
        is = new ByteArrayInputStream(data);
        dis = new DataInputStream(is);
    }

    public byte getId() {
        return id;
    }

    public void setId(int cmd) {
        setId((byte) cmd);
    }

    public void setId(byte cmd) {
        id = cmd;
    }

    public byte[] getData() {
        return os.toByteArray();
    }

    public DataInputStream reader() {
        return dis;
    }

    public DataOutputStream writer() {
        return dos;
    }

    public void cleanup() {
        try {
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
        } catch (IOException e) {
        }
    }

}
