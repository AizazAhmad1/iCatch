package com.icatch.wificam.customer.type;

public class ICatchEvent {
    private double doubleValue1;
    private double doubleValue2;
    private double doubleValue3;
    private int eventID;
    private ICatchFile fileValue1;
    private int intValue1;
    private int intValue2;
    private int intValue3;
    private int sessionID;
    private String stringValue1;
    private String stringValue2;
    private String stringValue3;

    public int getEventID() {
        return this.eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public int getIntValue1() {
        return this.intValue1;
    }

    public void setIntValue1(int intValue1) {
        this.intValue1 = intValue1;
    }

    public int getIntValue2() {
        return this.intValue2;
    }

    public void setIntValue2(int intValue2) {
        this.intValue2 = intValue2;
    }

    public int getIntValue3() {
        return this.intValue3;
    }

    public void setIntValue3(int intValue3) {
        this.intValue3 = intValue3;
    }

    public double getDoubleValue1() {
        return this.doubleValue1;
    }

    public void setDoubleValue1(double doubleValue1) {
        this.doubleValue1 = doubleValue1;
    }

    public double getDoubleValue2() {
        return this.doubleValue2;
    }

    public void setDoubleValue2(double doubleValue2) {
        this.doubleValue2 = doubleValue2;
    }

    public double getDoubleValue3() {
        return this.doubleValue3;
    }

    public void setDoubleValue3(double doubleValue3) {
        this.doubleValue3 = (double) this.intValue3;
    }

    public String getStringValue1() {
        return this.stringValue1;
    }

    public void setStringValue1(String stringValue1) {
        this.stringValue1 = stringValue1;
    }

    public void setStringValue2(String stringValue2) {
        this.stringValue2 = stringValue2;
    }

    public String getStringValue2() {
        return this.stringValue2;
    }

    public void setStringValue3(String stringValue3) {
        this.stringValue3 = stringValue3;
    }

    public String getStringValue3() {
        return this.stringValue3;
    }

    public ICatchFile getFileValue1() {
        return this.fileValue1;
    }

    public void setFileValue1(ICatchFile fileValue1) {
        this.fileValue1 = fileValue1;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("eventID: ").append(this.eventID).append("; ");
        stringBuilder.append("sessionID: ").append(this.sessionID).append("; ");
        stringBuilder.append("intValue1: ").append(this.intValue1).append("; ");
        stringBuilder.append("intValue2: ").append(this.intValue2).append("; ");
        stringBuilder.append("intValue2: ").append(this.intValue2).append("; ");
        stringBuilder.append("doubleValue1: ").append(this.doubleValue1).append("; ");
        stringBuilder.append("doubleValue2: ").append(this.doubleValue2).append("; ");
        stringBuilder.append("doubleValue3: ").append(this.doubleValue3).append("; ");
        stringBuilder.append("stringValue1: ").append(this.stringValue1).append("; ");
        stringBuilder.append("stringValue2: ").append(this.stringValue2).append("; ");
        stringBuilder.append("stringValue3: ").append(this.stringValue3).append("; ");
        return stringBuilder.toString();
    }
}
