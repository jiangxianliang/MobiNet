package thu.kejiafan.mobinet;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
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
	    new Handler().postDelayed(new Runnable() {  
            public void run() {  
                Intent mainIntent = new Intent(SplashActivity.this,  
                        MainActivity.class);  
                SplashActivity.this.startActivity(mainIntent);  
                SplashActivity.this.finish();  
            }
        }, 3000); 
	}
}
