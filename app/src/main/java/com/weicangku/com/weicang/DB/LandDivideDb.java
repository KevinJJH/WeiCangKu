package com.weicangku.com.weicang.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.weicangku.com.weicang.Bean.LandDivide;

import java.util.ArrayList;
import java.util.List;

public class LandDivideDb extends SQLiteOpenHelper {

	public final static int SQLDB_VERSION = 1;
	public final static String LANDDIVIDE = "landDivide";

	public static LandDivideDb myAddress;

	public static LandDivideDb getInstance(Context context) {
		if (myAddress == null)
			myAddress = new LandDivideDb(context);
		return myAddress;
	}

	public LandDivideDb(Context context) {
		super(context, LANDDIVIDE, null, SQLDB_VERSION);
	}

	public LandDivideDb(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String landDivide = "create table " + LANDDIVIDE
				+ "(code varchar(20) unique primary key,"
				+ "name varchar(30) not null,"
				+ "superior varchar(20) not null)";
		db.execSQL(landDivide);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	public boolean insertAddress(LandDivide landDivide) {
		SQLiteDatabase db = myAddress.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("code", landDivide.getCode());
		values.put("name", landDivide.getName());
		values.put("superior", landDivide.getSuperior());
		Long i = db.insert(LANDDIVIDE, null, values);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public List<LandDivide> queryAddress(String selection,
										 String[] selectionArgs) {
		Cursor cursor = null;
		List<LandDivide> list = new ArrayList<LandDivide>();
		SQLiteDatabase db = null;
		try {
			db = myAddress.getWritableDatabase();
			list = new ArrayList<LandDivide>();

			cursor = db.query(LANDDIVIDE, new String[] { "code", "name",
					"superior" }, selection, selectionArgs, null, null, null);

			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				while (!cursor.isAfterLast()) {
					LandDivide land = new LandDivide();
					land.setCode(cursor.getString(0));
					land.setName(cursor.getString(1));
					land.setSuperior(cursor.getString(2));
					list.add(land);
					cursor.moveToNext();
				}
			} else {
				return null;
			}
		} catch (Exception e) {

			// TODO: handle exception
		} finally {
			if(!cursor.isClosed()){
				cursor.close();
				db.close();
			}
		}
		return list;
	}
}