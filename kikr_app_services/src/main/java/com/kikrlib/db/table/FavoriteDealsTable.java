package com.kikrlib.db.table;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class FavoriteDealsTable {
	
	public static final String NAME = "FavoriteDeals";
	
	public interface FavoriteDealsListColumns extends BaseColumns {
		String DESCRIPTION = "description";
		String DEALID = "dealid";
		String LINK = "link";
		String IMAGELINK = "imagelink";
		String MERCHANTNAME = "merchant";
		String EXPIRYTIME = "expirydate";
		String TITLE = "title";
	}
	
	public static void createTable(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE " + FavoriteDealsTable.NAME + '('
				+ FavoriteDealsListColumns._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
				+ FavoriteDealsListColumns.DESCRIPTION +" TEXT,"
				+ FavoriteDealsListColumns.DEALID +" TEXT,"
				+ FavoriteDealsListColumns.LINK +" TEXT,"
				+ FavoriteDealsListColumns.IMAGELINK +" TEXT,"
				+ FavoriteDealsListColumns.EXPIRYTIME +" TEXT,"
				+ FavoriteDealsListColumns.TITLE +" TEXT,"
				+ FavoriteDealsListColumns.MERCHANTNAME +" TEXT);" );
	}
	
	public static void dropTable(SQLiteDatabase db){
		
		db.execSQL("DROP TABLE IF EXISTS " + FavoriteDealsTable.NAME);
	}
}
