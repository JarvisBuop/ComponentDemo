package com.jarvisdong.uikit.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

public class NetworkUtil {

	public static final String TAG = "NetworkUtil";

	public static final byte CURRENT_NETWORK_TYPE_NONE = 0;

	/*
	 * 根据APN区分网络类型
	 */
	public static final byte CURRENT_NETWORK_TYPE_WIFI = 1;// wifi

	public static final byte CURRENT_NETWORK_TYPE_CTNET = 2;// ctnet

	public static final byte CURRENT_NETWORK_TYPE_CTWAP = 3;// ctwap

	public static final byte CURRENT_NETWORK_TYPE_CMWAP = 4;// cmwap

	public static final byte CURRENT_NETWORK_TYPE_UNIWAP = 5;// uniwap,3gwap

	public static final byte CURRENT_NETWORK_TYPE_CMNET = 6;// cmnet

	public static final byte CURRENT_NETWORK_TYPE_UNIET = 7;// uninet,3gnet

	/**
	 * 根据运营商区分网络类型
	 */
	public static final byte CURRENT_NETWORK_TYPE_CTC = 10;// ctwap,ctnet

	public static final byte CURRENT_NETWORK_TYPE_CUC = 11;// uniwap,3gwap,uninet,3gnet

	public static final byte CURRENT_NETWORK_TYPE_CM = 12;// cmwap,cmnet

	/**
	 * apn值
	 */
	private static final String CONNECT_TYPE_WIFI = "wifi";

	private static final String CONNECT_TYPE_CTNET = "ctnet";

	private static final String CONNECT_TYPE_CTWAP = "ctwap";

	private static final String CONNECT_TYPE_CMNET = "cmnet";

	private static final String CONNECT_TYPE_CMWAP = "cmwap";

	private static final String CONNECT_TYPE_UNIWAP = "uniwap";

	private static final String CONNECT_TYPE_UNINET = "uninet";

	private static final String CONNECT_TYPE_UNI3GWAP = "3gwap";

	private static final String CONNECT_TYPE_UNI3GNET = "3gnet";

	private static final Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

	public static byte curNetworkType = CURRENT_NETWORK_TYPE_NONE;
	
	
	/**
	 * 判断当前网络类型。WIFI,NET,WAP
	 * 
	 * @param context
	 * @return
	 */
	public static byte getCurrentNetType(Context context) {
		NetworkInfo networkInfo = getActiveNetworkInfo(context);
		byte type = CURRENT_NETWORK_TYPE_NONE;
		if (networkInfo != null) {
			// String typeName = networkInfo.getTypeName();
			// XT800
			String typeName = networkInfo.getExtraInfo();
			if (TextUtils.isEmpty(typeName)) {
				typeName = networkInfo.getTypeName();
			}
			if (!TextUtils.isEmpty(typeName)) {
				String temp = typeName.toLowerCase();
				if (temp.indexOf(CONNECT_TYPE_WIFI) > -1) {// wifi
					type = CURRENT_NETWORK_TYPE_WIFI;
				} else if (temp.indexOf(CONNECT_TYPE_CTNET) > -1) {// ctnet
					type = CURRENT_NETWORK_TYPE_CTNET;
				} else if (temp.indexOf(CONNECT_TYPE_CTWAP) > -1) {// ctwap
					type = CURRENT_NETWORK_TYPE_CTWAP;
				} else if (temp.indexOf(CONNECT_TYPE_CMNET) > -1) {// cmnet
					type = CURRENT_NETWORK_TYPE_CMNET;
				} else if (temp.indexOf(CONNECT_TYPE_CMWAP) > -1) {// cmwap
					type = CURRENT_NETWORK_TYPE_CMWAP;
				} else if (temp.indexOf(CONNECT_TYPE_UNIWAP) > -1) {// uniwap
					type = CURRENT_NETWORK_TYPE_UNIWAP;
				} else if (temp.indexOf(CONNECT_TYPE_UNI3GWAP) > -1) {// 3gwap
					type = CURRENT_NETWORK_TYPE_UNIWAP;
				} else if (temp.indexOf(CONNECT_TYPE_UNINET) > -1) {// uninet
					type = CURRENT_NETWORK_TYPE_UNIET;
				} else if (temp.indexOf(CONNECT_TYPE_UNI3GNET) > -1) {// 3gnet
					type = CURRENT_NETWORK_TYPE_UNIET;
				}
			}
		}
	
		if (type == CURRENT_NETWORK_TYPE_NONE) {
			String apnType = getApnType(context);
			if (apnType != null && apnType.equals(CONNECT_TYPE_CTNET)) {// ctnet
				type = CURRENT_NETWORK_TYPE_CTNET;
			} else if (apnType != null && apnType.equals(CONNECT_TYPE_CTWAP)) {// ctwap
				type = CURRENT_NETWORK_TYPE_CTWAP;
			} else if (apnType != null && apnType.equals(CONNECT_TYPE_CMWAP)) {// cmwap
				type = CURRENT_NETWORK_TYPE_CMWAP;
			} else if (apnType != null && apnType.equals(CONNECT_TYPE_CMNET)) {// cmnet
				type = CURRENT_NETWORK_TYPE_CMNET;
			} else if (apnType != null && apnType.equals(CONNECT_TYPE_UNIWAP)) {// uniwap
				type = CURRENT_NETWORK_TYPE_UNIWAP;
			} else if (apnType != null && apnType.equals(CONNECT_TYPE_UNI3GWAP)) {// 3gwap
				type = CURRENT_NETWORK_TYPE_UNIWAP;
			} else if (apnType != null && apnType.equals(CONNECT_TYPE_UNINET)) {// uninet
				type = CURRENT_NETWORK_TYPE_UNIET;
			} else if (apnType != null && apnType.equals(CONNECT_TYPE_UNI3GNET)) {// 3gnet
				type = CURRENT_NETWORK_TYPE_UNIET;
			}
		}
		curNetworkType = type;

		return type;
	}

	/**
	 * 判断APNTYPE
	 *
	 * @param context
	 * @return
	 */
	/**
	 * @deprecated 4.0
	 * doc:
	 * Since the DB may contain corp passwords, we should secure it. Using the same permission as writing to the DB as the read is potentially as damaging as a write
	 */
	public static String getApnType(Context context) {

		String apntype = "nomatch";
		Cursor c = context.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
		if (c != null) {
			if (c.moveToFirst()) {
				String user = c.getString(c.getColumnIndex("user"));
				if (user != null && user.startsWith(CONNECT_TYPE_CTNET)) {
					apntype = CONNECT_TYPE_CTNET;
				} else if (user != null && user.startsWith(CONNECT_TYPE_CTWAP)) {
					apntype = CONNECT_TYPE_CTWAP;
				} else if (user != null && user.startsWith(CONNECT_TYPE_CMWAP)) {
					apntype = CONNECT_TYPE_CMWAP;
				} else if (user != null && user.startsWith(CONNECT_TYPE_CMNET)) {
					apntype = CONNECT_TYPE_CMNET;
				} else if (user != null && user.startsWith(CONNECT_TYPE_UNIWAP)) {
					apntype = CONNECT_TYPE_UNIWAP;
				} else if (user != null && user.startsWith(CONNECT_TYPE_UNINET)) {
					apntype = CONNECT_TYPE_UNINET;
				} else if (user != null && user.startsWith(CONNECT_TYPE_UNI3GWAP)) {
					apntype = CONNECT_TYPE_UNI3GWAP;
				} else if (user != null && user.startsWith(CONNECT_TYPE_UNI3GNET)) {
					apntype = CONNECT_TYPE_UNI3GNET;
				}
			}
			c.close();
			c = null;
		}

		return apntype;
	}

	/**
	 * 判断是否有网络可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetAvailable(Context context) {
		NetworkInfo networkInfo = getActiveNetworkInfo(context);
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		} else {
			return false;
		}
	}

	/**
	 * 此判断不可靠
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		NetworkInfo networkInfo = getActiveNetworkInfo(context);
		if (networkInfo != null) {
			boolean a = networkInfo.isConnected();
			return a;
		} else {
			return false;
		}
	}

	/**
	 * 获取可用的网络信息
	 * 
	 * @param context
	 * @return
	 */
	private static NetworkInfo getActiveNetworkInfo(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			return cm.getActiveNetworkInfo();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 当前网络是否是wifi网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if (ni != null) {
				if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
