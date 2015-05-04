package cn.gaomh.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BasicNetUtils {

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasNet(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo == null || !activeNetworkInfo.isAvailable()) {
			return false;
		}
		return true;
	}

	/**
	 * 字节流转化为字符串
	 * 
	 * @param is
	 * @return
	 */
	private static String stream2Striing(InputStream is) {
		int b;
		StringBuffer sb = new StringBuffer();
		try {
			while ((b = is.read()) != -1) {
				sb.append((char) b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	

}

class MyBasicHttpEntity extends AbstractHttpEntity{
	 	private InputStream content;
	    private boolean contentObtained;
	    private long length;

	    /**
	     * Creates a new basic entity.
	     * The content is initially missing, the content length
	     * is set to a negative number.
	     */
	    public MyBasicHttpEntity() {
	        super();
	        this.length = -1;
	    }

	    // non-javadoc, see interface HttpEntity
	    public long getContentLength() {
	        return this.length;
	    }

	    /**
	     * Obtains the content, once only.
	     *
	     * @return  the content, if this is the first call to this method
	     *          since {@link #setContent setContent} has been called
	     *
	     * @throws IllegalStateException
	     *          if the content has been obtained before, or
	     *          has not yet been provided
	     */
	    public InputStream getContent()
	        throws IllegalStateException {
	        if (this.content == null) {
	            throw new IllegalStateException("Content has not been provided");
	        }
	        if (this.contentObtained) {
	            throw new IllegalStateException("Content has been consumed");
	        }
	        this.contentObtained = true;
	        return this.content;

	    } // getContent

	    /**
	     * Tells that this entity is not repeatable.
	     *
	     * @return <code>false</code>
	     */
	    public boolean isRepeatable() {
	        return false;
	    }

	    /**
	     * Specifies the length of the content.
	     *
	     * @param len       the number of bytes in the content, or
	     *                  a negative number to indicate an unknown length
	     */
	    public void setContentLength(long len) {
	        this.length = len;
	    }

	    /**
	     * Specifies the content.
	     *
	     * @param instream          the stream to return with the next call to
	     *                          {@link #getContent getContent}
	     */
	    public void setContent(final InputStream instream) {
	        this.content = instream;
	        this.contentObtained = false; 
	    }

	    // non-javadoc, see interface HttpEntity
	    public void writeTo(final OutputStream outstream) throws IOException {
	        if (outstream == null) {
	            throw new IllegalArgumentException("Output stream may not be null");
	        }
	        InputStream instream = getContent();
	        int l;
	        byte[] tmp = new byte[2048];
	        while ((l = instream.read(tmp)) != -1) {
	            outstream.write(tmp, 0, l);
	        }
	    }

	    // non-javadoc, see interface HttpEntity
	    public boolean isStreaming() {
	        return !this.contentObtained && this.content != null;
	    }

	    // non-javadoc, see interface HttpEntity
	    public void consumeContent() throws IOException {
	        if (content != null) {
	            content.close(); // reads to the end of the entity
	        }
	    }
}

class MyBytesEntity implements HttpEntity{
	private byte[] bytes;
	private String contentEncoding ;
	
	/**
	 * 默认是utf8编码。--图片需要编码吗？？
	 * 
	 * @param bytes
	 */
	public MyBytesEntity(byte[] bytes){
		this.bytes = bytes;
		
	}
	
	public MyBytesEntity(byte[] bytes,String ContentEncoding){
		this.bytes = bytes;
		this.contentEncoding = ContentEncoding;
	}
	
	@Override
	public boolean isRepeatable() {
		return false;
	}

	@Override
	public boolean isChunked() {
		return false;
	}

	@Override
	public long getContentLength() {
		return bytes.length;
	}

	@Override
	public Header getContentType() {
		return null;
	}

	@Override
	public Header getContentEncoding() {
		Header contentEncoding_header;
		if(contentEncoding == null || "".equals(contentEncoding)){
			contentEncoding_header= new BasicHeader("type", "utf-8");
		}else{
			contentEncoding_header  = new BasicHeader("type",contentEncoding);
		}
		return contentEncoding_header;
	}

	@Override
	public InputStream getContent() throws IOException,
			IllegalStateException {
		InputStream is = null;
		
		
		return null;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		outstream.write(bytes);
		outstream.flush();
		outstream.close();
	}

	@Override
	public boolean isStreaming() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void consumeContent() throws IOException {
		// TODO Auto-generated method stub
		
	}
}


