package thu.kejiafan.mobinet;

import com.baidu.mobstat.StatService;

import thu.kejiafan.mobinet.R.id;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class NetworkFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.network, container, false);
		initWidget(view);
		handler4Wifi.post(runnable4Wifi);
		
		return view;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		StatService.onPause(this);
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		StatService.onResume(this);
		super.onResume();
	}
	
	private void initWidget(View view) {
    	Spinner spinner = (Spinner) view.findViewById(id.measurementTypeSpinner);
    	Config.edTargetAddress = (EditText) view.findViewById(id.targetAddress);
    	Config.btnRun = (Button) view.findViewById(id.btnRun);
    	Config.tvDataConnectionState = (TextView) view.findViewById(id.dataConnection);
    	Config.tvWiFiConnection = (TextView) view.findViewById(id.wifiConnection);
		Config.tvMacAddress = (TextView) view.findViewById(id.macAddress);
		Config.tvIPAddress = (TextView) view.findViewById(id.ipAddress); 
		Config.tvWiFiInfo = (TextView) view.findViewById(id.wifiInfo);
		Config.tvPingLatency = (TextView) view.findViewById(id.pingLatency);
		Config.tvDNSLatency = (TextView) view.findViewById(id.dnsLatency);
		Config.tvHTTPLatency = (TextView) view.findViewById(id.httpLatency);
		Config.tvTestReport = (TextView) view.findViewById(id.testState);
    	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, Config.measurementNames);
		// R.layout.spinner_dropdown
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Config.measurementID = arg2;
				Config.edTargetAddress.setText(Config.defaultTarget[arg2]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    Config.btnRun.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub

				if (Config.wifiState.equals("Disconnected")
						&& Config.dataConnectionState.equals("Disconnected")) {
					Config.tvTestReport.setText("网络已断开，请检查网络连接");
					return;
				}

				Config.testFlag = 0;
				String serverIPString = Config.edTargetAddress.getText().toString();

				switch (Config.measurementID) {
				case 0:
					Config.tvTestReport.setText("下行吞吐量 请关注后续版本");
					break;
				case 1:
					Config.tvTestReport.setText("上行吞吐量 请关注后续版本");
					break;
				case 2:
					Config.tvTestReport.setText("Ping testing...");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler4Test.removeCallbacks(runnable4Test);
					handler4Test.post(runnable4Test);
					Measurement.pingCmdTest(serverIPString, 10);
					break;
				case 3:
					Config.tvTestReport.setText("DNS lookup testing...");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler4Test.removeCallbacks(runnable4Test);
					handler4Test.post(runnable4Test);
					Measurement.dnsLookupTest(serverIPString, 10);
					break;
				case 4:
					Config.tvTestReport.setText("HTTP Test请关注后续版本");
					break;
				default:
					Config.tvTestReport.setText("Test does not support");
					break;
				}
			}

		});
	}
	
	private Handler handler4Wifi = new Handler();

	private Runnable runnable4Wifi = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Config.tvDataConnectionState.setText(Config.dataConnectionState);
			/**
			 * 是否连接Wifi
			 */
			try {
				ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

				if (activeNetInfo != null
						&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					
					WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
					WifiInfo wifiInfo = wifiManager.getConnectionInfo();
					Config.wifiState = "Connected:" + wifiInfo.getSSID();
					Config.wifiInfo = "RSSI:" + wifiInfo.getRssi() + " LinkSpeed:" + wifiInfo.getLinkSpeed();
					Config.macAddress = wifiInfo.getMacAddress();
					Config.ipAddress = SignalUtil.int2IP(wifiInfo.getIpAddress());
					Config.tvWiFiConnection.setText(Config.wifiState);
					Config.tvMacAddress.setText(Config.macAddress);
					Config.tvIPAddress.setText(Config.ipAddress);
					
				} else {
					Config.wifiState = "Disconnected";
					Config.tvWiFiConnection.setText(Config.wifiState);
					Config.wifiInfo = "";
				}	
				Config.tvWiFiInfo.setText(Config.wifiInfo);
			} catch (Exception e) {
				// TODO: handle exception
			}

			handler4Wifi.postDelayed(runnable4Wifi, 5000);
		}
	};
	
	private Handler handler4Test = new Handler();

	private Runnable runnable4Test = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			handler4Test.postDelayed(runnable4Test, 1000);
			if (Config.testFlag == 11) {
				Config.tvPingLatency.setText(Config.pingInfo + " ms");
				Config.tvTestReport.setText("Ping test finished");
				Config.testFlag = 10;
			} else if (Config.testFlag == 12) {
				Config.tvTestReport.setText("Ping test failed");
				Config.testFlag = 10;
			} else if (Config.testFlag == 13) {
				Config.tvTestReport.setText("您的机型暂不支持 请关注后续版本");
				Config.testFlag = 10;
			} else if (Config.testFlag == 21) {
				Config.tvDNSLatency.setText(Config.dnsLookupInfo + " ms");
				Config.tvTestReport.setText("DNS lookup test finished");
				Config.testFlag = 20;
			} else if (Config.testFlag == 22) {
				Config.tvTestReport.setText("DNS lookup test failed");
				Config.testFlag = 20;
			} else if (Config.testFlag == 31) {
				Config.tvHTTPLatency.setText(Config.httpInfo);
				Config.tvTestReport.setText("HTTP test finished");
				Config.testFlag = 30;
			} else if (Config.testFlag == 32) {
				Config.tvTestReport.setText("HTTP test failed");
				Config.testFlag = 30;
			}
		}
	};
}