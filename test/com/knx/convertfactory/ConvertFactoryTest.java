package com.knx.convertfactory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConvertFactoryTest {
    @Test
    public void ConvertGroupAddressToHex_1()
    {
        ConvertFactory convertFactory = new ConvertFactory();
        String result = convertFactory.convertDestinationAddressToHex("4/0/1");
        assertEquals("2001", result);
    }

    @Test
    public void ConvertGroupAddressToHex_2()
    {
        ConvertFactory convertFactory = new ConvertFactory();
        String result = convertFactory.convertDestinationAddressToHex("15/2/18");
        assertEquals("7a12", result);
    }

    @Test
    public void ConvertGroupAddressToHex_3()
    {
        ConvertFactory convertFactory = new ConvertFactory();
        String result = convertFactory.convertDestinationAddressToHex("5/6/10");
        assertEquals("2e0a", result);
    }

    @Test
    public void ConvertIndividualAddressToHex_1()
    {
        ConvertFactory convertFactory = new ConvertFactory();
        String result = convertFactory.convertSourceAddressToHex("1.1.2");
        assertEquals("1102", result);
    }

    @Test
    public void ConvertIndividualAddressToHex_2()
    {
        ConvertFactory convertFactory = new ConvertFactory();
        String result = convertFactory.convertSourceAddressToHex("1.1.118");
        assertEquals("1176", result);
    }

    @Test
    public void ConvertIndividualAddressToHex_3()
    {
        ConvertFactory convertFactory = new ConvertFactory();
        String result = convertFactory.convertSourceAddressToHex("1.1.5");
        assertEquals("1105", result);
    }

    @Test
    public void ConvertIndividualAddressToHex_4()
    {
        ConvertFactory convertFactory = new ConvertFactory();
        String result = convertFactory.convertSourceAddressToHex("1.1.55");
        assertEquals("1137", result);
    }

    @Test
    public void CalculateCRC_1()
    {
        ConvertFactory convertFactory = new ConvertFactory();
        String result = convertFactory.calculateCRC("BC02112001E10081");
        assertEquals("11", result);
    }

    @Test
    public void CalculateCRC_2()
    {
        ConvertFactory convertFactory = new ConvertFactory();
        String result = convertFactory.calculateCRC("BC02112001E10080");
        assertEquals("10", result);
    }

    @Test
    public void CalculateCRC_3()
    {
        ConvertFactory convertFactory = new ConvertFactory();
        String result = convertFactory.calculateCRC("BC110A2000E10041");
        assertEquals("d8", result);
    }
}