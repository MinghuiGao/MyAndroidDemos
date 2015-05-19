package cn.gaomh.camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import cn.gaomh.R;
import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;

/** 
 * 功能描述 : 调用
 * @类型名称 MultipleCamera
 * @版本 1.0
 * @创建者 gaominghui
 * @创建时间 2015年5月19日 下午4:41:46
 * @版权所有 ©2015 CTFO
 */
public class MultipleCamera extends FrameLayout implements IMultipleCamera, OnClickListener {

	private SurfaceView mSurfaceView;
	private Context mContext;
	private Camera mCamera;
	private Camera.Parameters parameters = null;
	private int[] mClickableViews = new int[]{R.id.tv_camera_back,R.id.iv_camera_shoot,R.id.tv_camera_falsh};
	Bundle bundle = null;
	private ButtonCallback mButtonCallback = new ButtonCallback() {
		@Override
		public void onShootClicked() {
		}
		@Override
		public void onBackClicked() {
		}
		@Override
		public void onSuccess() {
		}
	};
	
	public MultipleCamera(Context context){
		this(context,null);
	}
	
	public MultipleCamera(Context context,AttributeSet attrs){
		this(context,attrs,0);
	}
	
	public MultipleCamera(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext =  context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initViews();
	}
	
	private void initViews() {
		// set onclicklistener
		for(int clickView : mClickableViews){
			View view = this.findViewById(clickView);
			if(view != null){
				view.setOnClickListener(this);
			}
		}
		// init surfaceview
		mSurfaceView = (SurfaceView) this.findViewById(R.id.sv_camera_preview);
		if(mSurfaceView != null){
			mSurfaceView.setFocusable(true);
			SurfaceHolder holder = mSurfaceView.getHolder();
			holder.setKeepScreenOn(true);
			holder.addCallback(new SurfaceCallback());
		}
	}

	/** 设置回调 **/
	public void setButtonCallback(ButtonCallback buttonCallback){
		this.mButtonCallback = buttonCallback;
	}
	
	@Override
	public void startRecording() {
		// TODO Auto-generated method stub
	}

	@Override
	public File startRecording(String folder, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void pauseRecording() {
		// TODO Auto-generated method stub

	}

	@Override
	public String stopRecording() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replay() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveToLocal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void upload() {
		// TODO Auto-generated method stub

	}

	@Override
	public void takePhoto() {
		if(mCamera != null){
			mButtonCallback.onShootClicked();
			mCamera.takePicture(null, null, new UPictureCallback());
		}
	}

	@Override
	public File takePhoto(String foler, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void turnOnFlash() {
		parameters = mCamera.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
		parameters.setJpegQuality(50);
		parameters.setFlashMode(Parameters.FLASH_MODE_ON);
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
		mCamera.setParameters(parameters);
		mCamera.startPreview();
		mCamera.cancelAutoFocus();// 如果要实现连续的自动对焦，这一句必须加上
	}

	@Override
	public void turnOffFlash() {
		parameters = mCamera.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
		parameters.setJpegQuality(50);
		parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
		mCamera.setParameters(parameters);
		mCamera.startPreview();
		mCamera.cancelAutoFocus();// 如果要实现连续的自动对焦，这一句必须加上
	}

	@Override
	public void flash() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destory() {
		if(mCamera != null){
			mCamera.release();
		}
		mCamera = null;
		mClickableViews = null;
		mContext = null;
		mSurfaceView = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_camera_back:
			destory();
			this.mButtonCallback.onBackClicked();
			break;
		case R.id.iv_camera_shoot:
			takePhoto();
			this.mButtonCallback.onShootClicked();
			break;
		case R.id.tv_camera_falsh:
			String status = ((TextView)v).getText().toString();
			if("开闪".equals(status)){
				turnOnFlash();
				((TextView)v).setText("关闪");
			}else if("关闪".equals(status)){
				turnOffFlash();
				((TextView)v).setText("开闪");
			}
			break;
		default:
			break;
		}
	}
	
	/**  **/
	private final class SurfaceCallback implements Callback {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// 开始拍照
			if(mCamera == null){
				mCamera = Camera.open();
				try{
					mCamera.setPreviewDisplay(holder);
					initCamera();
					mCamera.startPreview();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// auto focus
			mCamera.autoFocus(new AutoFocusCallback() {
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
			if(mCamera != null){
				mCamera.stopPreview();  
				mCamera.release();  
				mCamera=null;  
			}
		}
	}
	
	// 相机参数的初始化设置
	private void initCamera() {
		parameters = mCamera.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
		// 设置图片的质量，从1-100.
		parameters.setJpegQuality(50);
//		parameters.setPictureSize(mSurfaceView.getWidth(),mSurfaceView.getHeight()); // 部分定制手机，无法正常识别该方法。
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
//		setDispaly(parameters, mCamera);
		mCamera.setParameters(parameters);
		mCamera.startPreview();
		mCamera.cancelAutoFocus();// 如果要实现连续的自动对焦，这一句必须加上
	}

	// 控制图像的正确显示方向 华为设置后角度错误
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

	/** 拍照的回调，进行照片的压缩、存储 **/
	private final class UPictureCallback implements PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			try {
				bundle = new Bundle();
				bundle.putByteArray("bytes", data); // 将图片字节数据保存在bundle当中，实现数据交换
				saveToSDCard(data); // 保存图片到sd卡中
				Toast.makeText(mContext, "success!",Toast.LENGTH_SHORT).show();
				mButtonCallback.onSuccess();
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
	

}
