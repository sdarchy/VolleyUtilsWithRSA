package com.archy.networkutilsvolley;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GetMachineInfo {
	/**
	 * 获取设备唯一序列号
	 * 
	 * @return 设备系列号
	 */
	@SuppressWarnings("static-access")
	public static String getMachineKey(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String id = telephonyManager.getDeviceId();
		if (TextUtils.isEmpty(id)) {
			id = android.provider.Settings.Secure.getString(
					context.getContentResolver(),
					android.provider.Settings.Secure.ANDROID_ID);
		}
		return id;
	}

	public static String getAndroidId(Context context) {
		return android.provider.Settings.Secure.getString(
				context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
	}

	/**
	 * TODO 获取设备ip地址
	 * 
	 * @return 设备ip地址
	 */
	public static String getMachineIp(Context context) {
		String IP = null;
		StringBuilder IPStringBuilder = new StringBuilder();
		try {
			Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface
					.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaceEnumeration
						.nextElement();
				Enumeration<InetAddress> inetAddressEnumeration = networkInterface
						.getInetAddresses();
				while (inetAddressEnumeration.hasMoreElements()) {
					InetAddress inetAddress = inetAddressEnumeration
							.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						IPStringBuilder.append(inetAddress.getHostAddress()
								.toString());
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		IP = IPStringBuilder.toString();
		return IP;
	}

	/**
	 * 获取wifi连接ip
	 * 
	 * @param context
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String getWifiIp(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		return (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "."
				+ ((ipAddress >> 16) & 0xFF) + "." + (ipAddress >> 24 & 0xFF);
	}

	/**
	 * 获取Gprs连接ip
	 * 
	 * @return
	 */
	public static String getGprsIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
//			Log.e("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	/**
	 * 获取gprs连接状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (con.getActiveNetworkInfo() != null) {
			return con.getActiveNetworkInfo().isConnected();
		} else {
			return false;
		}
	}
}