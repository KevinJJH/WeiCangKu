package com.weicangku.com.weicang.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.weicangku.com.weicang.Bean.AddressInfo;

import java.util.ArrayList;
import java.util.List;


public class AddressDb extends SQLiteOpenHelper {

	public final static int SQLDB_VERSION = 1;
	private final static String MY_ADDRESS = "myAddress";
	private final static String ID = "id";
	private final static String PROVINCES = "provinces";
	private final static String STREET = "street";
	private final static String NAME = "name";
	private final static String PHONE = "phone";
	private final static String STATUS = "status";

	public static AddressDb myAddress;

	public static AddressDb getInstance(Context context) {
		if (myAddress == null)
			myAddress = new AddressDb(context);
		return myAddress;
	}

	public AddressDb(Context context){
		super(context,MY_ADDRESS,null,SQLDB_VERSION);
	}

	public AddressDb(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String address = "create table " + MY_ADDRESS
				+ "("+ID+" varchar(20) primary key ,"
				+ PROVINCES+" varchar(100)," +STREET+" varchar(100),"
				+ NAME +" varchar(20)," +PHONE+" varchar(20),"
				+ STATUS +" boolean)";
		db.execSQL(address);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	public boolean insertAddress(AddressInfo address){
		SQLiteDatabase db = myAddress.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ID, address.getId());
		values.put(PROVINCES, address.getProvinces());
		values.put(STREET, address.getStreet());
		values.put(NAME, address.getName());
		values.put(PHONE, address.getPhone());
		values.put(STATUS, address.isStatus());
		Long i = db.insert(MY_ADDRESS, null, values);
		db.close();
		if(i>0){
			return true;
		}else{
			return false;
		}
	}

	public boolean updeteAddress(AddressInfo address){
		SQLiteDatabase db = myAddress.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ID, address.getId());
		values.put(PROVINCES, address.getProvinces());
		values.put(STREET, address.getStreet());
		values.put(NAME, address.getName());
		values.put(PHONE, address.getPhone());
		values.put(STATUS, address.isStatus());
		long i = db.update(MY_ADDRESS, values, ID+"=?", new String[]{address.getId()});
		if(i>0){
			return true;
		}else{
			return false;
		}
	}

	public boolean updeteAddress_test(String id,boolean fdfd){
		SQLiteDatabase db = myAddress.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(STATUS, fdfd);
		long i = db.update(MY_ADDRESS, values, ID+"=? ", new String[]{id});
		db.close();
		if(i>0){

			return true;
		}else{
			return false;
		}
	}

	public List<AddressInfo> queryAddress(){
		SQLiteDatabase db = myAddress.getWritableDatabase();
		List<AddressInfo> list = new ArrayList<AddressInfo>();

		Cursor cursor = db.query(MY_ADDRESS, new String[] { ID, PROVINCES, STREET, NAME, PHONE, STATUS},
				null, null, null, null, null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			while (!cursor.isAfterLast()){
				AddressInfo add = new AddressInfo();
				add.setId((cursor.getString(0)));
				add.setProvinces(cursor.getString(1));
				add.setStreet(cursor.getString(2));
				add.setName(cursor.getString(3));
				add.setPhone(cursor.getString(4));
				String str = cursor.getString(5);
				if(str.equals("0")){
					add.setStatus(false);
				}else{
					add.setStatus(true);
				}
				list.add(add);
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
			return list;
		} else {
			return null;
		}
	}

	public boolean deleteAddress(AddressInfo address){
		SQLiteDatabase db = myAddress.getWritableDatabase();

		int i = db.delete(MY_ADDRESS,  ID+"=?", new String[]{address.getId()});
		db.close();
		if(i>0){
			return true;
		}else{
			return false;
		}
	}
	//查询地址
	public String CursorOneDate(String who){

		String messageString = null;
		SQLiteDatabase db = myAddress.getWritableDatabase();

		Cursor cursor = db.query(MY_ADDRESS, null,
				STATUS+"=?", new String[]{who}, null, null, null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			messageString = cursor.getString(3)+","+
					cursor.getString(4)+","+
					cursor.getString(1)+
					cursor.getString(2);
		}
		cursor.close();
		db.close();

		return messageString;
	}
	public List<AddressInfo> oneAddress(String who){
		SQLiteDatabase db = myAddress.getWritableDatabase();
		List<AddressInfo> list = new ArrayList<AddressInfo>();


		Cursor cursor = db.query(MY_ADDRESS, null,
				STATUS+"=?", new String[]{who}, null, null, null);

		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
				AddressInfo add = new AddressInfo();
				add.setId((cursor.getString(0)));
				add.setProvinces(cursor.getString(1));
				add.setStreet(cursor.getString(2));
				add.setName(cursor.getString(3));
				add.setPhone(cursor.getString(4));
				String str = cursor.getString(5);
				if(str.equals("0")){
					add.setStatus(false);
				}else{
					add.setStatus(true);
				}
				list.add(add);

			cursor.close();
			db.close();
			return list;
		} else {
			return null;
		}
	}

}
