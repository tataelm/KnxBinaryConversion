package com.knx.convertfactory;

import com.knx.convertfactory.fields.AddressHopLength;
import com.knx.convertfactory.fields.ControlField;
import com.knx.convertfactory.fields.Payload;
import com.knx.convertfactory.types.AddressType;
import com.knx.convertfactory.types.CommunicationObjectValidationTypes;
import com.knx.convertfactory.types.TelegramPriorityTypes;

public class CommunicationObject {

    private ControlField controlField;
    private String sourceAddress, destinationAddress;
    private AddressHopLength addressHopLength;
    private Payload payload;
    private CommunicationObjectValidationTypes packageType = CommunicationObjectValidationTypes.NONE;
    private String rawHex;

    /**
     * Default ConvertCommunicationObject assigns some default values
     *
     * @param addressType whether is a single, group or broadcast communication
     * @param destinationAddress helps deciding addressType, gives the destination address
     * */
    public CommunicationObject(String sourceAddress, String destinationAddress, AddressType addressType, Payload payload)
    {
        this.controlField = new ControlField(true, false, TelegramPriorityTypes.LOW);
        this.addressHopLength = new AddressHopLength(addressType);
        this.destinationAddress = destinationAddress;
        this.sourceAddress = sourceAddress;
        this.payload = payload;
    }

    /**
     * Creates a ConvertCommunicationObject with all parameters
     * */
    public CommunicationObject(ControlField controlField, String sourceAddress, String destinationAddress, AddressHopLength addressHopLength, Payload payload, String rawHex) {
        this.controlField = controlField;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
        this.addressHopLength = addressHopLength;
        this.payload = payload;
        this.rawHex = rawHex;
    }

    public CommunicationObject(CommunicationObjectValidationTypes covt) {
        this.packageType = covt;
    }

    public ControlField getControlField() {
        return controlField;
    }

    public void setControlField(ControlField controlField) {
        this.controlField = controlField;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public AddressHopLength getAddressHopLength() {
        return addressHopLength;
    }

    public void setAddressHopLength(AddressHopLength addressHopLength) {
        this.addressHopLength = addressHopLength;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public CommunicationObjectValidationTypes getPackageType() {
        return packageType;
    }

    public void setPackageType(CommunicationObjectValidationTypes packageType) {
        this.packageType = packageType;
    }

    public String getRawHex() {
        return rawHex;
    }

    public void setRawHex(String rawHex) {
        this.rawHex = rawHex;
    }
}
