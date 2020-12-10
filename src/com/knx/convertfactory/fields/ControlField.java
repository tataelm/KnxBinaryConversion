package com.knx.convertfactory.fields;

import com.knx.convertfactory.helpers.HelpMethods;
import com.knx.convertfactory.types.TelegramPriorityTypes;

public class ControlField {

    private boolean isStandardBit;
    private boolean isRepeatedBit;
    private TelegramPriorityTypes telegramPriorityTypes = TelegramPriorityTypes.LOW;

    private String result;
    public boolean isValid = true;

    /**
     * This constructor called while creating a Communication Object
     * */
    public ControlField(boolean isStandart, boolean isRepeated, TelegramPriorityTypes telegramPriorityTypes)
    {
        String isStandartBit = isStandart ? "1" : "0";
        String isRepeatedBit = isRepeated ? "0" : "1";
        String telegramPriorityBit = telegramPriorityTypes.toString();
        result = isStandartBit + "0" + isRepeatedBit + "1" + telegramPriorityBit + "00";
    }

    /**
     * This constructor is called while raw byte to Communication Object conversion
     * */
    public ControlField(Byte inputByte)
    {
        int inputInt = inputByte & 0xFF;
        // We need 8 bits
        String inputString = HelpMethods.intToBinaryWith8Bits(inputInt);
        analyseBinary(inputString);
    }


    /**
     * A Control Field has a certain pattern such as X0X1 XX00
     * Whereas Xs differ the type of Control Field
     * */
    private void analyseBinary(String inputString) {
        try {
            char[] binary = inputString.toCharArray();

            if (binary[1] != '0' || binary[3] != '1' || binary[6] != '0' || binary[7] != '0')
            {
                isValid = false;
                return;
            }

            isStandardBit = binary[0] == '1';
            isRepeatedBit = binary[2] == '0';

            String telegramPriorityBinary  = String.valueOf(binary[4]) + String.valueOf(binary[5]);

            for (TelegramPriorityTypes telegram: TelegramPriorityTypes.values())
            {
                if (telegram.toString().equals(telegramPriorityBinary))
                {
                    telegramPriorityTypes = telegram;
                }
            }
        }
        catch (Exception e)
        {
            // TODO may not be a standard control field
        }
    }

    public String getControlFieldHex()
    {
        if (result != null)
        {
            return result;
        }
        else
        {
            String isStandartBitST = isStandardBit ? "1" : "0";
            String isRepeatedBitST = isRepeatedBit ? "0" : "1";
            String telegramPriorityBit = telegramPriorityTypes.toString();
            return  isStandartBitST + "0" + isRepeatedBitST + "1" + telegramPriorityBit + "00";
        }
    }

    public boolean isStandardBit() {
        return isStandardBit;
    }

    public boolean isRepeatedBit() {
        return isRepeatedBit;
    }

    public TelegramPriorityTypes getTelegramPriorityTypes() {
        return telegramPriorityTypes;
    }
}
