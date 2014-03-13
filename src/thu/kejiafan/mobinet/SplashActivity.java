package thu.kejiafan.mobinet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;

public class SplashActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_screen);
	    // Make sure the splash screen is shown in portrait orientation
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	    
	    // 创建日志路径	    
		try {
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {

			}
			else {
				return;
			}
			String mobilePath = android.os.Environment.getExternalStorageDirectory() + "/MobiNet";
			String pathDate = Config.dirDateFormat.format(new Date(System.currentTimeMillis()));
			File mobileFile = new File(mobilePath);
			mobileFile.mkdirs();
			mobilePath = mobilePath + "/" + pathDate;
			mobileFile = new File(mobilePath);
			mobileFile.mkdirs();
			Config.fosMobile = new FileOutputStream(mobilePath + "/Mobile.txt", true);
			Config.fosSignal = new FileOutputStream(mobilePath + "/Signal.txt", true);
			Config.fosSpeed = new FileOutputStream(mobilePath + "/Speed.txt", true);
			Config.fosCell = new FileOutputStream(mobilePath + "/Cell.txt", true);
			Config.fosUplink = new FileOutputStream(mobilePath + "/Uplink.txt", true);
			Config.fosDownlink = new FileOutputStream(mobilePath + "/Downlink.txt", true);
			Config.fosPing = new FileOutputStream(mobilePath + "/Ping.txt", true);
			Config.fosAddition = new FileOutputStream(mobilePath + "/Addition.txt", true);
			Config.fosDNS = new FileOutputStream(mobilePath + "/DNS.txt", true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
		
        getTelephoneInfo();
		writeLog();
		
	    new Handler().postDelayed(new Runnable() {  
            public void run() {  
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);  
                SplashActivity.this.finish();  
            }
        }, 3000);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub		
		super.onPause();
		
		StatService.onPause(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub		
		super.onResume();
		
		StatService.onResume(this);
	}
	
	private void getTelephoneInfo() {
    	Config.phoneModel = Build.MODEL + "  Inc:" + Build.MANUFACTURER;
		Config.osVersion = Build.VERSION.RELEASE + "  Level:" + Build.VERSION.SDK_INT;
		
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Config.providerName = telephonyManager.getNetworkOperatorName();
		Config.IMSI = telephonyManager.getSubscriberId();
		if (Config.providerName == null) {
			if (Config.IMSI.startsWith("46000")
					|| Config.IMSI.startsWith("46002")
					|| Config.IMSI.startsWith("46007")) {
				Config.providerName = "中国移动";
			} else if (Config.IMSI.startsWith("46001")) {
				Config.providerName = "中国联通";
			} else if (Config.IMSI.startsWith("46003")) {
				Config.providerName = "中国电信";
			} else {
				Config.providerName = "非大陆用户";
			}
		}
		ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		Config.subtypeName = networkInfo.getSubtypeName();
  	}
	
	void writeLog() {
		ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String infoString = "PhoneModel=" + Config.phoneModel + 
				"\nosVersion=" + Config.osVersion + 
				"\nProviderName=" + Config.providerName + 
				"\nDetailedState=" + networkInfo.getDetailedState() + 
				"\nReason=" + networkInfo.getReason() + 
				"\nSubtypeName=" + networkInfo.getSubtypeName() + 
				"\nExtraInfo=" + networkInfo.getExtraInfo() + 
				"\nTypeName=" + networkInfo.getTypeName() + 
				"\nIMEI=" + telephonyManager.getDeviceId() + 
				"\nIMSI=" + telephonyManager.getSubscriberId() + 
				"\nNetworkOperatorName=" + telephonyManager.getNetworkOperatorName() + 
				"\nSimOperatorName=" + telephonyManager.getSimOperatorName() + 
				"\nSimSerialNumber=" + telephonyManager.getSimSerialNumber();
		try {
			if (Config.fosMobile != null) {
				Config.fosMobile.write(infoString.getBytes());
				Config.fosMobile.write(System.getProperty("line.separator").getBytes());
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
