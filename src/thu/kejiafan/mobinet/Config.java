package thu.kejiafan.mobinet;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.res.Resources;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Config {
	// viewpager
	public static ViewPager mPager;
    public static ArrayList<Fragment> fragmentsList;
    public static ImageView ivBottomLine;
    public static TextView tvTabPhone;
	public static TextView tvTabNetwork;
	public static TextView tvTabGPS;
	public static TextView tvTabAbout;

    public static int currIndex = 0;
    public static int bottomLineWidth;
    public static int offset = 0;
    public static int position_one;
    public static int position_two;
    public static int position_three;
    public static Resources resources;
    
    // file for log
    static SimpleDateFormat dirDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    static FileOutputStream fosMobile = null;
	static FileOutputStream fosSignal = null;
	static FileOutputStream fosSpeed = null;
	static FileOutputStream fosCell = null;
	static FileOutputStream fosUplink = null;
	static FileOutputStream fosDownlink = null;
	static FileOutputStream fosPing = null;
	static FileOutputStream fosAddition = null;
	static FileOutputStream fosDNS = null;
	static FileOutputStream fosHTTP = null;
    
	// variable for signalStrength
 	static int gsmSignalStrength = 99; // Valid values are (0-31, 99) as defined in TS 27.007 8.5
 	static int gsmBitErrorRate = -1;   // bit error rate (0-7, 99) as defined in TS 27.007 8.5
 	static int cdmaDbm = -1;   // This value is the RSSI value
 	static int cdmaEcio = -1;  // This value is the Ec/Io
 	static int evdoDbm = -1;   // This value is the EVDO RSSI value
 	static int evdoEcio = -1;  // This value is the EVDO Ec/Io
 	static int evdoSnr = -1;   // Valid values are 0-8.  8 is the highest signal to noise ratio
 	static int lteSignalStrength = -1;
 	static int lteRsrp = -1;
 	static int lteRsrq = -1;
 	static int lteRssnr = -1;
 	static int lteCqi = -1;
 	static int currentLevel = -1;
 	
 	// phone Information
 	static String phoneModel = null;
	static String osVersion = null;
 	static String providerName = null;	
	static String IMEI = null;
	static String IMSI = null;
	static String subtypeName = null;
	
	// phone widget
	static TextView tvPhoneModel;
	static TextView tvosVersion;
	static TextView tvOperator;
	static TextView tvPhoneType;
	static TextView tvNetworkType;
	static TextView tvDataConnection;
	static TextView tvSignalStrength;
	static TextView tvSignalParameter;
	static TextView tvCurrentCell;
	static TextView tvCellLocation;
	static TextView tvHandoffNumber;
	
	// phone state
	static String networkTypeString = "";
	static int netTypeID = 0;
	static String signalStrengthString = "";
	static String SignalParameterString = "";
	static String dataConnectionState = "";
	static int disconnectNumber = 0;
	static int handoffNumber = -1;
	static int lastcellid = -1;
	static boolean lastConnect = false;	
	static TelephonyManager telManager;
	static int phoneEvents = PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
			| PhoneStateListener.LISTEN_SERVICE_STATE
			| PhoneStateListener.LISTEN_CELL_LOCATION
			| PhoneStateListener.LISTEN_DATA_ACTIVITY
			| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE;
	
	// network widget
	static EditText edTargetAddress;
	static Button btnRun;
	static TextView tvDataConnectionState;
	static TextView tvWiFiConnection;
	static TextView tvWiFiInfo;
	static TextView tvMacAddress;
	static TextView tvIPAddress;
	static TextView tvThroughput;
	static TextView tvDNSLatency;
	static TextView tvPingLatency;
	static TextView tvHTTPLatency;
	static TextView tvTestReport;	
	
	// network state
	static String testServerip = "59.66.122.23";
	static String testMeasuretime = "50"; //1
	static String testInterval = "5";
	static String[] measurementNames = { "Downlink Throughput",
			"Uplink Throughput", "Ping Test", "DNS Lookup Test", "HTTP Test" };
	static String[] defaultTarget = { testServerip, testServerip,
			"3g.sina.com.cn", "3g.sina.com.cn", "3g.sina.com.cn" };
	static int measurementID = 0;
	static String addressSina = "3g.sina.com.cn";
	static String addressBaidu = "m.baidu.com";
	static String wifiState = null;
	static String lastWifiState = null;
	static String wifiInfo = null;
	static String macAddress = null;
	static String ipAddress = null;
	static int testFlag = 0;
	static String pingInfo = "";
	static String dnsLookupInfo = "";
	static String httpInfo = "";
	
	// gps widget
	static TextView tvGpsState; 
	static TextView tvSatelliteAvailable;
	static TextView tvSatelliteFix;
	static TextView tvCurrentLocation; 
	static TextView tvCurrentSpeed;
	static TextView tvAccuracy;
	static TextView tvSystemTime;
	static TextView tvGpsTime;
	
	// gps content
	static SimpleDateFormat contentDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
	static SimpleDateFormat sysDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
	static long startTime = 0;
	static String totalTime = "";
	static String gpsStateString = "";
	static String mobilitySpeed = "";
	static float speed;
	static double latitude;
	static double longitude;
	static double accuracy;
	static LocationManager locationManager;
	static Location loc;
	static Criteria criteria;
	static String bestProvider = null;
	static String gpsAvailableNumber = "";
	static String gpsFixNumber = "";
	static String gpsTime = null;
	static boolean isGPSPrepared = false;
	
	// write log content
	static String lastSignalContent = "";
	static String lastSpeedContent = "";
	static String lastCellInfoContent = "";
	static String lastDataContentString = "";
}
