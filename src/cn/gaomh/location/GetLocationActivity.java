package cn.gaomh.location;

import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;

public class GetLocationActivity extends Activity {

	LocationManagerProxy locationManager = null;
	
	Double geolat = 0.0;
	Double geoLng = 0.0;
	String desc = "";//location info.
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		LinearLayout root = new LinearLayout(this);
		TextView tv = new TextView(this);
		root.addView(tv);
		//获取位置信息
		String location = getLocation();
		tv.setText(location);
	}

	/**
	 * get the location now.
	 */
	private String getLocation() {
		//init locationManager
		locationManager = LocationManagerProxy.getInstance(GetLocationActivity.this);
		List<String> providers = locationManager.getProviders(true);
		for(String provider : providers ){
			//get the gps or Amap(网络定位)AMapNet
			if(LocationManagerProxy.GPS_PROVIDER.equals(provider) || LocationManagerProxy.NETWORK_PROVIDER.equals(provider)){
				//注册一个位置监听其
				locationManager.requestLocationUpdates(provider, 0, 0, new AMapLocationListener(){

					@Override
					public void onLocationChanged(Location location) {
						if(location != null){
							Log.i("getLocationActivity", "location changed....");
							double latitude = location.getLatitude();
							double longitude = location.getLongitude();
							double altitude = location.getAltitude();
							long elapsedRealtimeNanos = location.getElapsedRealtimeNanos();
							float bearing = location.getBearing();
							System.out.println(latitude+ " -----"+longitude+ " -----"+altitude+ " -----"+elapsedRealtimeNanos+ " -----"+bearing);
						}
					}

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {}

					@Override
					public void onProviderEnabled(String provider) {}

					@Override
					public void onProviderDisabled(String provider) {}

					@Override
					public void onLocationChanged(AMapLocation amLocation) {
						
						if(amLocation != null){
							geolat = amLocation.getLatitude();
							geoLng = amLocation.getLongitude();
							Bundle extras = amLocation.getExtras();
							Set<String> keySet = extras.keySet();
							for(String key : keySet){
								System.out.println("key from amLocation's extras: "+key);
							}
							
						}
					}
				});
			}
		}
		
		return null;
	}
	
	
}
