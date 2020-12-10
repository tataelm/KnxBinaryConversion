package com.knx.convertfactory.types;

public enum AddressType
{
    SINGLE_DEVICE{
        public String toString() {
            return "0";
        }
    },
    MULTIPLE_DEVICE {
        public String toString() {
            return "1";
        }
    },
}
