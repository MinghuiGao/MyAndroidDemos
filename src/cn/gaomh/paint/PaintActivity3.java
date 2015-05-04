package cn.gaomh.paint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import cn.gaomh.R;

import android.R.id;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;


public class PaintActivity3 extends Activity
  implements View.OnClickListener
{
  private Bitmap bitmap;
  private Canvas canvas;
  private Display display;
  private ImageView iv;
  private ImageView iv_b;
  private ImageView iv_g;
  private ImageView iv_r;
  private Paint paint;
  private int process = 2;
  private SeekBar sb;

  public void draw(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt3 / 2;
    float f1 = paramInt1 - i;
    int j = paramInt3 / 2;
    float f2 = paramInt2 - j;
    float f3 = paramInt3 / 2 + paramInt1;
    float f4 = paramInt3 / 2 + paramInt2;
    RectF localRectF = new RectF(f1, f2, f3, f4);
    Canvas localCanvas = this.canvas;
    Paint localPaint = this.paint;
    localCanvas.drawOval(localRectF, localPaint);
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
      return;
    case R.id.iv_b:
      this.paint.setColor(-16776961);
      return;
    case R.id.iv_r:
      this.paint.setColor(-65536);
      return;
    case R.id.iv_g:
    	this.paint.setColor(-16711936);
    	return ;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.paint3);
    Paint localPaint = new Paint();
    this.paint = localPaint;
    ImageView localImageView1 = (ImageView)findViewById(R.id.iv_b);
    this.iv_b = localImageView1;
    ImageView localImageView2 = (ImageView)findViewById(R.id.iv_r);
    this.iv_r = localImageView2;
    ImageView localImageView3 = (ImageView)findViewById(R.id.iv_g);
    this.iv_g = localImageView3;
    this.iv_b.setOnClickListener(this);
    this.iv_g.setOnClickListener(this);
    this.iv_r.setOnClickListener(this);
    SeekBar localSeekBar1 = (SeekBar)findViewById(R.id.sb);
    this.sb = localSeekBar1;
    SeekBar localSeekBar2 = this.sb;
    localSeekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
    {
        public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
        {
          PaintActivity3 localPaintActivity = PaintActivity3.this;
          int i = paramSeekBar.getProgress();
          localPaintActivity.process = i;
          Paint localPaint = PaintActivity3.this.paint;
          float f = PaintActivity3.this.process;
          localPaint.setStrokeWidth(f);
        }

        public void onStartTrackingTouch(SeekBar paramSeekBar)
        {
        }

        public void onStopTrackingTouch(SeekBar paramSeekBar)
        {
        }
      });
    this.paint.setColor(-65536);
    this.paint.setStrokeWidth(8.0F);
    ImageView localImageView4 = (ImageView)findViewById(R.id.iv);
    this.iv = localImageView4;
    Display localDisplay = getWindowManager().getDefaultDisplay();
    this.display = localDisplay;
    int i = this.display.getWidth();
    int j = this.display.getHeight() + -100;
    Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
    Bitmap localBitmap1 = Bitmap.createBitmap(i, j, localConfig);
    this.bitmap = localBitmap1;
    Bitmap localBitmap2 = this.bitmap;
    Canvas localCanvas = new Canvas(localBitmap2);
    this.canvas = localCanvas;
    ImageView localImageView5 = this.iv;
    View.OnTouchListener local2 = new View.OnTouchListener()
    {
      int startX;
      int startY;

      public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
      {
        switch (paramMotionEvent.getAction())
        {
	        case MotionEvent.ACTION_DOWN:
	        	System.out.println("按下...");
	            int i = (int)paramMotionEvent.getX();
	            this.startX = i;
	            int j = (int)paramMotionEvent.getY();
	            this.startY = j;
	        case MotionEvent.ACTION_MOVE:
	        	System.out.println("移动...");
	            int k = (int)paramMotionEvent.getX();
	            int m = (int)paramMotionEvent.getY();
	            Canvas localCanvas = PaintActivity3.this.canvas;
	            float f1 = this.startX;
	            float f2 = this.startY;
	            float f3 = k;
	            float f4 = m;
	            Paint localPaint = PaintActivity3.this.paint;
	            localCanvas.drawLine(f1, f2, f3, f4, localPaint);
	            PaintActivity3 localPaintActivity = PaintActivity3.this;
	            int n = PaintActivity3.this.process;
	            localPaintActivity.draw(k, m, n);
	            int i1 = (int)paramMotionEvent.getX();
	            this.startX = i1;
	            int i2 = (int)paramMotionEvent.getY();
	            this.startY = i2;
	            ImageView localImageView = PaintActivity3.this.iv;
	            Bitmap localBitmap = PaintActivity3.this.bitmap;
	            localImageView.setImageBitmap(localBitmap);
	        case MotionEvent.ACTION_UP:
	        	System.out.println("松手");
	        default:
	        	 break;
        }
		return true;
      }
    };
    localImageView5.setOnTouchListener(local2);
  }

  public void save(View paramView)
  {
    try
    {
      File localFile1 = Environment.getExternalStorageDirectory();
      String str1 = String.valueOf(System.currentTimeMillis());
      String str2 = str1 + ".jpg";
      File localFile2 = new File(localFile1, str2);
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile2);
      Bitmap localBitmap = this.bitmap;
      Bitmap.CompressFormat localCompressFormat = Bitmap.CompressFormat.JPEG;
      boolean bool = localBitmap.compress(localCompressFormat, 100, localFileOutputStream);
      Toast.makeText(this, "保存成功", 0).show();
      Intent localIntent1 = new Intent();
      Intent localIntent2 = localIntent1.setAction("android.intent.action.MEDIA_MOUNTED");
      Uri localUri = Uri.fromFile(Environment.getExternalStorageDirectory());
      Intent localIntent3 = localIntent1.setData(localUri);
      sendBroadcast(localIntent1);
      return;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException.printStackTrace();
      Toast.makeText(this, "保存失败", 0).show();
    }
  }
}