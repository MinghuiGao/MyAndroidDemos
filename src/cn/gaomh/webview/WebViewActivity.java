package cn.gaomh.webview;

import cn.gaomh.R;
import cn.gaomh.paint.PaintActivity2.MyView;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WebViewActivity extends Activity {

	private WebView wb_myweb;
	private LinearLayout ll_signature;
    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    private Paint       mPaint;
    private MaskFilter  mEmboss;
    private MaskFilter  mBlur;
    private MyView mView;
    private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		
		String url = "file:///android_asset/login/login.html";
		wb_myweb = (WebView) findViewById(R.id.wv_myweb);
		ll_signature = (LinearLayout) findViewById(R.id.ll_signature);
		
/*		Drawable d = getResources().getDrawable(R.drawable.bg_c_0);
		wb_myweb.setBackground(d);*/
		wb_myweb.clearCache(true);
		//add a key listener to the webview.
		wb_myweb.setOnKeyListener(new OnKeyListener(){
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch(keyCode){
				case KeyEvent.KEYCODE_MENU:
					wb_myweb.loadUrl("javascript:javaWork('this is from java a Parameter.....')");
					break;
				case KeyEvent.KEYCODE_BACK:
					onDestroy();
				}
				
				return false;
			}
			
		});
		//add support for javascript
		WebSettings ws = wb_myweb.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setPluginState(PluginState.ON);
		
		//add the access to javascript..
		wb_myweb.addJavascriptInterface(new Object(){
			//Log
			public void log(String log){
				Log.i("web", log);
			}
			
			//show the meg from js..
			public void show(String str){
				Toast.makeText(WebViewActivity.this, str, 0).show();
				
			}
			//start an acitivty from js
			public void DumpActivity(String packet , String activityName){
				Intent intent = new Intent(Intent.ACTION_MAIN, null);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				ComponentName cn = new ComponentName(packet,activityName);
				intent.setComponent(cn);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				startActivity(intent);
				
			}
			//goto menu
			public void goToMain(){
				Intent intent = new Intent(WebViewActivity.this,RightMenuActivity.class);
				startActivity(intent);
				WebViewActivity.this.finish();
			}
			
			//check the user
			public boolean checkUser(String name,String password){
				//TODO: check in the database.
				if("gaomh".equals(name) && "daoli".equals(password)){
					return true;
				}else{
					return false;
				}
			}
			
		},"myfunction");
		
		
		
		wb_myweb.loadUrl(url);
		//paint
        mView = new MyView(this);
        Drawable bg = getResources().getDrawable(R.drawable.bg_c_2);
        mView.setBackground(bg);
        ll_signature.addView(mView);
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);
        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },
                                       0.4f, 6, 3.5f);
        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
	}
	
	public class MyView extends View {
        
//      private static final float MINP = 0.25f;
//      private static final float MAXP = 0.75f;
      
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
//      	canvas.drawColor(0xFFFFFFFF);
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
	
	
}
