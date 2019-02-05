package com.github.manevolent.jbot.property;

import java.util.Date;

public interface Property {
    String getName();

    String getString();
    boolean getBoolean();
    byte getByte();
    short getShort();
    int getInteger();
    long getLong();
    float getFloat();
    char getChar();
    double getDouble();
    Date getDate();
    byte[] getBytes();

    void set(String s);
    void set(boolean b);
    void set(byte b);
    void set(short s);
    void set(int i);
    void set(long l);
    void set(float f);
    void set(double d);
    void set(char c);
    void set(Date date);
    void set(byte[] data);

    void unset();

    boolean isNull();

    default Property ensure(boolean b) {
        if (isNull()) set(b);
        return this;
    }

    default Property ensure(double d) {
        if (isNull()) set(d);
        return this;
    }

    default Property ensure(String s) {
        if (isNull()) set(s);
        return this;
    }

    default Property ensure(byte b) {
        if (isNull()) set(b);
        return this;
    }

    default Property ensure(int i) {
        if (isNull()) set(i);
        return this;
    }

    default Property ensure(long l) {
        if (isNull()) set(l);
        return this;
    }

    default Property ensure(short s) {
        if (isNull()) set(s);
        return this;
    }

    default Property ensure(Date d) {
        if (isNull()) set(d);
        return this;
    }

    default Property ensure(float f) {
        if (isNull()) set(f);
        return this;
    }

}
