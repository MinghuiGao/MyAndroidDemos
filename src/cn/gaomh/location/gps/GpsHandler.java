package cn.gaomh.location.gps;

import java.io.Serializable;
import java.util.Iterator;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
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
public class GpsHandler extends LinearLayout implements OnClickListener {

	private LocationManager mLocationMgr;
	private String TAG = "ctfo";

	public static final int UPDATE_VIEW  =  0x1000;
	private final Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_VIEW:
				updateView((Location)msg.obj);
				break;
			default:
				break;
			}
		};
	};
	
	public GpsHandler(Context context) {
		this(context, null);
	}

	public GpsHandler(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GpsHandler(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		((Button) findViewById(R.id.bt_open_gps)).setOnClickListener(this);
		detectGpsAndObtainLocation();
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
			
			mLocationMgr = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
			// 为获取地理位置信息时设置查询条件
			String bestProvider = mLocationMgr.getBestProvider(getCriteria(), true);
			// 获取位置信息
			// 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
			Location location = mLocationMgr.getLastKnownLocation(bestProvider);
			updateView(location);
			// 监听状态
			mLocationMgr.addGpsStatusListener(listener);
			// 绑定监听，有4个参数
			// 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
			// 参数2，位置信息更新周期，单位毫秒
			// 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
			// 参数4，监听
			// 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
			
			// 1秒更新一次，或最小位移变化超过1米更新一次；
			// 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
//			mLocationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,locationListener);
			new Thread(new Runnable(){
				@Override
				public void run() {
					Looper.prepare();
					mLocationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1,locationListener);
					Looper.loop();
				}
			}).start();
		}
	}
	
	/** 服务绑定的连接 **/
	class GpsServiceConnection implements ServiceConnection{
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			
		}
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
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

	/** 位置信息服务的回调 **/
	public class LocationCallback implements LocationService.Callback,Serializable{
		@Override
		public void updateView(Location location) {
			updateView(location);
		}
	}
	
	/**
	 * 返回查询条件
	 * 
	 * @return
	 */
	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		// 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置是否要求速度
		criteria.setSpeedRequired(false);
		// 设置是否允许运营商收费
		criteria.setCostAllowed(false);
		// 设置是否需要方位信息
		criteria.setBearingRequired(false);
		// 设置是否需要海拔信息
		criteria.setAltitudeRequired(false);
		// 设置对电源的需求
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}
	
	// 状态监听
	GpsStatus.Listener listener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			switch (event) {
			// 第一次定位
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.i(TAG, "第一次定位");
				break;
			// 卫星状态改变
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.i(TAG, "卫星状态改变");
				// 获取当前状态
				GpsStatus gpsStatus = mLocationMgr.getGpsStatus(null);
				// 获取卫星颗数的默认最大值
				int maxSatellites = gpsStatus.getMaxSatellites();
				// 创建一个迭代器保存所有卫星
				Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
				int count = 0;
				while (iters.hasNext() && count <= maxSatellites) {
					GpsSatellite s = iters.next();
					count++;
				}
				System.out.println("搜索到：" + count + "颗卫星");
				break;
			// 定位启动
			case GpsStatus.GPS_EVENT_STARTED:
				Log.i(TAG, "定位启动");
				break;
			// 定位结束
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.i(TAG, "定位结束");
				break;
			}
		};
	};
	
	// 位置监听
	private LocationListener locationListener = new LocationListener() {

		/**
		 * 位置信息变化时触发
		 */
		public void onLocationChanged(Location location) {
			Message msg = Message.obtain();
			msg.what = UPDATE_VIEW;
			msg.obj = location;
			handler.sendMessage(msg);
			Log.i(TAG, "时间：" + location.getTime());
			Log.i(TAG, "经度：" + location.getLongitude());
			Log.i(TAG, "纬度：" + location.getLatitude());
			Log.i(TAG, "海拔：" + location.getAltitude());
		}

		/**
		 * GPS状态变化时触发
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			// GPS状态为可见时
			case LocationProvider.AVAILABLE:
				Log.i(TAG, "当前GPS状态为可见状态");
				break;
			// GPS状态为服务区外时
			case LocationProvider.OUT_OF_SERVICE:
				Log.i(TAG, "当前GPS状态为服务区外状态");
				break;
			// GPS状态为暂停服务时
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				Log.i(TAG, "当前GPS状态为暂停服务状态");
				break;
			}
		}

		/**
		 * GPS开启时触发
		 */
		public void onProviderEnabled(String provider) {
			Location location = mLocationMgr.getLastKnownLocation(provider);
			updateView(location);
		}

		/**
		 * GPS禁用时触发
		 */
		public void onProviderDisabled(String provider) {
			// TODO 更新视图
			updateView(null);
		}
		
	};
	
	
	
}
