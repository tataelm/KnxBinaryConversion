package com.knx.convertfactory.helpers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class HelpMethods {

    /**
     * Splits a long hexadecimal string into 2 digits string list
     *
     * @param inputHex takes hexadecimal string as input
     * @return list of hexadecimal numbers with 2 digits
     */
    public static List<String> getListHexIn2Digits(String inputHex) {
        List<String> parts = new ArrayList<>();
        int len = inputHex.length();
        for (int i=0; i<len; i+=2)
        {
            parts.add(inputHex.substring(i, Math.min(len, i + 2)));
        }
        return parts;
    }

    /**
     * Converts integer to binary and fills
     * up to 8 bits
     * */
    public static String intToBinaryWith8Bits(int n)
    {
        String binary = "";
        while (n > 0)
        {
            binary =  ( (n % 2 ) == 0 ? "0" : "1") + binary;
            n = n / 2;
        }

        while (binary.length() < 8)
        {
            binary = "0" + binary;
        }

        return binary;
    }

    /**
     * Converts integer to binary
     * */
    public static String intToBinary(int n) {
        String binary = "";
        while (n > 0)
        {
            binary =  ( (n % 2 ) == 0 ? "0" : "1") + binary;
            n = n / 2;
        }
        return binary;
    }

    /**
     * Converts 2 digits hexadecimal string input into a single byte
     *
     * @param hexString takes a string hexadecimal as input
     * @return a single byte, converted from input
     * */
    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit) ;
    }

    /**
     * Takes a single hexadecimal character and converts it into
     * its integer value
     *
     * @param hexChar is a single hexadecimal char
     * @return integer value of hex char
     * */
    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    /**
     * Forces the inputHex to have 4 digits by
     * adding 0 at the beginning
     *
     * @param inputHex takes hexadecimal string as input
     * @return hexadecimal with 4 digits
     */
    public static String getHexIn4Digits(String inputHex) {
        while (inputHex.length() < 4)
        {
            inputHex = "0" + inputHex;
        }
        return inputHex;
    }

    /**
     * Returns hexadecimal numbers in 2 digits
     * if they have only 1
     * */
    public static String getHexIn2Digits(String inputHex) {
        while (inputHex.length() < 2)
        {
            inputHex = "0" + inputHex;
        }
        return inputHex;
    }

    /**
     * Converts binary string to hexadecimal string
     *
     * @param binaryStr takes binary string as input
     * @return hexadecimal string
     */
    public static String binaryToHex(String binaryStr)
    {
        int decimal = Integer.parseInt(binaryStr,2);
        return Integer.toString(decimal,16);
    }

    /**
     * Converts binary to decimal
     * */
    public static int binaryToDecimal(String binaryString)
    {
        return Integer.parseInt(binaryString,2);
    }

    /**
     * Converts hexadecimal string to binary
     * */
    public static String hexToBinary(String s) {
        return new BigInteger(s, 16).toString(2);
    }

    /**
     * Converts decimal number to hexadecimal string
     * */
    public static String decimalToHex(int decimal)
    {
        return Integer.toString(decimal,16);
    }


    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    /**
     * Converts byte array to hexadecimal string
     * */
    public static String byteArrayToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Converts byte array to binary string
     * */
    public static String byteArrayToBinary( byte[] bytes )
    {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for( int i = 0; i < Byte.SIZE * bytes.length; i++ )
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    /**
     * Converts a single byte to binary string
     * */
    public static String byteToBinary(byte b)
    {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }

    /**
     * Converts a single byte to hexadecimal string
     * */
    public static String byteToHex(byte b)
    {
        return binaryToHex(byteToBinary(b));
    }

    /**
     * Converts list of bytes to hexadecimal string
     * */
    public static String listByteToHex(List<Byte> listByte) {
        byte[] arr = new byte[listByte.size()];

        for (int i = 0; i < listByte.size(); i++)
        {
            arr[i] = listByte.get(i);
        }
        return byteArrayToHex(arr);
    }
}
