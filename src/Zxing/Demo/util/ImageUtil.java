package Zxing.Demo.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ImageUtil {
	private static final String SDCARD_CACHE_IMG_PATH = Environment
			.getExternalStorageDirectory().getPath() + "/daoliota/images/";

	/**
	 * ????????SD??
	 * 
	 * @param imagePath
	 * @param buffer
	 * @throws IOException
	 */
	public static void saveImage(String imagePath, byte[] buffer)
			throws IOException {
		File f = new File(imagePath);
		if (f.exists()) {
			return;
		} else {
			File parentFile = f.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(imagePath);
			fos.write(buffer);
			fos.flush();
			fos.close();
		}
	}

	/**
	 * ??SD????????
	 * 
	 * @param imagePath
	 * @return
	 */
	public static Bitmap getImageFromLocal(String imagePath) {
		File file = new File(imagePath);
		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
			file.setLastModified(System.currentTimeMillis());
			return bitmap;
		}
		return null;
	}

	/**
	 * Bitmap?????Byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, bas);
		return bas.toByteArray();
	}

	/**
	 * ?????????????????
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Bitmap loadImage(final String imagePath, final String imgUrl,
			final ImageCallback callback) {
		Bitmap bitmap = getImageFromLocal(imagePath);
		if (bitmap != null) {
			return bitmap;
		} else {// ?????????
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (msg.obj != null) {
						Bitmap bitmap = (Bitmap) msg.obj;
						callback.loadImage(bitmap, imagePath);
					}
				}
			};

			Runnable runnable = new Runnable() {
				public void run() {
					try {
						URL url = new URL(imgUrl);
						Log.e("ImageUtil", imgUrl);
						URLConnection conn = url.openConnection();
						conn.setConnectTimeout(5000);
						conn.setReadTimeout(5000);
						conn.connect();
						BufferedInputStream bis = new BufferedInputStream(
								conn.getInputStream(), 8192);
						Bitmap bitmap = BitmapFactory.decodeStream(bis);
						// ?????????sd??
						saveImage(imagePath, bitmap2Bytes(bitmap));
						Message msg = handler.obtainMessage();
						msg.obj = bitmap;
						handler.sendMessage(msg);
					} catch (IOException e) {
						Log.e(ImageUtil.class.getName(), "����ͼƬʧ��");
					}
				}
			};
			ThreadPoolManager.getInstance().addTask(runnable);
		}
		return null;
	}

	// ???????��sd????��??
	public static String getCacheImgPath() {
		return SDCARD_CACHE_IMG_PATH;
	}

	public static String md5(String paramString) {
		String returnStr;
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			returnStr = byteToHexString(localMessageDigest.digest());
			return returnStr;
		} catch (Exception e) {
			return paramString;
		}
	}

	/**
	 * ?????byte?????????16???????
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/**
	 * 
	 * @author Mathew
	 * 
	 */
	public interface ImageCallback {
		public void loadImage(Bitmap bitmap, String imagePath);
	}

	/**
	 * ??????????????????????
	 * 
	 * @param imagePath
	 * @param imgUrl
	 * @param callback
	 * @return
	 */
	public static Bitmap loadImageFromNet(final String imagePath,
			final String imgUrl, final ImageCallback callback) {
		// Bitmap bitmap = getImageFromLocal(imagePath);
		// if(bitmap != null){
		// return bitmap;
		// }else{//?????????
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.obj != null) {
					Bitmap bitmap = (Bitmap) msg.obj;
					callback.loadImage(bitmap, imagePath);
				}
			}
		};

		Runnable runnable = new Runnable() {
			public void run() {
				try {
					URL url = new URL(imgUrl);
					Log.e("ImageUtil", imgUrl);
					URLConnection conn = url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);
					conn.connect();
					BufferedInputStream bis = new BufferedInputStream(
							conn.getInputStream(), 8192);
					Bitmap bitmap = BitmapFactory.decodeStream(bis);
					// ?????????sd??
					// saveImage(imagePath,bitmap2Bytes(bitmap));
					Message msg = handler.obtainMessage();
					msg.obj = bitmap;
					handler.sendMessage(msg);
				} catch (IOException e) {
					Log.e(ImageUtil.class.getName(), "????????????��??????");
				}
			}
		};
		ThreadPoolManager.getInstance().addTask(runnable);
		// }
		return null;
	}

	public static void clearcache(Context context) {
		System.out.println("sdcardpath:" + SDCARD_CACHE_IMG_PATH);
		File file = new File(SDCARD_CACHE_IMG_PATH);
		clear(file);

	}

	private static void clear(File file) {
		System.out.println(file.listFiles().length + "����");
		if (file != null && file.isDirectory()) {
			for (File child : file.listFiles()) {
				if (child.isDirectory()) {
					clear(child);
				} else {
					child.delete();
				}
			}

		}
	}

	// public static void clearcache(Context context){
	// if (context != null) {
	// //File cacheDir = context.getCacheDir();
	// File cacheDir = new File(ImageUtil.SDCARD_CACHE_IMG_PATH);
	// clearCache(cacheDir,context);
	// }
	// }
	//
	// public static void clearCache(File file,Context context) {
	// //File cacheDir = context.getCacheDir();
	// File cacheDir = new File(ImageUtil.SDCARD_CACHE_IMG_PATH);
	// String root = cacheDir.getAbsolutePath();
	// String[] list = cacheDir.list();
	// processFiles(list);
	// }
	public static void processFiles(String[] files) {
		File tmp = null;
		if (files != null && files.length > 0) {
			for (String file : files) {
				tmp = new File(file);
				if (tmp.isFile()) {
					tmp.delete();
				} else if (tmp.isDirectory()) {
					processFiles(tmp.list());
				}
			}
		}
	}

}
