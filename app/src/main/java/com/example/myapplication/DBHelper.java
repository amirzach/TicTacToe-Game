package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TicTacToeDB";
    private static final int DATABASE_VERSION = 6;
    private static final String TABLE_HOST = "HOST";
    private static final String TABLE_PLAYER = "PLAYER";
    private static final String KEY_HOST_ID = "id";
    private static final String KEY_HOST_USERNAME = "username";
    private static final String KEY_HOST_NICKNAME = "nickname";
    private static final String KEY_HOST_PASSWORD = "password";
    private static final String KEY_PLAYER_ID = "id";
    private static final String KEY_PLAYER_NICKNAME = "nickname";
    private static final String KEY_PLAYER_WINS = "wins";
    private static final String KEY_PLAYER_LOSE = "lose";
    private static final String KEY_PLAYER_DRAW = "draw";
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
                + KEY_PLAYER_WINS + " INT DEFAULT 0,"
                + KEY_PLAYER_LOSE + " INT DEFAULT 0,"
                + KEY_PLAYER_DRAW + " INT DEFAULT 0,"
                + KEY_PLAYER_POINTS + " INT DEFAULT 0)";

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
            db = this.getWritableDatabase();

            // Begin transaction
            db.beginTransaction();

            // Insert into TABLE_HOST
            ContentValues hostValues = new ContentValues();
            hostValues.put(KEY_HOST_USERNAME, username);
            hostValues.put(KEY_HOST_NICKNAME, nickname);
            hostValues.put(KEY_HOST_PASSWORD, password);
            long hostResult = db.insert(TABLE_HOST, null, hostValues);

            // Check if host insertion was successful
            if (hostResult == -1) {
                db.endTransaction();
                return false; // Return false if host insertion failed
            }

            // Insert host data into TABLE_PLAYER
            ContentValues playerValues = new ContentValues();
            playerValues.put(KEY_PLAYER_NICKNAME, nickname);
            playerValues.put(KEY_PLAYER_WINS, 0);
            playerValues.put(KEY_PLAYER_LOSE, 0);
            playerValues.put(KEY_PLAYER_DRAW, 0);
            playerValues.put(KEY_PLAYER_POINTS, 0);
            long playerResult = db.insert(TABLE_PLAYER, null, playerValues);

            // Check if host insertion in table player was successful
            if (playerResult == -1) {
                db.endTransaction();
                return false; // Return false if player insertion failed
            }

            // Commit transaction
            db.setTransactionSuccessful();
            return true; // Return true if both insertions were successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false in case of exception
        } finally {
            // End transaction and close database
            if (db != null) {
                db.endTransaction();
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

    public ArrayList<Float> getPoints(){
        SQLiteDatabase db = null;

        ArrayList<Float> result = new ArrayList<>();

        float points;
        db=this.getReadableDatabase();
        String query = "SELECT points FROM "+ TABLE_PLAYER;
        Cursor cursor=db.rawQuery(query,null);
        int iPoints=cursor.getColumnIndex(KEY_PLAYER_POINTS);
        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            points =cursor.getFloat(iPoints);
            result.add(points);

        }
        cursor.close();
        db.close();
        return result;
    }

    public void updatePlayerPoints(String nickname, int points) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER_POINTS, points);
        db.update(TABLE_PLAYER, values, KEY_PLAYER_NICKNAME + " = ?", new String[]{nickname});
    }
    public void updatePlayerWin(String playerName, int increment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE PLAYER SET wins = wins + ? WHERE nickname = ?", new Object[]{increment, playerName});
    }

    public void updatePlayerLose(String playerName, int increment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE PLAYER SET lose = lose + ? WHERE nickname = ?", new Object[]{increment, playerName});
    }

    public void updatePlayerDraw(String playerName, int increment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE PLAYER SET draw = draw + ? WHERE nickname = ?", new Object[]{increment, playerName});
    }


    public int getPlayerPoints(String nickname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLAYER, new String[]{KEY_PLAYER_POINTS}, KEY_PLAYER_NICKNAME + " = ?", new String[]{nickname}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(KEY_PLAYER_POINTS);
            if (columnIndex != -1) {
                int points = cursor.isNull(columnIndex) ? 0 : cursor.getInt(columnIndex);
                cursor.close();
                return points;
            } else {
                Log.e("DBHelper", "Column " + KEY_PLAYER_POINTS + " not found");
                cursor.close();
                return 0; // Default to 0 if the column is not found
            }
        } else {
            return 0; // Default to 0 if the player is not found
        }
    }

    // Method to get all players in descending order
    public List<Player> getAllPlayersDesc() {
        List<Player> playerList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PLAYER + " ORDER BY " + KEY_PLAYER_POINTS + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Player player = new Player(
                        cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PLAYER_NICKNAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_WINS)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_LOSE)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_DRAW)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_POINTS)));
                playerList.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return playerList;
    }

    // Method to search players by name
    public List<Player> searchPlayersByName(String nickname) {
        List<Player> playerList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PLAYER + " WHERE " + KEY_PLAYER_NICKNAME + " LIKE ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{"%" + nickname + "%"});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Player player = new Player(
                        cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_PLAYER_NICKNAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_WINS)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_LOSE)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_DRAW)),
                        cursor.getInt(cursor.getColumnIndex(KEY_PLAYER_POINTS)));
                playerList.add(player);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return playerList;
    }
    public int getHostWins() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT wins FROM " + TABLE_PLAYER + " WHERE " + KEY_PLAYER_ID + " = 1"; // Host ID is 1
        Cursor cursor = db.rawQuery(query, null);
        int wins = 0;
        if (cursor.moveToFirst()) {
            wins = cursor.getInt(cursor.getColumnIndexOrThrow("wins"));
        }
        cursor.close();
        return wins;
    }

    public int getHostLosses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT lose FROM " + TABLE_PLAYER + " WHERE " + KEY_PLAYER_ID + " = 1"; // Host ID is 1
        Cursor cursor = db.rawQuery(query, null);
        int losses = 0;
        if (cursor.moveToFirst()) {
            losses = cursor.getInt(cursor.getColumnIndexOrThrow("lose"));
        }
        cursor.close();
        return losses;
    }

    public int getHostDraws() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT draw FROM " + TABLE_PLAYER + " WHERE " + KEY_PLAYER_ID + " = 1"; // Host ID is 1
        Cursor cursor = db.rawQuery(query, null);
        int draws = 0;
        if (cursor.moveToFirst()) {
            draws = cursor.getInt(cursor.getColumnIndexOrThrow("draw"));
        }
        cursor.close();
        return draws;
    }


}
