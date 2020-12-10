package com.knx.convertfactory;

import com.knx.convertfactory.fields.AddressHopLength;
import com.knx.convertfactory.fields.ControlField;
import com.knx.convertfactory.fields.Payload;
import com.knx.convertfactory.helpers.HelpMethods;
import com.knx.convertfactory.types.CommunicationObjectValidationTypes;
import com.knx.convertfactory.types.ExpectedField;
import com.knx.convertfactory.types.RawDataExceptionType;

import java.util.ArrayList;
import java.util.List;

public class ConvertFactoryManager {

    /**
     * Main method for producing a byte array in order to communicate with KNX devices
     * This method takes a pre-created ConvertCommunicationObject and gets all necessary elements
     * from it and handles every field separately
     *
     * @param communicationObject takes ConvertCommunicationObject as a collection of all information
     *                                   needed to convert it into the byte array
     * @return byte array to communicate with port
     */
    public byte[] convertCommunicationObjectToByteArray(CommunicationObject communicationObject) throws Exception
    {
        ConvertFactory convertFactory = new ConvertFactory();

        String sourceAddress = communicationObject.getSourceAddress();
        String destinationAddress = communicationObject.getDestinationAddress();
        Payload payload = communicationObject.getPayload();
        ControlField controlField = communicationObject.getControlField();
        AddressHopLength addressHopLength = communicationObject.getAddressHopLength();

        sourceAddress = convertFactory.validateAddress(RawDataExceptionType.SOURCE_ADDRESS, sourceAddress);
        destinationAddress = convertFactory.validateAddress(RawDataExceptionType.DESTINATION_ADDRESS, destinationAddress);

        String controlFieldHex = HelpMethods.binaryToHex(controlField.getControlFieldHex()).toUpperCase();
        String sourceAddressHex = convertFactory.convertSourceAddressToHex(sourceAddress).toUpperCase();
        String destinationAddressHex = convertFactory.convertDestinationAddressToHex(destinationAddress).toUpperCase();
        String payloadHex = payload.getHex();

        addressHopLength.setDestinationAddress(destinationAddressHex);
        addressHopLength.startCalculations();

        String addressHopLengthHex = addressHopLength.getHexValueOfAddressHopLength(payload.getDataLength()).toUpperCase();

        String crc = convertFactory.calculateCRC(controlFieldHex + sourceAddressHex + destinationAddressHex + addressHopLengthHex + payloadHex);

        return addFixedBytes(controlFieldHex, sourceAddressHex, destinationAddressHex, addressHopLengthHex, payloadHex, crc);
    }

    /**
     * Adds fixed bytes in-between pre-calculated bytes according to KNX documentation
     *
     * @param input this is a string array, it holds every "unfixed" bytes
     * @return byte array to finish the process
     * */
    private byte[] addFixedBytes(String... input) {

        final int fixNumber = Integer.parseInt("80",16);
        final int fixNumberBeforeCRC = Integer.parseInt("40",16);
        String fixNumberFactorHex = "0";

        List<String> listResult = new ArrayList<>();
        for (int i = 0; i < input.length; i++) {

            // current hex value
            String hex = input[i];

            int fixNumberFactor;
            int fixNumberInResultArray;
            String fixNumberInResultArrayHex;

            // check if the CRC digit
            if (i != input.length-1)
            {
                // calculate and add fixed byte
                fixNumberFactor = Integer.parseInt(fixNumberFactorHex, 16);

                fixNumberInResultArray = fixNumber | fixNumberFactor;
                fixNumberInResultArrayHex = Integer.toHexString(fixNumberInResultArray);

                listResult.add(fixNumberInResultArrayHex);

                // increase the factor for next fixed byte
                fixNumberFactor++;
                fixNumberFactorHex =  Integer.toHexString(fixNumberFactor);

                // check if current hex has 2 digits
                if (hex.length() < 3) {
                    listResult.add(hex);
                } else { // if more than 2 digits, split it into 2

                    List<String> listHex = HelpMethods.getListHexIn2Digits(hex);
                    // add first 2 digits
                    listResult.add(listHex.get(0));

                    // calculate and add fixed byte
                    fixNumberFactor = Integer.parseInt(fixNumberFactorHex, 16);
                    fixNumberInResultArray = fixNumber | fixNumberFactor;
                    fixNumberInResultArrayHex = Integer.toHexString(fixNumberInResultArray);
                    listResult.add(fixNumberInResultArrayHex);

                    // increase the factor for next fixed byte
                    fixNumberFactor++;
                    fixNumberFactorHex =  Integer.toHexString(fixNumberFactor);

                    // add second 2 digits
                    listResult.add(listHex.get(1));
                }
            }
            else
            {
                // calculate and add fixed byte
                fixNumberFactor = Integer.parseInt(fixNumberFactorHex, 16);
                fixNumberInResultArray = fixNumberBeforeCRC | fixNumberFactor;
                fixNumberInResultArrayHex = Integer.toHexString(fixNumberInResultArray);
                listResult.add(fixNumberInResultArrayHex);

                // add crc
                listResult.add(hex);
            }
        }
        byte[] result = new byte[listResult.size()];
        for(int index = 0; index < result.length; index++)
        {
            result[index] = HelpMethods.hexToByte(listResult.get(index));
        }
        return result;
    }





    public List<Byte> listByte = new ArrayList<>();
    private ControlField controlField;
    private String sourceAddress, destinationAddress, rawHex;
    private AddressHopLength addressHopLength;
    private Payload payload;
    //public CommunicationObjectValidationTypes PackageType;
    private ConvertFactory convertFactory = new ConvertFactory();
    private ExpectedField EXPECTED;
    private CommunicationObjectValidationTypes communicationObjectValidationTypes = CommunicationObjectValidationTypes.INCOMPLETE;
    private boolean validPackage = false;
    private int searchedByte = 0;
    private int tmpDataLength = 0;

    public CommunicationObjectValidationTypes validateInputByteArray(byte[] byteArray) throws Exception {

        for (byte b : byteArray)
        {
            listByte.add(b);
            identifyInput(listByte);

            if (validPackage)
                return communicationObjectValidationTypes;
        }
        return CommunicationObjectValidationTypes.INCOMPLETE;
    }

    public CommunicationObjectValidationTypes validateByteInput(Byte b) throws Exception {

        listByte.add(b);
        identifyInput(listByte);

        if (validPackage) {
            return communicationObjectValidationTypes;
        }

        // return CommunicationObjectValidationTypes.INCOMPLETE;
        return communicationObjectValidationTypes;
    }

    private void identifyInput(List<Byte> listByte) throws Exception {

        if (listByte.size() == 1) // Check if control field OR ack message
        {
            System.out.println("list length: " + listByte.size() + " ---- searchedByte: " + searchedByte);

            if (!isAcknowledgeMessage(listByte.get(searchedByte))) { // searchedByte == 0

                controlField = new ControlField(listByte.get(searchedByte));


                if (controlField.isValid)
                {
                    EXPECTED = ExpectedField.SOURCE_ADDRESS;
                    searchedByte++;
                }
                else
                {
                    EXPECTED = ExpectedField.NONE;
                    resetList();
                }

                return;
            }

            validPackage = true;
            EXPECTED = ExpectedField.NONE;

            /*if (!isAcknowledgeMessage(listByte.get(searchedByte))) { // searchedByte == 0
               controlField = new ControlField(listByte.get(searchedByte));

               if (controlField.isValid)
               {
                   EXPECTED = ExpectedField.SOURCE_ADDRESS;
                   searchedByte++;
               }
               else
               {
                   EXPECTED = ExpectedField.NONE;
                   resetList();
               }
               return;
           }
           else
           {
               validPackage = true;
               EXPECTED = ExpectedField.NONE;
           }*/
        }

        switch (EXPECTED)
        {
            case SOURCE_ADDRESS:
                // Check if source address bytes fully in, else wait another round
                if (listByte.size() == 2)
                {
                    return;
                }
                else
                {
                    sourceAddress = convertFactory.convertBinaryToSourceAddress(listByte.get(searchedByte), listByte.get(searchedByte+1)); // searchedByte = 1
                    EXPECTED = ExpectedField.DESTINATION_ADDRESS;
                    searchedByte = searchedByte + 2;
                }
                break;

            case DESTINATION_ADDRESS:
                // Check if destination address bytes fully in, else wait another round
                if (listByte.size() == 4)
                {
                    return;
                }
                else
                {
                    destinationAddress = convertFactory.convertBinaryToDestinationAddress(listByte.get(searchedByte), listByte.get(searchedByte + 1)); // searchedByte = 3
                    EXPECTED = ExpectedField.ADDRESSHOPLENGTH;
                    searchedByte = searchedByte + 2;
                }
                break;

            case ADDRESSHOPLENGTH:
                // Check if address hop length bytes fully in, else wait another round
                if (listByte.size() == 6)
                {
                    return;
                }
                else
                {
                    addressHopLength = new AddressHopLength(listByte.get(searchedByte), listByte.get(searchedByte+1)); // searchedByte = 5
                    EXPECTED = ExpectedField.PAYLOAD;
                    searchedByte = searchedByte + 2;
                }
                break;

            case PAYLOAD:
                payload = new Payload(listByte.get(searchedByte), addressHopLength.getLength()); // searchedByte = 7
                EXPECTED = payload.getExpectedExtension();
                searchedByte++;

                if(EXPECTED.equals(ExpectedField.PAYLOAD_EXTENSION))
                    tmpDataLength = Integer.parseInt(payload.getDataLength());
                break;

            case PAYLOAD_EXTENSION:
                //int tmpDataLength = Integer.parseInt(payload.getDataLength());

                payload.addToData(listByte.get(searchedByte));

                searchedByte++;
                tmpDataLength--;

                if (tmpDataLength == 1)
                {
                    EXPECTED = ExpectedField.CRC;
                }
                break;

            case CRC:
                EXPECTED = ExpectedField.NONE;
                validPackage = true;
                Thread.sleep(10);
                communicationObjectValidationTypes = CommunicationObjectValidationTypes.DEFAULT;
                Thread.sleep(10);
                String hex = "";

                for (int index = 0; index < listByte.size()-1; index++)
                {
                    hex = hex + HelpMethods.getHexIn2Digits(HelpMethods.byteToHex(listByte.get(index)));
                }

                String validationCRC = convertFactory.calculateCRC(hex);
                String currentCRC = HelpMethods.byteToHex(listByte.get(listByte.size()-1));

                if (!validationCRC.toUpperCase().equals(currentCRC.toUpperCase()))
                {
                    //TODO throw new Exception("Unmatched CRC. Excepted: " + validationCRC + " Current CRC: " + currentCRC);
                }
                rawHex = hex + validationCRC;
                searchedByte = 0;

                System.out.println("CRC: validPackage -->" + validPackage + "  covt ---> " + communicationObjectValidationTypes.name());

                break;
        }
    }

    private boolean isAcknowledgeMessage(Byte aByte) {
        String hexValue = HelpMethods.byteToHex(aByte).toUpperCase();
        for (CommunicationObjectValidationTypes com : CommunicationObjectValidationTypes.values()) {
            if (com.toString().equals(hexValue)) {
                communicationObjectValidationTypes = com;
                return true;
            }
        }
        communicationObjectValidationTypes = CommunicationObjectValidationTypes.INCOMPLETE;
        return false;
    }

    public CommunicationObject getCommunicationObject() {
        return new CommunicationObject(controlField, sourceAddress, destinationAddress, addressHopLength, payload, rawHex);
    }

    public ControlField getControlField() {
        return controlField;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public Payload getPayload() {
        return payload;
    }

    public AddressHopLength getAddressHopLength() {
        return addressHopLength;
    }

    public void resetList() {
        searchedByte = 0;
        controlField = null;
        sourceAddress = null;
        destinationAddress = null;
        addressHopLength = null;
        payload = null;
        rawHex = null;
        listByte.clear();
    }


}
