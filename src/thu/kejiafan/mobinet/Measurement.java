package thu.kejiafan.mobinet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class Measurement {
	public static void pingCmdTest(final String target, final int count) {
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				
				String res[] = new String[30];
				String cmd = "ping " + target;
				
				try {
					int i = 0;
					Process process = Runtime.getRuntime().exec(cmd);
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(process.getInputStream()));
					while (i < count+1 && (res[i] = bufferedReader.readLine()) != null) {
						i++;
					}
					double avg = 0;
					for (i = 0; i < count; i++) {
						if (res[i+1].contains("time=")) {
							
						} else {
							Config.testFlag = 13;
							break;
						}
						String[] tmp = res[i+1].split("time=");
						String[] tmp2 = tmp[1].split(" ");
						avg += Double.parseDouble(tmp2[0]);
					}
					Config.pingInfo = String.valueOf(avg/count);
					Config.testFlag = 11;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Config.testFlag = 12;
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public static void pingJavaTest(final String target, final int count) {
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				long pingStartTime = 0;
				long pingEndTime = 0;
				ArrayList<Double> rrts = new ArrayList<Double>();

				try {
					int timeOut = 3000;
					long totalPingDelay = 0;
					String output = Config.contentDateFormat.format(new Date(System.currentTimeMillis())) 
							+ " " + target + " " + count + "\n";
					for (int i = 0; i < count; i++) {
						pingStartTime = System.currentTimeMillis();
						boolean status = InetAddress.getByName(target).isReachable(timeOut);
						pingEndTime = System.currentTimeMillis();
						long rrtVal = pingEndTime - pingStartTime;
						if (status) {
							totalPingDelay += rrtVal;
							rrts.add((double) rrtVal);
							output += i + ": " + rrtVal + "\n";
						}
					}
					double packetLoss = 1 - ((double) rrts.size() / count);
					Config.pingInfo = String.valueOf(totalPingDelay/count);
					Config.testFlag = 11;
				} catch (Exception e) {
					Config.testFlag = 12;
					e.printStackTrace();
				}
			}		
		}.start();
	}
	
	public static void pingURLTest(final String target, final int count) {
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				long pingStartTime = 0;
				long pingEndTime = 0;
				ArrayList<Double> rrts = new ArrayList<Double>();
				String output = Config.contentDateFormat.format(new Date(System.currentTimeMillis())) 
						+ " " + target + " " + count + "\n";
				try {
					long totalPingDelay = 0;
					URL url = new URL("http://" + target);
					int timeOut = 3000;

					for (int i = 0; i < count; i++) {
						pingStartTime = System.currentTimeMillis();
						HttpURLConnection httpClient = (HttpURLConnection) url.openConnection();
						httpClient.setRequestProperty("Connection", "close");
						httpClient.setRequestMethod("HEAD");
						httpClient.setReadTimeout(timeOut);
						httpClient.setConnectTimeout(timeOut);
						httpClient.connect();
						pingEndTime = System.currentTimeMillis();
						httpClient.disconnect();
						totalPingDelay += pingEndTime - pingStartTime;
						output += i + ": " + totalPingDelay + "\n";
						rrts.add((double) pingEndTime - pingStartTime);
					}
					double packetLoss = 1 - ((double) rrts.size() / count);
					Config.pingInfo = String.valueOf(totalPingDelay/count);
					Config.testFlag = 11;
				} catch (Exception e) {
					Config.testFlag = 12;
					e.printStackTrace();
				}
			}			
		}.start();		
	}
	
	public static void dnsLookupTest(final String target, final int count) {
		new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				// nslookupÃüÁî tracertÃüÁî
				long t1, t2;
				long totalTime = 0;
				int successCnt = 0;
				String output = Config.contentDateFormat.format(new Date(System.currentTimeMillis())) 
						+ " " + target + " " + count + "\n";
				for (int i = 0; i < count; i++) {
					try {
						t1 = System.currentTimeMillis();
						InetAddress inet = InetAddress.getByName(target);
						t2 = System.currentTimeMillis();
						if (inet != null) {
							totalTime += (t2 - t1);
							successCnt++;
							output += i + ": " + (t2 - t1) + "\n";
						}
					} catch (UnknownHostException e) {
						Config.dnsLookupInfo = "ÓòÃû½âÎö´íÎó";
						Config.testFlag = 22;
						e.printStackTrace();
					}
				}
				Config.dnsLookupInfo = String.valueOf(totalTime / successCnt);
				Config.testFlag = 21;
			}
		}.start();
	}
	
	public static void httpTest(final String target) {
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();

				String urlString = "http://" + target;
				try {
					long t1=System.currentTimeMillis();
//					URL url = new URL(urlString);
//					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//					urlConnection.getConnectTimeout();
					
//					AndroidHttpClient androidHttpClient; //get post
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpget = new HttpGet(urlString); 
					HttpResponse response = httpClient.execute(httpget);
					
					long t2=System.currentTimeMillis();
//					Config.httpInfo = String.valueOf(t2-t1);
					Config.testFlag = 31;
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					Config.testFlag = 32;
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Config.testFlag = 32;
					e.printStackTrace();
				}				
			}
		}.start();
	}
}
