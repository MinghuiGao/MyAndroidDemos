package cn.gaomh.camera;

import cn.gaomh.R;
import cn.gaomh.camera.IMultipleCamera.ButtonCallback;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/** 
 * 功能描述 : 自定义的multiplecamera类。
 * @类型名称 MultipleCameraActivity
 * @版本 1.0
 * @创建者 gaominghui
 * @创建时间 2015年5月19日 下午9:08:12
 * @版权所有 ©2015 CTFO
 */
public class MultipleCameraActivity extends Activity {

	private  MultipleCamera mMultipleCamera;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_main);
		mMultipleCamera = (MultipleCamera) findViewById(R.id.mc_camera);
		mMultipleCamera.setButtonCallback(new ButtonCallback() {
			@Override
			public void onShootClicked() {
				Toast.makeText(getApplicationContext(), "点击拍照", 0).show();
			}
			@Override
			public void onBackClicked() {
				mMultipleCamera.destory();
				MultipleCameraActivity.this.finish();
			}
			@Override
			public void onSuccess() {
				Toast.makeText(getApplicationContext(), "进行下一项", 0).show();
			}
		});
	}
}
