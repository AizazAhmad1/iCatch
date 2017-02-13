package com.icatch.ismartdv2016.AppInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CfgProperty {
    private static Map<String, String> propertyMap = new HashMap();
    private String fileName;

    public CfgProperty(String fileName) {
        this.fileName = fileName;
    }

    public String getProperty(String key) throws FileNotFoundException, IOException {
        String value = (String) propertyMap.get(key);
        Properties property = new Properties();
        if (value != null) {
            return value;
        }
        property.load(new FileInputStream(this.fileName));
        value = property.getProperty(key);
        propertyMap.put(key, value);
        return value;
    }

    public Map<String, String> getProperty(List<String> propertyList) throws FileNotFoundException, IOException {
        Throwable th;
        Map<String, String> propertyMap = new HashMap();
        Properties property = new Properties();
        FileInputStream inputFile = null;
        try {
            FileInputStream inputFile2 = new FileInputStream(this.fileName);
            try {
                property.load(inputFile2);
                for (String name : propertyList) {
                    propertyMap.put(name, property.getProperty(name));
                }
                if (inputFile2 != null) {
                    inputFile2.close();
                }
                return propertyMap;
            } catch (Throwable th2) {
                th = th2;
                inputFile = inputFile2;
                if (inputFile != null) {
                    inputFile.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            if (inputFile != null) {
                inputFile.close();
            }
            throw th;
        }
    }
}
