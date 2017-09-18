package com.example.BOLO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	public DatabaseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        String createQuery = "CREATE TABLE device (name text primary key ,type text);";                 
        db.execSQL(createQuery);	
        String createQuery1 = "CREATE TABLE command (cname text,name text,value text,bits text,decodetype text);";                 
        db.execSQL(createQuery1);	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropQuery="DROP TABLE device;";
        db.execSQL(dropQuery);
		String createQuery = "CREATE TABLE device (name text primary key ,type text);";                 
        db.execSQL(createQuery);	
	}

}
