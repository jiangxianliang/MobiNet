package thu.kejiafan.mobinet;

import com.baidu.mobstat.StatService;

import thu.kejiafan.mobinet.R.id;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PhoneFragment extends Fragment {
	
	private MyPhoneStateListener myPhoneListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.phone, container, false);
		initWidget(view);
		
		myPhoneListener = new MyPhoneStateListener();
		Config.telManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		Config.telManager.listen(myPhoneListener, Config.phoneEvents);
		
		switch (Config.telManager.getNetworkType()) {
		case TelephonyManager.PHONE_TYPE_NONE:
			Config.tvPhoneType.setText("无信号");
			break;
		case TelephonyManager.PHONE_TYPE_GSM:
			Config.tvPhoneType.setText("GSM");
			break;
		case TelephonyManager.PHONE_TYPE_CDMA:
			Config.tvPhoneType.setText("CDMA");
			break;
		case TelephonyManager.PHONE_TYPE_SIP:
			Config.tvPhoneType.setText("SIP");
			break;
		default:
			Config.tvPhoneType.setText("");
			break;
		}
				
		return view;
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		Config.telManager.listen(myPhoneListener, Config.phoneEvents);
		StatService.onPause(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Config.telManager.listen(myPhoneListener, Config.phoneEvents);
		StatService.onResume(this);		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Config.telManager.listen(myPhoneListener, Config.phoneEvents);
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		Config.telManager.listen(myPhoneListener, Config.phoneEvents);
		super.onStop();
	}

	private void initWidget(View view) {
    	Config.tvPhoneModel = (TextView) view.findViewById(id.phoneModel);
    	Config.tvosVersion = (TextView) view.findViewById(id.osVersion);
    	Config.tvOperator = (TextView) view.findViewById(id.operator);
    	Config.tvPhoneType = (TextView) view.findViewById(id.phoneType);
    	Config.tvDataConnection = (TextView) view.findViewById(id.networkState);
    	Config.tvNetworkType = (TextView) view.findViewById(id.networkType);
    	Config.tvSignalStrength = (TextView) view.findViewById(id.signalStrength);
    	Config.tvSignalParameter = (TextView) view.findViewById(id.signalParameter);
    	Config.tvCurrentCell = (TextView) view.findViewById(id.currentCell);
    	Config.tvCellLocation = (TextView) view.findViewById(id.cellLocation);
    	Config.tvHandoffNumber = (TextView) view.findViewById(id.handoff);
		
		Config.tvPhoneModel.setText(Config.phoneModel);
		Config.tvosVersion.setText(Config.osVersion);
		Config.tvOperator.setText(Config.providerName);		
		Config.tvDataConnection.setText(Config.dataConnectionState);
		Config.tvNetworkType.setText(Config.subtypeName);
		Config.tvSignalStrength.setText(Config.signalStrengthString);
		Config.tvSignalParameter.setText(Config.SignalParameterString);
		Config.tvHandoffNumber.setText("切换次数:"
				+ String.valueOf(Config.handoffNumber) + " 断网次数:"
				+ String.valueOf(Config.disconnectNumber));
    }
}
