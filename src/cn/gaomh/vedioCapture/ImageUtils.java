package cn.gaomh.vedioCapture;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {
	private Context context;

	public ImageUtils(Context context) {
		this.context = context;
	}

	/**
	 * 将字节流转换为byte数组.
	 * @param is
	 * @return
	 */
	public static byte[] streamToBytes(InputStream is) {
		ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int len;
		try {
			while ((len = is.read(buffer)) >= 0) {
				os.write(buffer, 0, len);
			}
		} catch (java.io.IOException e) {System.out.println("streamToBytes io exception...");}
		return os.toByteArray();
	}

	/**
	 * 一句话的事，写不写都行。
	 * 
	 * @param bytes
	 * @return
	 */
	public Bitmap bytes2Bitmap(byte[] bytes) {
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * 将bitmap转化为byte【】。其中编码格式和质量可以继续抽。
	 * 
	 * @param bitmap
	 * @return
	 */
	public byte[] bitmap2Bytes(Bitmap bitmap) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 将Bitmap压缩成PNG编码，质量为100%存储
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);// 除了PNG还有很多常见格式，如jpeg等。
		return os.toByteArray();

	}

	/**
	 * 将资源中的图像转换为btimap后转为byte[]
	 * 
	 * @param bitmap
	 * @param id
	 *            resource id.
	 * @return
	 */
	public byte[] resource2Bitmap2Bytes(int resId) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				resId);
		if (bitmap == null) {
			System.out.println("bitmap2Bytes --> bitmap is null...");
			return null;
		}
		return bitmap2Bytes(bitmap);
	}
}
