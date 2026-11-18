package com.example.btth_app_03;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appointments.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsersTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)";
        db.execSQL(createUsersTable);

        // Create appointments table
        String createAppointmentsTable = "CREATE TABLE appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "location TEXT NOT NULL, " +
                "date TEXT NOT NULL, " +
                "time TEXT NOT NULL, " +
                "reminder INTEGER NOT NULL)";
        db.execSQL(createAppointmentsTable);

        // Insert sample user data
        db.execSQL("INSERT INTO users (username, password) VALUES ('admin', '123456')");

        // Insert sample appointments data
        db.execSQL("INSERT INTO appointments (title, location, date, time, reminder) VALUES ('Họp nhóm đồ án', 'Phòng B203', '2026-10-25', '09:00', 1)");
        db.execSQL("INSERT INTO appointments (title, location, date, time, reminder) VALUES ('Khám răng', 'Nha khoa Minh Tâm', '2026-10-26', '14:30', 1)");
        db.execSQL("INSERT INTO appointments (title, location, date, time, reminder) VALUES ('Đi cà phê với bạn', 'Highland Coffee', '2026-10-27', '19:00', 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS appointments");
        onCreate(db);
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public void insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        db.insert("users", null, values);
    }

    public long insertAppointment(Appointment a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", a.getTitle());
        values.put("location", a.getLocation());
        values.put("date", a.getDate());
        values.put("time", a.getTime());
        values.put("reminder", a.getReminder());
        return db.insert("appointments", null, values);
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM appointments ORDER BY date ASC, time ASC", null);

        if (cursor.moveToFirst()) {
            do {
                Appointment a = new Appointment();
                a.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                a.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                a.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
                a.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                a.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
                a.setReminder(cursor.getInt(cursor.getColumnIndexOrThrow("reminder")));
                list.add(a);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Appointment getAppointmentById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM appointments WHERE id = ?", new String[]{String.valueOf(id)});
        Appointment a = null;

        if (cursor.moveToFirst()) {
            a = new Appointment();
            a.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            a.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            a.setLocation(cursor.getString(cursor.getColumnIndexOrThrow("location")));
            a.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            a.setTime(cursor.getString(cursor.getColumnIndexOrThrow("time")));
            a.setReminder(cursor.getInt(cursor.getColumnIndexOrThrow("reminder")));
        }
        cursor.close();
        return a;
    }

    public void updateReminder(int id, boolean on) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("reminder", on ? 1 : 0);
        db.update("appointments", values, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteAppointment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("appointments", "id = ?", new String[]{String.valueOf(id)});
    }

    public int countAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM appointments", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int countReminders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM appointments WHERE reminder = 1", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
