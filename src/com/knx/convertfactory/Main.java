package com.knx.convertfactory;

import com.knx.convertfactory.fields.AddressHopLength;
import com.knx.convertfactory.fields.ControlField;
import com.knx.convertfactory.fields.Payload;
import com.knx.convertfactory.helpers.HelpMethods;
import com.knx.convertfactory.types.AddressType;
import com.knx.convertfactory.types.CommunicationObjectValidationTypes;
import com.knx.convertfactory.types.PayloadTypes;
import com.knx.convertfactory.types.TelegramPriorityTypes;

import java.util.ArrayList;
import java.util.List;

class Main {

    public static void main(String[] args) throws Exception {
        ConvertFactoryManager convertFactoryManager = new ConvertFactoryManager();

        /*ConvertCommunicationObject convertCommunicationObject = new ConvertCommunicationObject
                (
                new ControlField(true,false,TelegramPriorityTypes.LOW),
                        "1.1.2",
                        "4.0.1",
                        new AddressHopLength(AddressType.MULTIPLE_DEVICE),
                        new Payload(PayloadTypes.Write, "01","0")
                  );*/

      /*  CommunicationObject communicationObject = new CommunicationObject
                (
                        new ControlField(true,false,TelegramPriorityTypes.LOW),
                        "1.1.2",
                        "4.0.1",
                        new AddressHopLength(AddressType.MULTIPLE_DEVICE),
                        new Payload(PayloadTypes.Write, "01","0"),
                        ""
                );

        byte[] hexResult = convertFactoryManager.convertCommunicationObjectToByteArray(communicationObject);
        communicationObject.setRawHex(HelpMethods.byteArrayToHex(hexResult));
        System.out.println(hexResult);*/

        byte[] pack1 = {
                (byte) 0xBC,
                (byte) 0x12,
        };

        byte[] pack2 = {
                (byte) 0x03,
                (byte) 0x08,
                (byte) 0x06,
                (byte) 0xE1,
                (byte) 0x00,
                (byte) 0x00,
                (byte) 0xBD,
                (byte) 0x8B,
        };

        byte[] pack3 = {
                (byte) 0xBC,
                (byte) 0x15,
                (byte) 0x01,
        };

        byte[] pack4 = {
                (byte) 0x08,
                (byte) 0x06,
                (byte) 0xE3,
                (byte) 0x00,
                (byte) 0x40,
                (byte) 0x0C,
                (byte) 0x33,
                (byte) 0xC5,
        };



        List<byte[]> listByteArray = new ArrayList<>();
        listByteArray.add(pack1);
        listByteArray.add(pack2);
        listByteArray.add(pack3);
        listByteArray.add(pack4);

        List<CommunicationObject> listCommunicationObject = new ArrayList<>();

        for (byte[] byteArray : listByteArray)
        {
            for (Byte byt : byteArray)
            {
                CommunicationObjectValidationTypes covt = convertFactoryManager.validateByteInput(byt);

                int length = convertFactoryManager.listByte.size();
                String hex = HelpMethods.listByteToHex(convertFactoryManager.listByte);

                if (covt != CommunicationObjectValidationTypes.INCOMPLETE)
                {
                    CommunicationObject comObject = convertFactoryManager.getCommunicationObject();
                    comObject.setPackageType(covt);
                    listCommunicationObject.add(comObject);
                    convertFactoryManager.resetList();
                }
            }
        }
        int k = 3;
    }




   /* byte[] pack1 = {
            (byte) 0xBC,
            (byte) 0x11,
            (byte) 0x02
    };



    byte[] pack2 = {
            (byte) 0x11,
            (byte) 0x01,
            (byte) 0xE1,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x91,
            (byte) 0x8B,
    };

    byte[] pack3 = {
            (byte) 0xBC,
            (byte) 0x11,
            (byte) 0x0A,
            (byte) 0x20,
            (byte) 0x00,
            (byte) 0xE1,
            (byte) 0x00,
            (byte) 0x40,
    };

    byte[] pack4 = {
            (byte) 0xD9,
            (byte) 0x9C,
            (byte) 0x11,
            (byte) 0x0A,
            (byte) 0x20,
            (byte) 0x00,
            (byte) 0xE1,
            (byte) 0x00,
    };

    byte[] pack5 = {
            (byte) 0x40,
            (byte) 0xF9,
            (byte) 0x9C,
            (byte) 0x11,
            (byte) 0x0A,
            (byte) 0x20,
            (byte) 0x00,
            (byte) 0xE1,
    };

    byte[] pack6 = {
            (byte) 0x00,
            (byte) 0x40,
            (byte) 0xF9,
            (byte) 0x9C,
            (byte) 0x11,
            (byte) 0x0A,
            (byte) 0x20,
            (byte) 0x00,
    };

    byte[] pack7 = {
            (byte) 0xE1,
            (byte) 0x00,
            (byte) 0x40,
            (byte) 0xF9
    };*/
}
