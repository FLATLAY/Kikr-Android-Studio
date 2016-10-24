package com.flatlaylib.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.flatlaylib.bean.Uuid;
import com.flatlaylib.db.table.UuidListTable;
import com.flatlaylib.utils.Syso;

public class UuidDAO extends AbsDAO<Uuid> {
	private String defaultInRange="false";

	public UuidDAO(SQLiteDatabase database) {
		super(database);
	}

	public long insert(Uuid data) {
		try {
			Syso.debug(TAG, "insert data = " + data);
			
			ContentValues cv = new ContentValues();
			cv.put(UuidListTable.UuidListColumns.uuid, data.getUuid());
			cv.put(UuidListTable.UuidListColumns.inrange,defaultInRange);
			long result = db.insert(UuidListTable.NAME, null, cv);
//			LogUtils.debug(TAG, "insert result = " + result);
			return result;
		} catch (Exception er) {
			Syso.error(TAG, "insert error = " + er.getMessage());
			return -1;
		}
	}

	public void insertOrUpdate(Uuid data) {

		if (isUuidExist(data.getUuid())) {

		} else {
			insert(data);
		}
	}
	
	public boolean isUuidExist(String uuid){
		if(getCursor(uuid).getCount() > 0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isUuidInRange(String uuid){
		Cursor cursor=getCursor(uuid);
		if(cursor.getString(cursor.getColumnIndex(UuidListTable.UuidListColumns.inrange)).equals("true")){
			return true;
		}else{
			return false;
		}
	}
	
	public void updateUuidStatus(ArrayList<String> uuidList){
		List<Uuid> uuids=getList();
		for(int i=0;i<uuids.size();i++){
			if(uuidList.contains(uuids.get(i).getUuid())){
				updateUuidRange(uuids.get(i).getUuid(),"true");
			}else{
				updateUuidRange(uuids.get(i).getUuid(),"false");
			}
		}
	}
	
	private void updateUuidRange(String uuid, String inrange) {
		try{
			ContentValues values=new ContentValues();
			values.put(UuidListTable.UuidListColumns.inrange, inrange);
			int update=db.update(UuidListTable.NAME, values, UuidListTable.UuidListColumns.uuid+"='"+uuid+"'", null);
//			LogUtils.info("Update uuid: "+update +" ,"+inrange+" uuid>>"+uuid);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String uuid) {
		String whereClause = UuidListTable.UuidListColumns.uuid + " = " + uuid;
		db.delete(UuidListTable.NAME, whereClause, null);
	}

	@Override
	public void delete() {
		db.delete(UuidListTable.NAME, null, null);
	}

	@Override
	public Cursor getCursor() {
		try{
		Cursor cursor = db.query(UuidListTable.NAME, null, null, null, null, null, null);
		if(cursor!=null)
			cursor.moveToFirst();
		return cursor;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Cursor getCursor(String uuid) {
		String whereClause = UuidListTable.UuidListColumns.uuid + " = '" + uuid+"'";
		Cursor cursor = db.query(UuidListTable.NAME, null, whereClause, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	@Override
	public int getSize() {
		return getCursor().getCount();
	}

	@Override
	public List<Uuid> getList() {
		try{
		Cursor cursor = getCursor();
		return createList(cursor);
		}catch(Exception e){
			e.printStackTrace();
			return new ArrayList<Uuid>();
		}
	}

	@Override
	public List<Uuid> createList(Cursor cursor) {
		List<Uuid> list = new ArrayList<Uuid>();
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
	public Uuid createObject(Cursor cursor) {
		Uuid data = new Uuid();
		if (cursor != null) {
			data.setUuid(cursor.getString(cursor.getColumnIndex(UuidListTable.UuidListColumns.uuid)));
		}
		return data;
	}

	@Override
	public void insertBulk(List<Uuid> list) {
		// TODO Auto-generated method stub

	}

}
