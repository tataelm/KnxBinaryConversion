package com.knx.convertfactory.types;

public enum RawDataExceptionType {
    SOURCE_ADDRESS {
        public String toString() {
            return "Invalid source address: ";
        }

        ;
    },

    DESTINATION_ADDRESS {
        public String toString() {
            return "Invalid destination address: ";
        }

        ;
    }
}
