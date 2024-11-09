package com.lab.endsem.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appSettings.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SETTINGS = "settings";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SOUND = "notification_sound";

    private static final String CREATE_TABLE_SETTINGS = "CREATE TABLE " + TABLE_SETTINGS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SOUND + " TEXT" +
            ")";

    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SETTINGS);
        initializeDefaultSound(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETTINGS);
        onCreate(db);
    }

    private void initializeDefaultSound(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SOUND, "sound1"); // Default sound
        db.insert(TABLE_SETTINGS, null, values);
    }

    public void saveSelectedSound(String soundFile) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(COLUMN_SOUND, soundFile);

            int rowsUpdated = db.update(TABLE_SETTINGS, values, null, null);
            if (rowsUpdated == 0) {
                db.insert(TABLE_SETTINGS, null, values);
            }
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }

    public String getSelectedSound() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String selectedSound = "sound1";

        try {
            db = this.getReadableDatabase();
            cursor = db.query(TABLE_SETTINGS, new String[]{COLUMN_SOUND}, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                selectedSound = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SOUND));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return selectedSound;
    }
}
