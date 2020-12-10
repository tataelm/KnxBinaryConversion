package com.knx.convertfactory.fields;

import com.knx.convertfactory.helpers.HelpMethods;
import com.knx.convertfactory.helpers.RawDataException;
import com.knx.convertfactory.types.AddressType;

public class AddressHopLength {

    private String destinationAddress;
    private final AddressType addressType;
    private String hopCount;
    private int length;

    // TODO currently this is accepted as only 00
    final private String extension = "00";

    /**
     * This constructor called while creating a Communication Object
     * */
    public AddressHopLength(AddressType addressType)
    {
        this.addressType = addressType;
    }

    /**
     * This constructor is called while raw byte to Communication Object conversion
     * */
    public AddressHopLength(Byte b1, Byte b2)
    {
        byte[] bArray = new byte[1];
        bArray[0] = b1;

        // TODO currently this is accepted as only 00
        //bArray[1] = b2; //00 extension

        String adrHopLen = HelpMethods.byteToBinary(b1);
        char[] adrHopLenArray = adrHopLen.toCharArray();

        addressType = adrHopLenArray[0] == '1' ? AddressType.MULTIPLE_DEVICE : AddressType.SINGLE_DEVICE;
        hopCount = "" + adrHopLenArray[1] + adrHopLenArray[2] + adrHopLenArray[3];

        length = HelpMethods.binaryToDecimal("" + adrHopLenArray[4] + adrHopLenArray[5] + adrHopLenArray[6] + adrHopLenArray[7]);
    }

    public void setDestinationAddress(String _destinationAddress) {
        this.destinationAddress = _destinationAddress;
    }

    public void startCalculations() throws Exception
    {
        validation();
        assignHopCount();
    }

    /**
     * Assigns hop count with following conditions:
     *
     * if AddressType is single, hop count is 111
     * if AddressType is multiple and destination address 0000, hop count is 111
     * if AddressType is multiple and destination address's different than 0000, hop count is 110
     * */
    private void assignHopCount()
    {
        if (addressType.equals(AddressType.SINGLE_DEVICE))
        {
            hopCount = "111";
        }
        else if(destinationAddress.equals("0000"))
        {
            hopCount = "111";
        }
        else
        {
            hopCount = "110";
        }
    }

    /**
     * Validates if the communication is valid
     *
     * @throws RawDataException when destinationAddress=="0000" and AddressType is SINGLE_DEVICE
     * */
    private void validation() throws Exception {
        if (destinationAddress.equals("0000") && addressType.equals(AddressType.SINGLE_DEVICE))
        {
            throw new Exception("INVALID COMMUNICATION");
        }
    }

    /***
     * @param dataLength takes the dataLength in string
     * @return hex value for address-hop-length field
     * */
    public String getHexValueOfAddressHopLength(String dataLength) {
        dataLength = String.valueOf(Integer.parseInt(dataLength) + 1);
        String firstDigit = addressType.toString() + hopCount;
        int decimal = HelpMethods.binaryToDecimal(firstDigit);
        return HelpMethods.decimalToHex(decimal) + dataLength + extension;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public String getHopCount() {
        return hopCount;
    }

    public int getLength() {
        return length;
    }
}


