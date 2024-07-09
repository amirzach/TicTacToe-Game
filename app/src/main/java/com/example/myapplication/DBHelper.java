package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TicTacToeDB";
    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_HOST = "HOST";
    private static final String TABLE_PLAYER = "PLAYER";
    private static final String KEY_HOST_ID = "id";
    private static final String KEY_HOST_USERNAME = "username";
    private static final String KEY_HOST_NICKNAME = "nickname";
    private static final String KEY_HOST_PASSWORD = "password";
    private static final String KEY_PLAYER_ID = "id";
    private static final String KEY_PLAYER_NICKNAME = "nickname";
    private static final String KEY_PLAYER_POINTS = "points";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onCreateTable(db);
    }

    private void onCreateTable(SQLiteDatabase db) {

        String CREATE_TABLE_HOST = "CREATE TABLE " + TABLE_HOST + "("
                + KEY_HOST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_HOST_USERNAME + " TEXT, "
                + KEY_HOST_NICKNAME + " TEXT, "
                + KEY_HOST_PASSWORD + " TEXT)";

        String CREATE_TABLE_PLAYER = "CREATE TABLE " + TABLE_PLAYER + "("
                + KEY_PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_PLAYER_NICKNAME + " TEXT UNIQUE,"
                + KEY_PLAYER_POINTS + " INT)";

        db.execSQL(CREATE_TABLE_HOST);
        db.execSQL(CREATE_TABLE_PLAYER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
            onCreate(db);
    }

    public boolean createHost(String username, String nickname, String password) {
        SQLiteDatabase db = null;
        try {
            if (isHostExists()) {
                return true; 
            }
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_HOST_USERNAME, username);
            values.put(KEY_HOST_NICKNAME, nickname);
            values.put(KEY_HOST_PASSWORD, password);

            long result = db.insert(TABLE_HOST, null, values);
            return result != -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    protected boolean isHostExists() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_HOST, null);
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public boolean updateHost(int id, String username, String nickname, String password) {
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_HOST_USERNAME, username);
            values.put(KEY_HOST_NICKNAME, nickname);
            values.put(KEY_HOST_PASSWORD, password);

            int result = db.update(TABLE_HOST, values, KEY_HOST_ID + " = ?", new String[]{String.valueOf(id)});
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public boolean hostValidation(String username, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + TABLE_HOST
                    + " WHERE " + KEY_HOST_USERNAME
                    + " = ? AND " + KEY_HOST_PASSWORD
                    + " = ?", new String[]{username, password});
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public boolean isUsernameTaken(String username) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM "
                    + TABLE_HOST + " WHERE "
                    + KEY_HOST_USERNAME + " = ?", new String[]{username});
            return cursor.getCount() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

}
