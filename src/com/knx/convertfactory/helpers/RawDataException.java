package com.knx.convertfactory.helpers;

public class RawDataException extends Exception{

    public RawDataException(String type, String message) {
        super(type + message);
    }
}


