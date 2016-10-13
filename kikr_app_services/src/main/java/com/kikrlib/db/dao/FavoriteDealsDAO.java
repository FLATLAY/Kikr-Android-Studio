package com.kikrlib.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kikrlib.bean.TopDeals;
import com.kikrlib.db.table.FavoriteDealsTable;
import com.kikrlib.utils.Syso;

public class FavoriteDealsDAO extends AbsDAO<TopDeals> {

	public FavoriteDealsDAO(SQLiteDatabase database) {
		super(database);
	}

	@Override
	public List<TopDeals> createList(Cursor cursor) {
		List<TopDeals> list = new ArrayList<TopDeals>();
		if(cursor!=null){
			Syso.debug(TAG, "cursor size = " + cursor.getCount());
			for (int i = 0; i < cursor.getCount(); i++) {
				list.add(createObject(cursor));
				cursor.moveToNext();
			}
		}
		return list;
	}

	@Override
	public TopDeals createObject(Cursor cursor) {
		TopDeals data = new TopDeals();
		if (cursor != null) {
			data.setDealid(cursor.getString(cursor.getColumnIndex(FavoriteDealsTable.FavoriteDealsListColumns.DEALID)));
			data.setDescription(cursor.getString(cursor.getColumnIndex(FavoriteDealsTable.FavoriteDealsListColumns.DESCRIPTION)));
			data.setExpiryTime(cursor.getString(cursor.getColumnIndex(FavoriteDealsTable.FavoriteDealsListColumns.EXPIRYTIME)));
			data.setImagelink(cursor.getString(cursor.getColumnIndex(FavoriteDealsTable.FavoriteDealsListColumns.IMAGELINK)));
			data.setLink(cursor.getString(cursor.getColumnIndex(FavoriteDealsTable.FavoriteDealsListColumns.LINK)));
			data.setMerchantName(cursor.getString(cursor.getColumnIndex(FavoriteDealsTable.FavoriteDealsListColumns.MERCHANTNAME)));
			data.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteDealsTable.FavoriteDealsListColumns.TITLE)));
			data.setFavorite(true);
			}
		return data;
	}
	
	public List<String> createDealidList(Cursor cursor) {
		List<String> list = new ArrayList<String>();
		if(cursor!=null){
			Syso.debug(TAG, "cursor size = " + cursor.getCount());
			for (int i = 0; i < cursor.getCount(); i++) {
				list.add(createDealidObject(cursor));
				cursor.moveToNext();
			}
		}
		return list;
	}
	
	public String createDealidObject(Cursor cursor) {
		String data = "";
		if (cursor != null) {
			data = cursor.getString(cursor.getColumnIndex(FavoriteDealsTable.FavoriteDealsListColumns.DEALID));
		}
		return data;
	}

	@Override
	public void delete(String dealid) {
		String whereClause =FavoriteDealsTable.FavoriteDealsListColumns.DEALID + " = " + dealid;
		db.delete(FavoriteDealsTable.NAME, whereClause, null);
	}

	@Override
	public void delete() {
		db.delete(FavoriteDealsTable.NAME, null, null);
	}

	@Override
	public Cursor getCursor() {
		try{
			Cursor cursor = db.query(FavoriteDealsTable.NAME, null, null, null, null, null, null);
			if(cursor!=null)
				cursor.moveToFirst();
			return cursor;
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
	}

	@Override
	public Cursor getCursor(String dealid) {
		String whereClause = FavoriteDealsTable.FavoriteDealsListColumns.DEALID + " = '" + dealid+"'";
		Cursor cursor = db.query(FavoriteDealsTable.NAME, null, whereClause, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	@Override
	public List<TopDeals> getList() {
		try{
			Cursor cursor = getCursor();
			return createList(cursor);
			}catch(Exception e){
				e.printStackTrace();
				return new ArrayList<TopDeals>();
			}
	}
	
	public List<String> getDealIdList() {
		try{
			Cursor cursor = getCursor();
			return createDealidList(cursor);
			}catch(Exception e){
				e.printStackTrace();
				return new ArrayList<String>();
			}
	}

	@Override
	public int getSize() {
		return getCursor().getCount();
	}

	@Override
	public long insert(TopDeals data) {
		try {
			Syso.debug(TAG, "insert data = " + data);
			ContentValues cv = new ContentValues();
			cv.put(FavoriteDealsTable.FavoriteDealsListColumns.DEALID, data.getDealid());
			cv.put(FavoriteDealsTable.FavoriteDealsListColumns.DESCRIPTION, data.getDescription());
			cv.put(FavoriteDealsTable.FavoriteDealsListColumns.EXPIRYTIME, data.getExpiryTime());
			cv.put(FavoriteDealsTable.FavoriteDealsListColumns.IMAGELINK, data.getImagelink());
			cv.put(FavoriteDealsTable.FavoriteDealsListColumns.LINK, data.getLink());
			cv.put(FavoriteDealsTable.FavoriteDealsListColumns.TITLE, data.getTitle());
			cv.put(FavoriteDealsTable.FavoriteDealsListColumns.MERCHANTNAME, data.getMerchantName());
			long result = db.insert(FavoriteDealsTable.NAME, null, cv);
//			LogUtils.debug(TAG, "insert result = " + result);
			return result;
		} catch (Exception er) {
			Syso.error(TAG, "insert error = " + er.getMessage());
			return -1;
		}
	}

	@Override
	public void insertBulk(List<TopDeals> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertOrUpdate(TopDeals data) {
		// TODO Auto-generated method stub
		
	}
	
}
