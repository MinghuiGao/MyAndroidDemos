package cn.gaomh.vedioCapture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import cn.gaomh.Constant;
import cn.gaomh.R;
import cn.gaomh.utils.NetUtils;

public class VedioCaptureActivity extends Activity implements OnClickListener {

	private EditText et_vediocapture_savepath;
	private Button bt_vediocapture_;
	Camera mCamera;
	FrameLayout preview;
	CameraPreview mPreview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vediocapture);
		bt_vediocapture_ = (Button) findViewById(R.id.bt_vediocapture_);
		if (checkCameraHardware(VedioCaptureActivity.this)) {
	        // Create an instance of Camera
			mCamera = getFrontCamera(this);//试试前端摄像头先。。。。
//			mCamera = getBackCamera();//前面没有就用后面的
	        // Create our Preview view and set it as the content of our activity.
	        mPreview = new CameraPreview(this, mCamera);
	        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
	        preview.addView(mPreview);
			
	        bt_vediocapture_.setOnClickListener(this);
		} else {
			Toast.makeText(this, "no camera...", 0).show();
		}
	}

	/**
	 * Check if this device has a camera
	 * 
	 * @author sdk
	 * */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	
	/**
	 * return the front face camera.
	 * @return
	 * @author dell
	 */
	public static Camera getFrontCamera(Activity activity){
		int cameraCount = 0;
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras(); // get cameras number
		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				// 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
				try {
					cam = Camera.open(camIdx);
					int rotation = activity.getWindowManager().getDefaultDisplay()
				             .getRotation();
				     int degrees = 0;
				     switch (rotation) {
				         case Surface.ROTATION_0: degrees = 0; break;
				         case Surface.ROTATION_90: degrees = 90; break;
				         case Surface.ROTATION_180: degrees = 180; break;
				         case Surface.ROTATION_270: degrees = 270; break;
				     }
				     int result;
				     if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				         result = (cameraInfo.orientation + degrees) % 360;
				         result = (360 - result) % 360;  // compensate the mirror
				     } else {  // back-facing
				         result = (cameraInfo.orientation - degrees + 360) % 360;
				     }
				     cam.setDisplayOrientation(result);
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
		}
		return cam;
	}
	/**
	 *  return the back face camera.
	 * @return
	 * @author dell
	 */
	public static Camera getBackCamera(){
		return getCameraInstance();
	}
	
	/**
	 * A safe way to get an instance of the Camera object.
	 * 
	 * @author sdk
	 */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			System.out.println("exception here... no camera.");
		}
		return c; // returns null if camera is unavailable
	}

	/**
	 * set camera orientation...
	 * @param activity
	 * @param cameraId
	 * @param camera
	 */
	public static void setCameraDisplayOrientation(Activity activity,
	         int cameraId, android.hardware.Camera camera) {
	     android.hardware.Camera.CameraInfo info =
	             new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay()
	             .getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }
	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	 }
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.bt_vediocapture_:
			//自测所有参数。
//			mCamera.takePicture(
//						//shuttercallback to play a sound or vibrate.
//						new MyShutterCallBack(VedioCaptureActivity.this)
//					, new PictureCallback() {//raw
//						
//						@Override
//						public void onPictureTaken(byte[] data, Camera camera) {
//							Log.i(Constant.tag, "raw === data.length : " + data.length);
//						}
//						
//					}, new PictureCallback() {//postview
//						
//						@Override
//						public void onPictureTaken(byte[] data, Camera camera) {
//							Log.i(Constant.tag, "postview === data.length : " + data.length);
//							
//						}
//					}, new PictureCallback() {//jpeg
//						
//						@Override
//						public void onPictureTaken(byte[] data, Camera camera) {
//							Log.i(Constant.tag, "jpeg === data.length : " + data.length);
//							
//						}
//					});
			
			mCamera.takePicture(null, null, new PictureCallback() {
				@Override
				public void onPictureTaken(byte[] data, Camera camera) {
					//send the data to server here...... 
					//bytes -> String -> S ->bytes -> image...
					camera.stopPreview();
					System.out.println("data .l enght: "+data.length);
					
					//bitmap -> add view--> confirm save ---> continue
					
					Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
					ImageView iv = new ImageView(VedioCaptureActivity.this);
					iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
					iv.setImageBitmap(bmp);
					preview.addView(iv);
					
					
					int res = (Integer) NetUtils.uploadBytes(data,"http://192.168.3.102:8080/web_fileupload/StorePicServlet");
					System.out.println("result：" + res);
					if(res == Constant.SUCCESS){
						
						Toast.makeText(VedioCaptureActivity.this, "上传成功！", 0).show();
						//显示刚拍的照片，或者允许用户对照片做处理。
						//可以生成一个新的view，来讲停滞的画面覆盖，这样就相当于预览了。
						
						try {Thread.sleep(2000);} catch (InterruptedException e){e.printStackTrace();}camera.startPreview();
						
					}else{
						//上传失败.
						
					}
					
				}
			});
			break;
			default:
				break;
		}
	}
	
	/**
	 * 上传字节数组到服务器--如果图像太大，需要分段上传,要不要断点？
	 * 
	 * @param bytes
	 * @param url
	 * @return
	 * @author dell
	 */
	public Object uploadBytes(byte[] bytes , String url){
		HttpClient client = NetUtils.getHttpClient();
		HttpPost post = new HttpPost(url);
		
		Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		
		
		
//		BasicHttpEntity entity = new BasicHttpEntity();
//		//bytes 2 inputStream
//		InputStream is = new ByteArrayInputStream(bytes); 
//		try {
//			System.out.println("is . available:" +is.available());
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		entity.setContent(is);
//		entity.setContentType("text/html");
//		post.setEntity(entity);
		post.setEntity(new ByteArrayEntity(bytes));
		try {
			HttpResponse response = client.execute(post);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == 200){
				return Constant.SUCCESS;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Constant.FAILED;
	}
	
	
	class MyShutterCallBack implements ShutterCallback{
		private Context context;
		private Vibrator v ;
		public MyShutterCallBack(Context context){
			this.context = context;
			v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		}
		@Override
		public void onShutter() {
			v.vibrate(1000);
		}
	}
}
