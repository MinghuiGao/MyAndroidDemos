package cn.gaomh.location.gps;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.gaomh.R;

/**
 * 功能描述 : 负责gps数据获取及开启状态监测
 * 
 * @类型名称 GpsHanlder
 * @版本 1.0
 * @创建者 gaominghui
 * @创建时间 2015年5月20日 上午11:33:52
 * @版权所有 ©2015 CTFO
 */
public class GpsHandlerWithBroadcast extends LinearLayout implements OnClickListener {

	private LocationManager mLocationMgr;
	
	private LocationReceiver mLocationReceiver;
	
	public static final int UPDATE_VIEW  =  0x1000;
	private Intent gpsService;
	
	public GpsHandlerWithBroadcast(Context context) {
		this(context, null);
	}

	public GpsHandlerWithBroadcast(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GpsHandlerWithBroadcast(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		((Button) findViewById(R.id.bt_open_gps)).setOnClickListener(this);
		detectGpsAndObtainLocation();
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mLocationReceiver = new LocationReceiver();
		IntentFilter counterActionFilter = new IntentFilter(LocationService.UPDATE_VIEW_ACTION);
		getContext().registerReceiver(mLocationReceiver, counterActionFilter);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(mLocationReceiver != null){
			getContext().unregisterReceiver(mLocationReceiver);
		}
	}

	/** 监测gps，如果开启则获取位置信息 **/
	public void detectGpsAndObtainLocation(){
		mLocationMgr = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
		if(!mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)){// 没有开启gps
			((Button) findViewById(R.id.bt_open_gps)).setVisibility(View.VISIBLE);
			((Button) findViewById(R.id.bt_open_gps)).setClickable(true);
			((TextView) findViewById(R.id.tv_gps_stauts)).setText("请开启gps功能！");
		}else{
			((Button) findViewById(R.id.bt_open_gps)).setVisibility(View.GONE);
			((TextView) findViewById(R.id.tv_gps_stauts)).setText("正在获取...");
			
			gpsService = new Intent(getContext(),LocationService.class);
			getContext().startService(gpsService);
		}
	}
	
	/** 停止服务 **/
	public void destory(){
		mLocationMgr = null;
		if(gpsService != null){
			getContext().stopService(gpsService);
			gpsService = null;
		}
	}
	
	@Override
	public void onClick(View v) {
		// open gpssetting。
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			getContext().startActivity(intent);
		} catch (ActivityNotFoundException ex) {
			// General settings activity
			intent.setAction(Settings.ACTION_SETTINGS);
			try {
				getContext().startActivity(intent);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 实时更新文本内容
	 * 
	 * @param location
	 */
	private void updateView(Location location) {
		if (location != null) {
			((TextView) findViewById(R.id.tv_gps_stauts)).setText("获取成功:"+"经度："+String.valueOf(location.getLongitude())+"纬度："+String.valueOf(location.getLatitude()));
			Toast.makeText(getContext(), "经度："+String.valueOf(location.getLongitude())+"纬度："+String.valueOf(location.getLatitude()), 0).show();
		} else {
			if(!mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)){// 没有开启gps
				((Button) findViewById(R.id.bt_open_gps)).setVisibility(View.VISIBLE);
				((Button) findViewById(R.id.bt_open_gps)).setClickable(true);
				((TextView) findViewById(R.id.tv_gps_stauts)).setText("请开启gps功能！");
			}else{
				((TextView) findViewById(R.id.tv_gps_stauts)).setText("请到企业门脸正前方空旷处。");
			}
		}
	}

	
	/** 位置获取后更新视图  **/
	class LocationReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if( intent != null){
				Bundle extras = intent.getExtras();
				Location location = extras.getParcelable("location");
				updateView(location);
			}
		}
	}
	
	
}
