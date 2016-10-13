package com.kikrlib.db.table;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class UuidListTable {
	
	public static final String NAME = "UuidList";
	
	public interface UuidListColumns extends BaseColumns {
		
		String uuid = "uuid";
		String inrange = "inrange";
	}
	
	public static void createTable(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + UuidListTable.NAME + '('
				+ UuidListColumns._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ UuidListColumns.inrange +" TEXT,"
				+ UuidListColumns.uuid +" TEXT);" );
	}
	
	public static void dropTable(SQLiteDatabase db){
		
		db.execSQL("DROP TABLE IF EXISTS " + UuidListTable.NAME);
	}
}
