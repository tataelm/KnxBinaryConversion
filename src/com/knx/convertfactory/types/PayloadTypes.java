package com.knx.convertfactory.types;

public enum PayloadTypes {

    Read {
        public String toString() {
            return "00";
        }
    },
    Response {
        public String toString() {
            return "40";
        }
    },

    Write {
        public String toString() {
            return "80";
        }
    },

}
