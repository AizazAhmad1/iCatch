package com.icatch.ismartdv2016.Dbl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.icatch.ismartdv2016.Beans.CameraSlot;
import com.icatch.ismartdv2016.GlobalApp.GlobalInfo;
import com.icatch.ismartdv2016.Log.AppLog;
import com.icatch.ismartdv2016.SystemInfo.MWifiManager;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import uk.co.senab.photoview.BuildConfig;

public class CameraSlotSQLite {
    private static CameraSlotSQLite instance;
    private String TAG = "CameraSlotSQLite";
    private ArrayList<CameraSlot> camSlotList;
    private Context context;
    public int curSlotPosition = 0;
    public String curWifiSsid = BuildConfig.FLAVOR;
    private SQLiteDatabase db;

    public static CameraSlotSQLite getInstance() {
        if (instance == null) {
            instance = new CameraSlotSQLite();
        }
        return instance;
    }

    private CameraSlotSQLite() {
        creatTable(GlobalInfo.getInstance().getAppContext());
    }

    private void creatTable(Context context) {
        AppLog.i(this.TAG, "start creatTable");
        this.context = context;
        this.db = new CameraSlotSQLiteHelper(context).getWritableDatabase();
        AppLog.i(this.TAG, "end creatTable");
    }

    public boolean insert(CameraSlot camSlot) {
        AppLog.i(this.TAG, "start insert isOccupied=" + switchBoolToInt(Boolean.valueOf(camSlot.isOccupied)));
        ContentValues cValue = new ContentValues();
        cValue.put("isOccupied", Integer.valueOf(switchBoolToInt(Boolean.valueOf(camSlot.isOccupied))));
        cValue.put("cameraName", camSlot.cameraName);
        cValue.put("imageBuffer", camSlot.cameraPhoto);
        if (this.db.insert(CameraSlotSQLiteHelper.DATABASE_TABLE, null, cValue) == -1) {
            AppLog.i(this.TAG, "failed to insert!");
            return false;
        }
        AppLog.i(this.TAG, "end: insert success");
        return true;
    }

    public void update(CameraSlot camSlot) {
        AppLog.i(this.TAG, "start update slotPosition=" + camSlot.slotPosition);
        ContentValues values = new ContentValues();
        values.put("isOccupied", Integer.valueOf(switchBoolToInt(Boolean.valueOf(camSlot.isOccupied))));
        if (!(camSlot.isOccupied && camSlot.cameraPhoto == null)) {
            values.put("imageBuffer", camSlot.cameraPhoto);
        }
        values.put("cameraName", camSlot.cameraName);
        String[] whereArgs = new String[]{String.valueOf(camSlot.slotPosition + 1)};
        this.db.update(CameraSlotSQLiteHelper.DATABASE_TABLE, values, "_id=?", whereArgs);
        AppLog.i(this.TAG, "end update");
    }

    public void deleteByPosition(int slotPosition) {
        AppLog.i(this.TAG, "start delete slotPosition=" + slotPosition);
        update(new CameraSlot(slotPosition, false, null, null, false));
        AppLog.i(this.TAG, "end delete");
    }

    public ArrayList<CameraSlot> getAllCameraSlotFormDb() {
        AppLog.i(this.TAG, "start getAllCameraSlotFormDb");
        this.camSlotList = new ArrayList();
        Cursor cursor = this.db.rawQuery("select * from cameraSlotInfo", null);
        AppLog.i(this.TAG, "end rawQuery =" + cursor.getCount());
        String wifiSsid = MWifiManager.getSsid(this.context);
        AppLog.i(this.TAG, "getSsid wifiSsid =" + wifiSsid);
        while (cursor.moveToNext()) {
            AppLog.i(this.TAG, "cursor.getInt(cursor.getColumnIndex =" + cursor.getInt(cursor.getColumnIndex("_id")));
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            int isUsed = cursor.getInt(cursor.getColumnIndex("isOccupied"));
            String camName = cursor.getString(cursor.getColumnIndex("cameraName"));
            byte[] imageBuf = cursor.getBlob(cursor.getColumnIndex("imageBuffer"));
            boolean isReady = false;
            if (wifiSsid != null && wifiSsid.equals(camName)) {
                isReady = true;
            }
            this.camSlotList.add(new CameraSlot(id - 1, switchIntToBool(isUsed).booleanValue(), camName, imageBuf, isReady));
            AppLog.i(this.TAG, " switchIntToBool(isUsed) =" + switchIntToBool(isUsed));
            AppLog.i(this.TAG, "_id=" + id + " isOccupied=" + isUsed + " camName=" + camName);
        }
        AppLog.i(this.TAG, "end query all cameraSlot");
        if (cursor != null) {
            cursor.close();
        }
        if (this.camSlotList.size() == 0) {
            for (int ii = 0; ii < 3; ii++) {
                if (insert(new CameraSlot(ii, false, null, null))) {
                    this.camSlotList.add(new CameraSlot(ii, false, null, null, false));
                }
            }
        }
        AppLog.i(this.TAG, "end getAllCameraSlotFormDb");
        return this.camSlotList;
    }

    public void updateImage(Bitmap bitmap) {
        AppLog.d(this.TAG, "start updateImage curSlotPosition=" + this.curSlotPosition);
        AppLog.i(this.TAG, "start updateImage curWifiSsid=" + this.curWifiSsid);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(CompressFormat.PNG, 100, os);
            update(new CameraSlot(this.curSlotPosition, true, this.curWifiSsid, os.toByteArray(), true));
        }
    }

    public void closeDB() {
        this.db.close();
    }

    private int switchBoolToInt(Boolean value) {
        if (value.booleanValue()) {
            return 1;
        }
        return 0;
    }

    private Boolean switchIntToBool(int value) {
        if (value == 1) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }
}
