package com.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

@SuppressLint("SdCardPath") 
public class DataBaseHelper extends SQLiteOpenHelper {
	
	private static final String DB_PATH = "/data/data/com.active.myjp/databases/";
	private static final String DATABASENAME = "jp.db";
	private static final int DATABASEVERSION = 1;
	private static final String TABLENAME = "myword";

	public DataBaseHelper(Context context) {
		super(context, DB_PATH+DATABASENAME, null, DATABASEVERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE "+TABLENAME+"(id INTEGER PRIMARYKEY,name VARCHAR(50) NOT NULL,birthday DATE NOT NULL)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		String sql = "DROP TABLE IF EXISTS"+TABLENAME;
		db.execSQL(sql);
		this.onCreate(db);
	}

}
