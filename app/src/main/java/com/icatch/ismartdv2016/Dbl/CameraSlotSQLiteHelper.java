package com.icatch.ismartdv2016.Dbl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.icatch.ismartdv2016.Log.AppLog;

public class CameraSlotSQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cameraSlotDb111.db";
    public static final String DATABASE_TABLE = "cameraSlotInfo";
    private static final int DATABASE_VERSION = 1;
    private String CREATE_CAMINFODB = "CREATE TABLE IF NOT EXISTS cameraSlotInfo (_id integer primary key autoincrement, isOccupied integer, cameraName varchar, imageBuffer blob)";
    private String databaseCreate = "CREATE TABLE IF NOT EXISTS cameraSlotDb111.db (_id INTEGER PRIMARY KEY AUTOINCREMENT, isOccupied INTEGER, cameraName VARCHAR, imageBuffer BLOB)";
    private String tableDrop = "drop table if exists cameraSlotInfo";

    public CameraSlotSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        AppLog.d("tigertiger", "start CREATE_CAMINFODB");
        db.execSQL(this.CREATE_CAMINFODB);
        AppLog.d("tigertiger", "end CREATE_CAMINFODB");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(this.tableDrop);
        onCreate(db);
    }
}
