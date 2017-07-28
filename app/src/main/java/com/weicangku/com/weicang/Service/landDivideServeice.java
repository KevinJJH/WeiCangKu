package com.weicangku.com.weicang.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.weicangku.com.weicang.Bean.LandDivide;
import com.weicangku.com.weicang.DB.LandDivideDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class landDivideServeice extends IntentService {

	public landDivideServeice() {
		super("landDivideServeice");
		// TODO Auto-generated constructor stub
	}

	private String[] txt = new String[] { "city1.txt", "city2.txt",
			"city3.txt", "city4.txt", "city5.txt", "city6.txt", "city7.txt" };
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		LandDivideDb landDivideDB = LandDivideDb
				.getInstance(getBaseContext());
		List<LandDivide> list = landDivideDB.queryAddress(null, null);
		if (list == null) {
			for (int i = 0; i < txt.length; i++) {
				String str = getAssetString(txt[i], getBaseContext());
				try {
					str = str.substring(1);
					JSONArray jsonArray = new JSONArray(str);
					for (int j = 0; j < jsonArray.length(); j++) {
						JSONObject temp = jsonArray.getJSONObject(j);
						LandDivide landDivide = new LandDivide();
						landDivide.setCode(temp.getString("code"));
						landDivide.setName(temp.getString("name"));
						landDivide.setSuperior(temp
								.getString("superior"));
						landDivideDB.insertAddress(landDivide);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} else {
			for (int i = 0; i < txt.length; i++) {
				String str = getAssetString(txt[i], getBaseContext());
				try {
					str = str.substring(1);
					JSONArray jsonArray = new JSONArray(str);
					for (int j = 0; j < jsonArray.length(); j++) {
						JSONObject temp = jsonArray.getJSONObject(j);
						LandDivide landDivide = new LandDivide();
						landDivide.setCode(temp.getString("code"));
						landDivide.setName(temp.getString("name"));
						landDivide.setSuperior(temp
								.getString("superior"));
						
						List<LandDivide> mList = landDivideDB.queryAddress("code=?", new String[]{temp.getString("code")});
						if(mList==null){
							landDivideDB.insertAddress(landDivide);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getAssetString(String asset, Context context) {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(context
					.getAssets().open(asset), "utf-8"));
			String line = null;
			StringBuilder builder = new StringBuilder();
			while (null != (line = bufferedReader.readLine())) {
				builder.append(line).append("\n");
			}
			return builder.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != bufferedReader) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			bufferedReader = null;
		}
		return "";
	}

}