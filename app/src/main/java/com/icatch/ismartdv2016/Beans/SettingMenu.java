package com.icatch.ismartdv2016.Beans;

public class SettingMenu {
    public int name;
    public int titleId;
    public String value;

    public SettingMenu(int name, String value, int titleId) {
        this.name = name;
        this.value = value;
        this.titleId = titleId;
    }

    public int getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public int getTitleId() {
        return this.titleId;
    }
}
