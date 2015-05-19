package cn.gaomh.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import cn.gaomh.R;

/**
 * 功能描述 自定义相机，对预览surfaceview进行自定义。
 * 
 * @类型名称 MyCameraActivity
 * @版本 1.0
 * @创建者 gaominghui
 * @创建时间 2015年5月19日 上午11:46:51
 * @版权所有 ©2015 CTFO
 */
public class MyCameraActivity extends Activity {
	private View layout;
	private Camera camera;
	private Camera.Parameters parameters = null;

	Bundle bundle = null; // 声明一个Bundle对象，用来存储数据

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 显示界面
		setContentView(R.layout.camera_preview);

		layout = this.findViewById(R.id.buttonLayout);

		SurfaceView surfaceView = (SurfaceView) this
				.findViewById(R.id.surfaceView);
		surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
		surfaceView.setFocusable(true);
		surfaceView.getHolder().addCallback(new SurfaceCallback());// 为SurfaceView的句柄添加一个回调函数
	}

	// 相机参数的初始化设置
	private void initCamera() {
		parameters = camera.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
		// parameters.setPictureSize(surfaceView.getWidth(),
		// surfaceView.getHeight()); // 部分定制手机，无法正常识别该方法。
		parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
		setDispaly(parameters, camera);
		camera.setParameters(parameters);
		camera.startPreview();
		camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
	}

	// 控制图像的正确显示方向
	private void setDispaly(Camera.Parameters parameters, Camera camera) {
		if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
			setDisplayOrientation(camera, 90);
		} else {
			parameters.setRotation(90);
		}
	}
	// 实现的图像的正确显示
	private void setDisplayOrientation(Camera camera, int i) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod("setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null) {
				downPolymorphic.invoke(camera, new Object[] { i });
			}
		} catch (Exception e) {
			Log.e("Came_e", "图像出错");
		}
	}

	/**
	 * 按钮被点击触发的事件
	 * 
	 * @param v
	 */
	public void btnOnclick(View v) {
		if (camera != null) {
			switch (v.getId()) {
			case R.id.takepicture:
				// 拍照
				camera.takePicture(null, null, new MyPictureCallback());
				break;
			}
		}
	}

	/**
	 * 图片被点击触发的时间
	 * 
	 * @param v
	 */
	public void imageClick(View v) {
		if (v.getId() == R.id.scalePic) {
			if (bundle == null) {
				Toast.makeText(getApplicationContext(), "take photo.",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MyCameraActivity.this, "bundle is not null.",
						Toast.LENGTH_SHORT).show();
				// Intent intent = new Intent(this, ShowPicActivity.class);
				// intent.putExtras(bundle);
				// startActivity(intent);
			}
		}
	}

	/** 拍照的回调，进行照片的压缩、存储 **/
	private final class MyPictureCallback implements PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				bundle = new Bundle();
				bundle.putByteArray("bytes", data); // 将图片字节数据保存在bundle当中，实现数据交换
				saveToSDCard(data); // 保存图片到sd卡中
				Toast.makeText(getApplicationContext(), "success!",Toast.LENGTH_SHORT).show();
				camera.startPreview(); // 拍完照后，重新开始预览

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将拍下来的照片存放在SD卡中 
	 * 
	 * @param data
	 * @throws IOException
	 */
	public static void saveToSDCard(byte[] data) throws IOException {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
		String filename = format.format(date) + ".jpg";
		File fileFolder = new File(Environment.getExternalStorageDirectory()+ "/finger/");
		if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
			fileFolder.mkdir();
		}
		File jpgFile = new File(fileFolder, filename);
		FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
		outputStream.write(data); // 写入sd卡中
		outputStream.close(); // 关闭输出流
	}

	/**  **/
	private final class SurfaceCallback implements Callback {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// 开始拍照
			if(camera == null){
				camera = Camera.open();
				try{
					camera.setPreviewDisplay(holder);
					initCamera();
					camera.startPreview();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// auto focus
			camera.autoFocus(new AutoFocusCallback() {
				@Override
				public void onAutoFocus(boolean success, Camera camera) {
					if(success){
						initCamera();
					}
				}
			});
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if(camera != null){
				camera.stopPreview();  
	            camera.release();  
	            camera=null;  
			}
		}

	}
	
	/**
	 * 点击手机屏幕是，显示两个按钮
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			layout.setVisibility(ViewGroup.VISIBLE); // 设置视图可见
			break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_CAMERA: // 按下拍照按钮
			if (camera != null && event.getRepeatCount() == 0) {
				// 拍照
				// 注：调用takePicture()方法进行拍照是传入了一个PictureCallback对象——当程序获取了拍照所得的图片数据之后
				// ，PictureCallback对象将会被回调，该对象可以负责对相片进行保存或传入网络
				camera.takePicture(null, null, new MyPictureCallback());
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	// 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
	public static int getPreviewDegree(Activity activity) {
		// 获得手机的方向
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degree = 0;
		// 根据手机的方向计算相机预览画面应该选择的角度
		switch (rotation) {
		case Surface.ROTATION_0:
			degree = 90;
			break;
		case Surface.ROTATION_90:
			degree = 0;
			break;
		case Surface.ROTATION_180:
			degree = 270;
			break;
		case Surface.ROTATION_270:
			degree = 180;
			break;
		}
		return degree;
	}
}
