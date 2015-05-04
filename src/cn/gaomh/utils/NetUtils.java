package cn.gaomh.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Entity;

import cn.gaomh.Constant;

public class NetUtils {

	/**
	 * 上传字节数组到服务器--如果图像太大，需要分段上传,要不要断点？
	 * 
	 * @param bytes
	 * @param url
	 * @return
	 * @author dell
	 */
	public static Object uploadBytes(byte[] bytes , String url){
		HttpClient client = getHttpClient();
		HttpPost post = new HttpPost(url);
		
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
	
	/**
	 * 返回一个httpclient，并进行初步设置。
	 * @return
	 */
	public static HttpClient getHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);
		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
		HttpClientParams.setRedirecting(httpParams, true);
		String userAgent = "Android/4.0";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}
}
