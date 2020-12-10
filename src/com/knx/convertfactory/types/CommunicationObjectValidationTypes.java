package com.knx.convertfactory.types;

public enum CommunicationObjectValidationTypes {
    NONE,
    INCOMPLETE,
    DEFAULT,
    PositiveConfirm {
        public String toString() {
            return "8B";
        }
    },
    NegativeConfirm {
        public String toString() {
            return "0B";
        }
    },
    NegativeAcknowledge {
        public String toString() {
            return "0C";
        }
    },
    BusyAcknowledge {
        public String toString() {
            return "C0";
        }
    },
    Acknowledge {
        public String toString() {
            return "CC";
        }
    },
    StateResponse_TODO {
        public String toString() {
            return "17";
        }
    },
    ReadRegister_Request {
        public String toString() {
            return "2E";
        }
    },
}
