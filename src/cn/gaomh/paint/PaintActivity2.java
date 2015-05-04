package cn.gaomh.paint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.gaomh.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class PaintActivity2 extends Activity implements ColorPickerDialog.OnColorChangedListener{
    /** Called when the activity is first created. */
	private static final String TAG = "Paint2SS -> ImageManager";
    private static final String APPLICATION_NAME = "Paint2SS";  
    private static final Uri IMAGE_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
    private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/" + APPLICATION_NAME;
    
    private MyView mView;
    private Intent intent;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new MyView(this);
        Drawable bg = getResources().getDrawable(R.drawable.bg_c_2);
        mView.setBackground(bg);
        setContentView(mView);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(8);
        
        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                                       0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
        
        //動作確認
//        Bitmap bitmap0 = BitmapFactory.decodeFile("/mnt/sdcard/Paint2SS/20120116_055902.png");
//		mView.mCanvas.drawBitmap(bitmap0, 0, 0, null);
        //これはエラーが出た。
        //mView.mCanvas.setBitmap(bitmap0);
        
//        ////test
//        intent = getIntent();
//        if(intent != null){
//        	Log.i("intent","not null");
//        	Bundle bundle = intent.getExtras();
//        	if (bundle!=null){
//	        	if (bundle.containsKey("file")) {
//	        		//どうもgetParcelableではキーが存在しない場合はnullが戻ってくる。
//	        		Bitmap bitmap_Paint2SS = (Bitmap)bundle.getParcelable("net.masanoriyono.Paint2SS.BITMAP");
//	        	}
//        	}
//        }
//        ////test end.
        intent = getIntent();
        if(intent != null){
        	Log.i("intent","not null");
            if ("bitmap".equals(intent.getAction())) {
            	Bundle bundle = intent.getExtras();
            	//これがnullの場合があるのにチェックせずに
            	//キーの存在をチェックしていたのでエラーが起きていた。
            	if (bundle!=null){
		        	if (bundle.containsKey("file")) {
		        		//どうもgetParcelableではキーが存在しない場合はnullが戻ってくる。
		        		//Bitmap bitmap_Paint2SS = (Bitmap)bundle.getParcelable("net.masanoriyono.Paint2SS.BITMAP");
		        		String Paint2SS_imagefile = intent.getStringExtra("file");
	    	        	//String Paint2SS_imagefile = intent.getStringExtra("net.masanoriyono.Paint2SS.BITMAP");
	    	        	Log.i("onDraw",Paint2SS_imagefile);
	    	        	//ファイル読み込み。
	//    	        	File f = new File(Paint2SS_imagefile);
	//    	        	BitmapFactory.Options options = new BitmapFactory.Options();
	//    	    		options.inJustDecodeBounds = true;
	//    	    		BitmapFactory.decodeFile(Paint2SS_imagefile);
	//    	    		options.inJustDecodeBounds = false;
	//    	    		//options.inSampleSize = nScale;
	    	    		Bitmap bitmap = BitmapFactory.decodeFile(Paint2SS_imagefile);
	    	    		mView.mCanvas.drawBitmap(bitmap, 0, 0, null);
		            }
            	}
	        }
        }
    }
    
    
    private Paint       mPaint;
    private MaskFilter  mEmboss;
    private MaskFilter  mBlur;
    
    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    public class MyView extends View {
        
//        private static final float MINP = 0.25f;
//        private static final float MAXP = 0.75f;
        
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        
        public MyView(Context c) {
            super(c);
            //画面サイズを取得
            WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
            Display disp = wm.getDefaultDisplay();
            int display_width = disp.getWidth();
            int display_height = disp.getHeight();
            
            
            mBitmap = Bitmap.createBitmap(display_width, display_height, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            //背景色
        	//canvas.drawColor(0xFFAAAAAA);
//        	canvas.drawColor(0xFFFFFFFF);
        	canvas.drawColor(Color.TRANSPARENT);
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
            
        }
        
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;
        
        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                mX = x;
                mY = y;
            }
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }
    
    private static final int SAVE_MENU_ID = Menu.FIRST;
    private static final int COLOR_MENU_ID = Menu.FIRST+1;
    private static final int EMBOSS_MENU_ID = Menu.FIRST + 2;
    private static final int BLUR_MENU_ID = Menu.FIRST + 3;
    private static final int ERASE_MENU_ID = Menu.FIRST + 4;
    private static final int SRCATOP_MENU_ID = Menu.FIRST + 5;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        menu.add(0, SAVE_MENU_ID, 0, "Save").setShortcut('3', 's');
        menu.add(0, COLOR_MENU_ID, 0, "Color").setShortcut('3', 'c');
        menu.add(0, EMBOSS_MENU_ID, 0, "Emboss").setShortcut('4', 's');
        menu.add(0, BLUR_MENU_ID, 0, "Blur").setShortcut('5', 'z');
        menu.add(0, ERASE_MENU_ID, 0, "Erase").setShortcut('5', 'z');
        menu.add(0, SRCATOP_MENU_ID, 0, "SrcATop").setShortcut('5', 'z');

        /****   Is this the mechanism to extend with filter effects?
        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(
                              Menu.ALTERNATIVE, 0,
                              new ComponentName(this, NotesList.class),
                              null, intent, 0, null);
        *****/
        return true;
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xFF);
        
        boolean f_res;
        
        switch (item.getItemId()) {
        	case SAVE_MENU_ID:
        		long dateTaken = System.currentTimeMillis();  
                String filename = createName(dateTaken) + ".png";  
                
                Log.v("SAVE_MENU",PATH + " " +filename);
                f_res = saveToFile(PATH,filename,dateTaken);
                if (f_res){
                	Toast.makeText(this, "Save to gallery", Toast.LENGTH_SHORT).show();
                }
                
	            return true;
            case COLOR_MENU_ID:
                new ColorPickerDialog(this, this, mPaint.getColor()).show();
                return true;
            case EMBOSS_MENU_ID:
                if (mPaint.getMaskFilter() != mEmboss) {
                    mPaint.setMaskFilter(mEmboss);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case BLUR_MENU_ID:
                if (mPaint.getMaskFilter() != mBlur) {
                    mPaint.setMaskFilter(mBlur);
                } else {
                    mPaint.setMaskFilter(null);
                }
                return true;
            case ERASE_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                        PorterDuff.Mode.CLEAR));
                return true;
            case SRCATOP_MENU_ID:
                mPaint.setXfermode(new PorterDuffXfermode(
                                                    PorterDuff.Mode.SRC_ATOP));
                mPaint.setAlpha(0x80);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
//    @Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			this.finish();
//			this.moveTaskToBack(true);
//			return false;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

	private String createName(long dateTaken) {  
        return DateFormat.format("yyyyMMdd_kkmmss", dateTaken).toString();  
    }
    private boolean saveToFile(String directory,String filename,long dateTaken) {
    	mView.setDrawingCacheEnabled(false);
    	mView.setDrawingCacheEnabled(true);
//        Bitmap bitmap0 = Bitmap.createBitmap(mView.getDrawingCache());
        Bitmap bitmap0 = mView.getDrawingCache();
        
    	OutputStream outputStream = null;  
        try {  
            File dir = new File(directory+ "/");  
            if (!dir.exists()) {  
                dir.mkdirs();  
                Log.d(TAG, dir.toString() + " create");  
            }
            
            String filePath = directory + "/" + filename;
            
            File file = new File(directory + "/" + filename);  
            if (file.createNewFile()) {  
                outputStream = new FileOutputStream(file);  
                if (bitmap0 != null) {  
                    bitmap0.compress(CompressFormat.PNG, 100, outputStream);
                    
                    //ギャラリーへの登録。
                	ContentResolver contentResolver = getContentResolver();
                	
                	ContentValues values = new ContentValues();  
                    values.put(Images.Media.TITLE, filename);  
                    values.put(Images.Media.DISPLAY_NAME, filename);  
                    values.put(Images.Media.DATE_TAKEN, dateTaken);  
                    values.put(Images.Media.MIME_TYPE, "image/png");  
                    values.put(Images.Media.DATA, filePath);  
                    
                	contentResolver.insert(IMAGE_URI, values);
                	
                	Log.d(TAG, file.toString() + " write");
                	
                	return true;
                }  
            }  
      
        } catch (FileNotFoundException ex) {  
            Log.w(TAG, ex);  
            return false;  
        } catch (IOException ex) {  
            Log.w(TAG, ex);  
            return false;  
        } finally {  
            if (outputStream != null) {  
                try {  
                    outputStream.close();  
                } catch (Throwable t) {  
                }  
            }  
        }
		return false; 
    }
}