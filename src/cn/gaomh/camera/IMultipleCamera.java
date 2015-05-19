package cn.gaomh.camera;

import java.io.File;

/** 
 * 功能描述 : 多媒体相机
 * @类型名称 IMultipleCamera
 * @版本 1.0
 * @创建者 gaominghui
 * @创建时间 2014-10-20 下午5:46:46
 * @版权所有 ©2014 CTFO
 * @修改者 gaominghui
 * @修改时间 2014-10-20 下午5:46:46
 * @修改描述 
 */
public interface IMultipleCamera {

	static final int TAKE_PHOTO = 0x4001;
	static final int TAPE = 0x4002;
	
	public void startRecording();
	
	/**
	 * 功能描述：
	 * @param foler 文件上传分类路径
	 * @param fileName 文件名
	 * @return 
	 * @版本 1.0
	 * @创建者 gaominghui
	 * @创建时间 2014-10-29 下午5:17:33
	 * @版权所有 ©2014 CTFO
	 * @修改者 gaominghui
	 * @修改时间 2014-10-29 下午5:17:33
	 * 修改描述 
	 */
	public File startRecording(String folder,String fileName);
	
	public void pauseRecording();

	public String stopRecording();

	public void replay();

	public void saveToLocal();

	public void upload();

	public void takePhoto();
	
	
	/**
	 * 功能描述：
	 * @param foler 文件上传分类路径
	 * @param fileName 文件名
	 * @return 
	 * @版本 1.0
	 * @创建者 gaominghui
	 * @创建时间 2014-10-27 下午4:57:27
	 * @版权所有 ©2014 CTFO
	 * @修改者 gaominghui
	 * @修改时间 2014-10-27 下午4:57:27
	 * 修改描述 
	 */
	public File takePhoto(String foler,String fileName);

	public void turnOnFlash();

	public void turnOffFlash();

	public void flash();

	public void destory();
	
	/** 相机的按钮回调接口 **/
	public interface ButtonCallback{
		public void onBackClicked();
		public void onShootClicked();
		public void onSuccess();
	}

}