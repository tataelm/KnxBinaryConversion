package com.knx.convertfactory;

import com.knx.convertfactory.helpers.HelpMethods;
import com.knx.convertfactory.helpers.RawDataException;
import com.knx.convertfactory.types.RawDataExceptionType;

import java.util.List;

class ConvertFactory {

    /**
     * Converts human input for destination address
     * to hexadecimal
     *
     * @param groupAddress takes String in x/x/x format
     * @return destination address in hexadecimal
     */
    public String convertDestinationAddressToHex(String groupAddress) {
        String[] adrArr = groupAddress.split("/");

        int a1 = Integer.parseInt(adrArr[0]);
        int a2 = Integer.parseInt(adrArr[1]);
        int a3 = Integer.parseInt(adrArr[2]);

        int tmp1 = (a1 << 11) | (a2 << 8) | a3;

        return HelpMethods.getHexIn4Digits(Integer.toHexString(tmp1));
    }

    /**
     * Converts human input for source address
     * to hexadecimal
     *
     * @param individualAddress takes String in x.x.x format
     * @return source address in hexadecimal
     */
    public String convertSourceAddressToHex(String individualAddress)
    {
        String[] adrArr = individualAddress.split("\\.");

        String hex1 = "";
        String hex2 = "";
        String hex3 = "";

        hex1 = Integer.toHexString(Integer.parseInt(adrArr[0]));
        hex2 = Integer.toHexString(Integer.parseInt(adrArr[1]));
        hex3 = Integer.toHexString(Integer.parseInt(adrArr[2]));

        if (hex3.length() == 1)
        {
            hex3 = "0" + hex3;
        }

        return HelpMethods.getHexIn4Digits(hex1+hex2+hex3);
    }

    /**
     * Calculates CRC hexadecimal with given parameters
     * such as control field, source address, destination address,
     * address type, hop count, length, payload
     *
     * @param inputHex takes hexadecimal string as input
     * @return CRC in hexadecimal
     */
    public String calculateCRC(String inputHex)
    {
        List<String> hexIn2Digits = HelpMethods.getListHexIn2Digits(inputHex);
        hexIn2Digits.add(0,"FF");
        return xorHex(hexIn2Digits);
    }

    /**
     * This helps calculateCRC method
     * by XORing all given elements in the listHex
     *
     * @param listHex takes hexadecimal list string as input
     * @return 2 digit hexadecimal string
     */
    private String xorHex(List<String> listHex) {
        int result = 0;

        for (String hex : listHex) {
            result = result ^ Integer.parseInt(hex, 16);
        }
        return String.format("%02x", result);
    }

    /**
     * Validates the source and/or destination address, whether the format
     * is valid or not.
     *
     * Valid format should be x/x/x or x.x.x
     *
     * @param addressType decides if "address" is source or destination address
     * @param address string input for destination or source address
     * @return validated address
     * @throws RawDataException throws only when the format cannot be validated
     */
    public String validateAddress(RawDataExceptionType addressType, String address) throws RawDataException {
        boolean containsDot = address.contains(".");
        boolean containsSlash = address.contains("/");
        boolean dotCount2 = address.length() - address.replace(".", "").length() == 2;
        boolean slashCount2 = address.length() - address.replace("/", "").length() == 2;

        if ((!containsDot && !containsSlash) || (!dotCount2 && !slashCount2)) {
            throw new RawDataException(addressType.toString(), "Wrong format. Please write it as 1.1.1 or 1/1/1");
        }

        if (addressType.equals(RawDataExceptionType.DESTINATION_ADDRESS) && containsDot)
        {
            return address.replaceAll("\\.", "/");
        }
        else if(addressType.equals(RawDataExceptionType.SOURCE_ADDRESS) && containsSlash)
        {
            return address.replaceAll("/", "\\.");
        }
        else
        {
            return address;
        }
    }

    /**
     * This method combines and converts 2 raw bytes into hexadecimal source address
     *
     * @param byte1 is the first byte of source address
     * @param byte2 is the second byte of source address
     *
     * @return 2 bytes of hexadecimal string
     * */
    public String convertBinaryToSourceAddress(Byte byte1, Byte byte2) {
        byte[] byteArray = new byte[2];
        byteArray[0] = byte1;
        byteArray[1] = byte2;

        String sourceInHex = HelpMethods.byteArrayToHex(byteArray);
        char[] sourceCharArr = sourceInHex.toCharArray();

        int s1 = Integer.parseInt(String.valueOf(sourceCharArr[0]), 16);
        int s2 = Integer.parseInt(String.valueOf(sourceCharArr[1]), 16);
        int s3 = Integer.parseInt("" + sourceCharArr[2] + sourceCharArr[3], 16);

        return s1 + "." + s2 + "." + s3;
    }

    /**
     * This method combines and converts 2 raw bytes into hexadecimal destination address
     * after doing necessary shifting operations
     *
     * @param byte1 is the first byte of destination address
     * @param byte2 is the second byte of destination address
     *
     * @return 2 bytes of hexadecimal string
     * */
    public String convertBinaryToDestinationAddress(Byte byte1, Byte byte2) {
        byte[] byteArray = new byte[2];
        byteArray[0] = byte1;
        byteArray[1] = byte2;

        String hex = HelpMethods.byteArrayToHex(byteArray);
        char[] destinationCharArr = hex.toCharArray();

        String left = "" + destinationCharArr[0] + destinationCharArr[1];
        String right = "" + destinationCharArr[2] + destinationCharArr[3];

        int iLeft = Integer.parseInt(left, 16);
        int leftValue = iLeft >> 3;
        int resTemp = leftValue << 3;
        int middleValue = iLeft - resTemp;
        int rightValue = Integer.parseInt(right, 16);

        return leftValue + "/" + middleValue + "/" + rightValue;
    }
}
