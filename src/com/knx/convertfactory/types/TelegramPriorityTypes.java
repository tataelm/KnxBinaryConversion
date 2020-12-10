package com.knx.convertfactory.types;

public enum TelegramPriorityTypes {
    SYSTEM {
        public String toString() {
            return "00";
        }
    },

    URGENT {
        public String toString() {
            return "10";
        }
    },

    NORMAL {
        public String toString() {
            return "01";
        }
    },

    LOW {
        public String toString() {
            return "11";
        }
    },
}
