
# KNX Binary Conversion Library

This library aims to help a provide communication with KNX devices via serial port. It allows you to create communication objects and convert them to byte array and vice versa.

> *Please note that this library aims for a certain goal and doesn't cover every need for KNX Communications.*

## Download JAR File

[DOWNLOAD](https://github.com/tataelm/KnxBinaryConversion/raw/master/out/artifacts/KnxBinaryConvertFactory_jar/KnxBinaryConvertFactory.jar) the final artifact from here

## How to use it

This library works on both ways.
- Java Object > Byte Array
- Byte array > Java object


### 1. Create an object and convert it to byte array

First you need to create your **Communication Object**

There are different types of constructors for Communication Objects. A default way to create it requires couple sub-objects:

a. Control Field

b. Source Address

c. Destination Address

d. AddressHopLength

c. Payload


        CommunicationObject communicationObject = new CommunicationObject
                (
                        new ControlField(true,false,TelegramPriorityTypes.LOW),
                        "1.1.2",
                        "4.0.1",
                        new AddressHopLength(AddressType.MULTIPLE_DEVICE),
                        new Payload(PayloadTypes.Write, "01","0")
                );

##### Control Field

A Control Field contains 4 bit:
- 1 bit to determine whether it is Standard or not. (First parameter as **boolean**)
- 1 bit to determine whether it is Repeated or not (Second parameter as **boolean**)
- 2 bits to determine Telegram Priority (Third parameter as **TelegramPriorityType enum**)


##### Source Address
This is a raw string. The input pattern must be like X/X/X or X.X.X

##### Destination Address
This is a raw string. The input pattern must be like X/X/X or X.X.X

##### AddressHopLength
This class takes one parameter as **AddressType enum**

##### Payload
When creating a payload class, we must define 3 parameters. 
- PayloadType, where we determine whether it is a **Write**, **Read** or **Response** command
- A data string, which differs from device to device. 
- A datalength string


### Converting the Communication Object

Simply create an instance of ConvertFactoryManager and call the related method.

    ConvertFactoryManager convertFactoryManager = new ConvertFactoryManager();
    byte[] hexResult = convertFactoryManager.convertCommunicationObjectToByteArray(communicationObject);

You could optionally set the final raw hexadecimal value to self object to see it. 

` communicationObject.setRawHex(HelpMethods.byteArrayToHex(hexResult));
       System.out.println(hexResult);`


### 2. Receive raw byte array and convert it to Communication Object

While listening from a serial port (in my case USB port) and the packages coming as byte arrays.


    List<CommunicationObject> listCommunicationObject = new ArrayList<>();
    
    for (Byte byt : byteArray)
    {
       CommunicationObjectValidationTypes covt = convertFactoryManager.validateByteInput(byt);
    
        if (covt != CommunicationObjectValidationTypes.INCOMPLETE)
        {
            CommunicationObject comObject = ConvertFactoryManager.getCommunicationObject();
            comObject.setPackageType(covt);
            listCommunicationObject.add(comObject);
            convertFactoryManager.resetList();
        }
    }


At the end of this iteration, you will have a fully readable object. 

Please be careful that sometimes one whole package can be read in separate packages. So if the byteArray you are iterating isn't complete, don't be worried. Through this library, you will be able to combine different packages into one readable Communication Object.


### Known Issues
I have run some tests on it and fixed some bugs already. So far so good. 

Devices I have tested with;
- KNX Actuator(lights, lamps)
- KNX Thermostat


Still need to test with;
- KNX DALI devices


#### NOTES
- Developed with IntelliJ Community
- Used JDK 1.8




