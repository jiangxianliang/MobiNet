package thu.kejiafan.mobinet;

import java.util.ArrayList;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		initAll();
	}
	
	private void initAll() {
		// 设置屏幕常亮
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		// 调用百度统计
		StatService.setAppChannel(this, "Baidu Market", true);
		StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START, 1, false);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);		
		Config.resources = getResources();
        initWidth();
        initTextView();
        initViewPager();
        
        getTelephoneInfo();
	}
	
	private void initTextView() {
        Config.tvTabPhone = (TextView) findViewById(R.id.tv_tab_activity);
        Config.tvTabNetwork = (TextView) findViewById(R.id.tv_tab_groups);
        Config.tvTabGPS = (TextView) findViewById(R.id.tv_tab_friends);
        Config.tvTabAbout = (TextView) findViewById(R.id.tv_tab_chat);

        Config.tvTabPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Config.mPager.setCurrentItem(0);
			}
		});
        Config.tvTabNetwork.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Config.mPager.setCurrentItem(1);
			}
		});
        Config.tvTabGPS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Config.mPager.setCurrentItem(2);
			}
		});
        Config.tvTabAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Config.mPager.setCurrentItem(3);
			}
		});
    }

    private void initViewPager() {
    	Config.mPager = (ViewPager) findViewById(R.id.vPager);
    	Config.fragmentsList = new ArrayList<Fragment>();

		Fragment phoneFragment = new PhoneFragment();
		Fragment networkFragment = new NetworkFragment();
		Fragment gpsFragment = new GPSFragment();
		Fragment aboutFragment = new AboutFragment();

        Config.fragmentsList.add(phoneFragment);
        Config.fragmentsList.add(networkFragment);
        Config.fragmentsList.add(gpsFragment);
        Config.fragmentsList.add(aboutFragment);
        
        Config.mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), Config.fragmentsList));
        Config.mPager.setCurrentItem(0);
        Config.mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    private void initWidth() {
        Config.ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
        Config.bottomLineWidth = Config.ivBottomLine.getLayoutParams().width;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        Config.offset = (int) ((screenW / 4.0 - Config.bottomLineWidth) / 2);

        Config.position_one = (int) (screenW / 4.0);
        Config.position_two = Config.position_one * 2;
        Config.position_three = Config.position_one * 3;
    }
    
    private void getTelephoneInfo() {
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setIcon(R.drawable.ic_launcher);
			builder.setTitle("Exit");
			builder.setMessage("退出MobiNet?");
			builder.setPositiveButton("返回",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							return;
						}
					});
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							android.os.Process.killProcess(android.os.Process.myPid());
						}
					});
			builder.show();
		}

		return super.onKeyDown(keyCode, event);
	}
}
