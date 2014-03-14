package thu.kejiafan.mobinet;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import com.baidu.mobstat.StatService;

import thu.kejiafan.mobinet.R.id;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GPSFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.gps, container, false);
		initWidget(view);
		initLocation();
		
		handler4GPS.post(runnable4GPS);
		handler4Time.post(runnable4Time);
		
		return view;
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

	private void initWidget(View view) {
    	Config.tvGpsState = (TextView) view.findViewById(id.gpsState);
    	Config.tvSatelliteAvailable = (TextView) view.findViewById(id.satelliteAvailable);
    	Config.tvSatelliteFix = (TextView) view.findViewById(id.satelliteFix);
    	Config.tvCurrentLocation = (TextView) view.findViewById(id.currentLocation);
    	Config.tvCurrentSpeed = (TextView) view.findViewById(id.currentSpeed);
    	Config.tvAccuracy = (TextView) view.findViewById(id.accuracy);
    	Config.tvSystemTime = (TextView) view.findViewById(id.systemTime);
    	Config.tvGpsTime = (TextView) view.findViewById(id.gpsTime);
    	
    	Config.tvGpsState.setText(Config.gpsStateString);
	}
	
	private final LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			// When GPS state change, can capture immediately
			switch (status) {
			case LocationProvider.AVAILABLE:
				Config.gpsStateString = "Available";
				Config.isGPSPrepared = true;
				break;
			case LocationProvider.OUT_OF_SERVICE:
				Config.gpsStateString = "OutOfService";
				Config.mobilitySpeed = "Unknown";
				Config.isGPSPrepared = false;
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Config.gpsStateString = "Unavailable";
				Config.mobilitySpeed = "Unknown";
				Config.isGPSPrepared = false;
				break;
			}
			Config.tvGpsState.setText(Config.gpsStateString);
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			// GPS开启时触发
			Config.loc = Config.locationManager.getLastKnownLocation(provider);
			Config.gpsStateString = "Enabled";
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Config.gpsStateString = "Disabled";
			Config.tvGpsState.setText(Config.gpsStateString);
			Config.mobilitySpeed = "";
			Config.gpsAvailableNumber = "";
			Config.gpsFixNumber = "";
			Config.isGPSPrepared = false;
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			Config.loc = Config.locationManager.getLastKnownLocation(Config.bestProvider);
			String date = Config.contentDateFormat.format(new Date(System.currentTimeMillis()));
			Config.isGPSPrepared = true;
			Config.speed = Config.loc.getSpeed();
			Config.mobilitySpeed = String.valueOf(Config.speed * 3.6) + " km/h";
			Config.latitude = Config.loc.getLatitude();
			Config.longitude = Config.loc.getLongitude();
			Config.accuracy = Config.loc.getAccuracy();
			Config.gpsTime = Config.contentDateFormat.format(Config.loc.getTime());			
			
			Config.tvAccuracy.setText(String.valueOf(Config.accuracy));
			Config.tvCurrentLocation.setText(Config.latitude + " " + Config.longitude);
			Config.tvCurrentSpeed.setText(Config.mobilitySpeed);
			Config.tvGpsTime.setText(Config.gpsTime);
			
			String speedContent = Config.gpsStateString + " " 
					+ Config.mobilitySpeed + " "
					+ String.valueOf(Config.latitude) + " "
					+ String.valueOf(Config.longitude) + " "
					+ String.valueOf(Config.accuracy);
			try {
				if (speedContent.equals(Config.lastSpeedContent)) {
					
				} else {
					Config.lastSpeedContent = speedContent;
					speedContent = date + " " + speedContent;
					if (Config.fosSpeed != null) {
						Config.fosSpeed.write(speedContent.getBytes());
						Config.fosSpeed.write(System.getProperty("line.separator").getBytes());
					}
				}								
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	// 获取当前所连GPS数量
	private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {

		@Override
		public void onGpsStatusChanged(int event) {
			// TODO Auto-generated method stub
			LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			GpsStatus status = locManager.getGpsStatus(null); // 取当前状态

			switch (event) {
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				// 卫星状态改变
				int maxSatellites = status.getMaxSatellites(); // 获取卫星颗数的默认最大值
				Iterator<GpsSatellite> it = status.getSatellites().iterator();
				int gpsAvailableNumber = 0;
				int gpsFixNumber = 0;
				while (it.hasNext() && gpsAvailableNumber <= maxSatellites) {
					GpsSatellite s = it.next();
					gpsAvailableNumber++;
					if (s.usedInFix()) {
						gpsFixNumber++;
					}
				}
				Config.gpsAvailableNumber = String.valueOf(gpsAvailableNumber);
				Config.gpsFixNumber = String.valueOf(gpsFixNumber);
				Config.tvSatelliteAvailable.setText(Config.gpsFixNumber);
				Config.tvSatelliteFix.setText(Config.gpsAvailableNumber);
				break;
			case GpsStatus.GPS_EVENT_STARTED:
				Config.gpsStateString = "Start";
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				Config.gpsStateString = "Stop";
				Config.isGPSPrepared = false;
				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Config.gpsStateString = "FirstFix";
				Config.isGPSPrepared = true;
			default:
				break;
			}
			Config.tvGpsState.setText(Config.gpsStateString);
		}
	};

	private void initCriteria() {
		Config.locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (Config.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| Config.locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Config.criteria = new Criteria();
			Config.criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
			Config.criteria.setAltitudeRequired(true); // 显示海拔
			Config.criteria.setBearingRequired(true); // 显示方向
			Config.criteria.setSpeedRequired(true); // 显示速度
			Config.criteria.setCostAllowed(false); // 不允许有花费
			Config.criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗
			Config.bestProvider = Config.locationManager.getBestProvider(Config.criteria, true);

			// locationManager用来监听定位信息的改变
			Config.locationManager.requestLocationUpdates(Config.bestProvider, 100, 5,
					locationListener);
			Config.locationManager.addGpsStatusListener(statusListener);
			
			initLocation();
		} 		
	}

	private void initLocation() {
		Config.locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		Location gpsLocation = Config.locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(gpsLocation == null){     
			gpsLocation = Config.locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(gpsLocation != null){     
        	Config.latitude = gpsLocation.getLatitude(); //经度     
        	Config.longitude = gpsLocation.getLongitude(); //纬度  
        	Config.tvCurrentLocation.setText(Config.latitude + " " + Config.longitude);
        }
        if (Config.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

		} else {
			Config.gpsStateString = "Disabled";
		}
	}
	
	private Handler handler4GPS = new Handler();

	private Runnable runnable4GPS = new Runnable() {

		@Override
		public void run() {			
			// TODO Auto-generated method stub
			LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				initCriteria();
			} else {
				Config.gpsStateString = "Disabled";
				Config.tvGpsState.setText(Config.gpsStateString);
				handler4GPS.postDelayed(runnable4GPS, 1000);
			}
		}
	};
	
	private Handler handler4Time = new Handler();

	private Runnable runnable4Time = new Runnable() {

		@Override
		public void run() {			
			// TODO Auto-generated method stub
			handler4Time.postDelayed(runnable4Time, 1000);
			long time = System.currentTimeMillis();
			long show = (time - Config.startTime) / 1000;
			Config.totalTime = show / 60 + "'" + show % 60;
			Config.tvSystemTime.setText(Config.sysDateFormat.format(new Date(
					time)) + " 总时长:" + Config.totalTime);
			if (Config.isGPSPrepared) {
				Config.gpsTime = Config.contentDateFormat.format(Config.loc.getTime());	
				Config.tvGpsTime.setText(Config.gpsTime);
			}			
		}
	};
}
