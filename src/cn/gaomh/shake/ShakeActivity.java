package cn.gaomh.shake;

import cn.gaomh.Constant;
import cn.gaomh.R;
import cn.gaomh.shake.ShakeListener.OnShakeListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ShakeActivity extends Activity {

	ShakeListener mShakeListener = null;
	private TextView tv_shake_;
	private ImageView iv_shake_;
	private SensorManager ss;
	private Vibrator vibrator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake);
		Log.i(Constant.tag, "begin to shake");

		tv_shake_ = (TextView) findViewById(R.id.tv_shake_);
		iv_shake_ = (ImageView) findViewById(R.id.iv_shake_);

		ss = (SensorManager) getSystemService(SENSOR_SERVICE);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

		mShakeListener = new ShakeListener(ShakeActivity.this);

		mShakeListener.setOnShakeListener(new shakeLitener());
	}

	private class shakeLitener implements OnShakeListener {
		@Override
		public void onShake() {
			vibrator.vibrate(1000);
			tv_shake_.setText("摇一摇成功啦！");
			iv_shake_.setImageResource(R.drawable.ic_launcher);
			mShakeListener.stop();
		}
	}

	/*
	 * ShakeListener mShakeListener = null; private TextView tv_shake_; private
	 * ImageView iv;
	 * 
	 * @Override protected void onCreate(Bundle savedInstanceState) { // TODO
	 * Auto-generated method stub super.onCreate(savedInstanceState);
	 * this.setContentView(R.layout.main);
	 * 
	 * tv=(TextView)this.findViewById(R.id.textView1);
	 * iv=(ImageView)this.findViewById(R.id.imageView1);
	 * 
	 * mShakeListener = new ShakeListener(this);
	 * mShakeListener.setOnShakeListener(new shakeLitener()); } private class
	 * shakeLitener implements OnShakeListener{
	 * 
	 * @Override public void onShake() { // TODO Auto-generated method stub
	 * tv.setText("摇一摇成功啦！"); iv.setImageResource(R.drawable.attitude_laugh);
	 * mShakeListener.stop(); }
	 * 
	 * } }
	 */

	public static void loadShake(Context context, Object... objects) {
		Intent intent = new Intent(context, ShakeActivity.class);
		context.startActivity(intent);
	}

}
