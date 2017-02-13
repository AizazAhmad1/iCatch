package com.icatch.wificam.customer.type;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class ICatchPhotoExif {
    public static final int ORIENTATION_FLIP_HORIZONTAL = 2;
    public static final int ORIENTATION_FLIP_VERTICAL = 4;
    public static final int ORIENTATION_NORMAL = 1;
    public static final int ORIENTATION_ROTATE_180 = 3;
    public static final int ORIENTATION_ROTATE_270 = 8;
    public static final int ORIENTATION_ROTATE_90 = 6;
    public static final int ORIENTATION_TRANSPOSE = 5;
    public static final int ORIENTATION_TRANSVERSE = 7;
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final String TAG_APERTURE = "FNumber";
    public static final String TAG_DATETIME = "DateTime";
    public static final String TAG_EXPOSURE_TIME = "ExposureTime";
    public static final String TAG_FLASH = "Flash";
    public static final String TAG_FOCAL_LENGTH = "FocalLength";
    public static final String TAG_GPS_ALTITUDE = "GPSAltitude";
    public static final String TAG_GPS_ALTITUDE_REF = "GPSAltitudeRef";
    public static final String TAG_GPS_DATESTAMP = "GPSDateStamp";
    public static final String TAG_GPS_LATITUDE = "GPSLatitude";
    public static final String TAG_GPS_LATITUDE_REF = "GPSLatitudeRef";
    public static final String TAG_GPS_LONGITUDE = "GPSLongitude";
    public static final String TAG_GPS_LONGITUDE_REF = "GPSLongitudeRef";
    public static final String TAG_GPS_PROCESSING_METHOD = "GPSProcessingMethod";
    public static final String TAG_GPS_TIMESTAMP = "GPSTimeStamp";
    public static final String TAG_IMAGE_LENGTH = "ImageLength";
    public static final String TAG_IMAGE_WIDTH = "ImageWidth";
    public static final String TAG_ISO = "ISOSpeedRatings";
    public static final String TAG_MAKE = "Make";
    public static final String TAG_MODEL = "Model";
    public static final String TAG_ORIENTATION = "Orientation";
    public static final String TAG_WHITE_BALANCE = "WhiteBalance";
    public static final int WHITEBALANCE_AUTO = 0;
    public static final int WHITEBALANCE_MANUAL = 1;
    private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
    private HashMap<String, String> mAttributes;
    private boolean mHasThumbnail;

    static {
        sFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public ICatchPhotoExif(String exifs) {
        loadAttributes(exifs);
    }

    public String getAttribute(String tag) {
        return (String) this.mAttributes.get(tag);
    }

    public int getAttributeInt(String tag, int defaultValue) {
        String value = (String) this.mAttributes.get(tag);
        if (value != null) {
            try {
                defaultValue = Integer.valueOf(value).intValue();
            } catch (NumberFormatException e) {
            }
        }
        return defaultValue;
    }

    public double getAttributeDouble(String tag, double defaultValue) {
        String value = (String) this.mAttributes.get(tag);
        if (value == null) {
            return defaultValue;
        }
        try {
            int index = value.indexOf("/");
            if (index == -1) {
                return defaultValue;
            }
            double denom = Double.parseDouble(value.substring(index + WHITEBALANCE_MANUAL));
            if (denom != 0.0d) {
                return Double.parseDouble(value.substring(WHITEBALANCE_AUTO, index)) / denom;
            }
            return defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public void setAttribute(String tag, String value) {
        this.mAttributes.put(tag, value);
    }

    private void loadAttributes(String exifs) {
        this.mAttributes = new HashMap();
        String[] attributes = exifs.split(";");
        int length = attributes.length;
        for (int i = WHITEBALANCE_AUTO; i < length; i += WHITEBALANCE_MANUAL) {
            String[] temps = attributes[i].split(":");
            if (temps != null && temps.length > WHITEBALANCE_MANUAL) {
                String attrName = temps[WHITEBALANCE_AUTO];
                String attrValue = temps[WHITEBALANCE_MANUAL];
                if (attrName.equals("hasThumbnail")) {
                    this.mHasThumbnail = attrValue.equalsIgnoreCase("true");
                } else {
                    this.mAttributes.put(attrName, attrValue);
                }
            }
        }
    }

    public boolean hasThumbnail() {
        return this.mHasThumbnail;
    }

    public boolean getLatLong(float[] output) {
        String latValue = (String) this.mAttributes.get(TAG_GPS_LATITUDE);
        String latRef = (String) this.mAttributes.get(TAG_GPS_LATITUDE_REF);
        String lngValue = (String) this.mAttributes.get(TAG_GPS_LONGITUDE);
        String lngRef = (String) this.mAttributes.get(TAG_GPS_LONGITUDE_REF);
        if (!(latValue == null || latRef == null || lngValue == null || lngRef == null)) {
            try {
                output[WHITEBALANCE_AUTO] = convertRationalLatLonToFloat(latValue, latRef);
                output[WHITEBALANCE_MANUAL] = convertRationalLatLonToFloat(lngValue, lngRef);
                return true;
            } catch (IllegalArgumentException e) {
            }
        }
        return false;
    }

    public double getAltitude(double defaultValue) {
        int i = -1;
        double altitude = getAttributeDouble(TAG_GPS_ALTITUDE, -1.0d);
        int ref = getAttributeInt(TAG_GPS_ALTITUDE_REF, -1);
        if (altitude < 0.0d || ref < 0) {
            return defaultValue;
        }
        if (ref != WHITEBALANCE_MANUAL) {
            i = WHITEBALANCE_MANUAL;
        }
        return altitude * ((double) i);
    }

    public long getDateTime() {
        long j = -1;
        String dateTimeString = (String) this.mAttributes.get(TAG_DATETIME);
        if (dateTimeString != null) {
            try {
                Date datetime = sFormatter.parse(dateTimeString, new ParsePosition(WHITEBALANCE_AUTO));
                if (datetime != null) {
                    j = datetime.getTime();
                }
            } catch (IllegalArgumentException e) {
            }
        }
        return j;
    }

    public long getGpsDateTime() {
        long j = -1;
        String date = (String) this.mAttributes.get(TAG_GPS_DATESTAMP);
        String time = (String) this.mAttributes.get(TAG_GPS_TIMESTAMP);
        if (!(date == null || time == null)) {
            try {
                Date datetime = sFormatter.parse(date + ' ' + time, new ParsePosition(WHITEBALANCE_AUTO));
                if (datetime != null) {
                    j = datetime.getTime();
                }
            } catch (IllegalArgumentException e) {
            }
        }
        return j;
    }

    private static float convertRationalLatLonToFloat(String rationalString, String ref) {
        try {
            String[] parts = rationalString.split(",");
            String[] pair = parts[WHITEBALANCE_AUTO].split("/");
            double degrees = Double.parseDouble(pair[WHITEBALANCE_AUTO].trim()) / Double.parseDouble(pair[WHITEBALANCE_MANUAL].trim());
            pair = parts[WHITEBALANCE_MANUAL].split("/");
            double minutes = Double.parseDouble(pair[WHITEBALANCE_AUTO].trim()) / Double.parseDouble(pair[WHITEBALANCE_MANUAL].trim());
            pair = parts[ORIENTATION_FLIP_HORIZONTAL].split("/");
            double result = ((minutes / 60.0d) + degrees) + ((Double.parseDouble(pair[WHITEBALANCE_AUTO].trim()) / Double.parseDouble(pair[WHITEBALANCE_MANUAL].trim())) / 3600.0d);
            if (!ref.equals("S")) {
                if (!ref.equals("W")) {
                    return (float) result;
                }
            }
            return (float) (-result);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new IllegalArgumentException();
        }
    }
}
