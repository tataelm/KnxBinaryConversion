package com.knx.convertfactory.fields;

import com.knx.convertfactory.helpers.HelpMethods;
import com.knx.convertfactory.types.ExpectedField;
import com.knx.convertfactory.types.PayloadTypes;

public class Payload {

    private final PayloadTypes payloadTypes;
    private String data;
    private final String dataLength;
    private String result = "";

    private ExpectedField EXPECTED;

    /**
     * This constructor called while creating a Communication Object
     * */
    public Payload(PayloadTypes payloadTypes, String data, String dataLength)
    {
        this.payloadTypes = payloadTypes;
        this.data = data;
        this.dataLength = dataLength;

        calculatePayload();
    }

    /**
     * This constructor is called while raw byte to Communication Object conversion
     * */
    public Payload(Byte aByte, int length) {
        int inputValue = Byte.toUnsignedInt(aByte);

        int payloadValue = inputValue >> 4;
        switch (payloadValue)
        {
            case 0:
                payloadTypes = PayloadTypes.Read;
                break;

            case 4:
                payloadTypes = PayloadTypes.Response;
                break;

            default:
            case 8:
                payloadTypes = PayloadTypes.Write;
                break;

            //default:
            //throw new Exception("Unknown payload type"); TODO handle this
        }

        dataLength = String.valueOf(length);

        int commandData = 0;

        if (length > 1)
        {
            EXPECTED = ExpectedField.PAYLOAD_EXTENSION;
        }
        else
        {
            EXPECTED = ExpectedField.CRC;
            commandData = inputValue ^ Integer.parseInt(payloadTypes.toString(), 16);

        }
        data = String.valueOf(commandData);
        data = HelpMethods.getHexIn2Digits(data);
    }

    /**
     * Calculates payload by its length.
     *
     * If it is bigger than 0, it adds next to PayloadType. e.g. 8041
     * Else, it ORs with the PayloadType. e.g. 81
     * */
    private void calculatePayload()
    {
        if (Integer.parseInt(dataLength) > 0)
        {
            result = payloadTypes.toString() + data;
        }
        else
        {
            int iData = Integer.parseInt(data, 16);
            int iPayload = Integer.parseInt(payloadTypes.toString());
            result = getHexIn2Digits(String.valueOf(iData | iPayload));
        }
    }

    /**
     * Forces the inputHex to have 2 digits by
     * adding 0 at the beginning
     *
     * @param inputHex takes hexadecimal string as input
     * @return hexadecimal with 2 digits
     */
    private String getHexIn2Digits(String inputHex) {
        while (inputHex.length() < 2)
        {
            inputHex = "0" + inputHex;
        }
        return inputHex;
    }

    public String getDataLength()
    {
        return dataLength;
    }

    public String getHex() {
        return result;
    }

    public String getData() {
        return data;
    }

    public ExpectedField getExpectedExtension() {
        return EXPECTED;
    }

    public void addToData(Byte aByte) {
        String hex = HelpMethods.byteToHex(aByte);
        data = data + hex;
    }

    public PayloadTypes getPayloadTypes() {
        return payloadTypes;
    }
}

