package com.app.aztask.util;

import java.util.UUID;

import com.app.aztask.DeviceLocation;
import com.app.aztask.ui.MainActivity;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Util {
	
	//public static String SERVER_URL="http://10.1.19.179:9000";
	public static String SERVER_URL="http://aztask-demo.herokuapp.com";
    public static String PROJECT_NUMBER = "155962838252";
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

	
	

	public static String getDeviceId() {

		final TelephonyManager tm = (TelephonyManager) MainActivity.getAppContext()
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		Log.i("MainActivity", "Original Device Id:" + tm.getDeviceId());

		tmSerial = "" + tm.getSimSerialNumber();
		androidId = "" + android.provider.Settings.Secure.getString(MainActivity.getAppContext().getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String deviceId = deviceUuid.toString();

		return deviceId;
	}
	
	public static Location getDeviceLocation() {
		

		LocationManager lm = (LocationManager) MainActivity.getAppContext().getSystemService(Context.LOCATION_SERVICE);

		DeviceLocation locationListener = new DeviceLocation();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location==null){
			Log.e("Util", "The application couldn't get real coordinates, so getting home location.");
			location=new Location(LocationManager.GPS_PROVIDER);
			location.setLatitude(65.9667d);
			location.setLongitude(-18.5333d);
		}
		return location;
	}
	



}
