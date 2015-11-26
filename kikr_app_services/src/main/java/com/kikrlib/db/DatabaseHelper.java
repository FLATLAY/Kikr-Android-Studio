package com.kikrlib.db;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kikrlib.AppContext;
import com.kikrlib.db.table.FavoriteDealsTable;
import com.kikrlib.db.table.UuidListTable;


public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "kikr.db";
	public static final int DATABASE_VERSION = 2;

	private static DatabaseHelper INSTANCE;
	
	public static DatabaseHelper getIntance(Application context){
		if(INSTANCE == null){
			INSTANCE = new DatabaseHelper(context);	
		}
		return INSTANCE;
	}
	
	public static SQLiteDatabase getDatabase(){
		getIntance(AppContext.getInstance().getContext());
		return INSTANCE.getWritableDatabase();
	}

	private DatabaseHelper(Application context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		synchronized (getClass()) {
			createTables(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);	
		createTables(db);
	}
	
	public void resetAllTables(SQLiteDatabase db){
		dropTables(db);
		createTables(db);
//		AppPreference.getInstance().clearAllData();
	}
	
	private void createTables(SQLiteDatabase db) {
		UuidListTable.createTable(db);
		FavoriteDealsTable.createTable(db);
	}
	
	private void dropTables(SQLiteDatabase db){
		UuidListTable.dropTable(db);
		FavoriteDealsTable.dropTable(db);
	}
}
