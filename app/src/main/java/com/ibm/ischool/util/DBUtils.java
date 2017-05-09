package com.ibm.ischool.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBUtils extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "ischool.db";//数据库名称 
	
    private static final int SCHEMA_VERSION = 1;
	
	public DBUtils(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS lesson_detail (_id INTEGER PRIMARY KEY AUTOINCREMENT, levelId TEXT, chapterId TEXT, lessonId TEXT, lessonScore INTEGER, lessonStar INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
