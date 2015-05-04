package cn.gaomh.paint;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class PaintActivity extends Activity {

	private Paint mPaint;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new PaintView(this));
        
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.argb(10, 255, 0, 0));
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(1);
    }
    
    public class PaintView extends View {
        private Bitmap  mBitmap;
        private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;

        public PaintView(Context c) {
            super(c);

            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

            canvas.drawPath(mPath, mPaint);
        }

        private float mX, mY, mR;
        private final float baseR = 20;
        private Color mC;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            mR = baseR;
            Random rand = new Random();
            mPaint.setColor(Color.argb(17, rand.nextInt(255)+1, rand.nextInt(255)+1, rand.nextInt(255)+1));
            mPath.addCircle(x, y, mR, Path.Direction.CW);
        }
        
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            float dR = baseR;
            //if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            	//mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            	dR -= .1*Math.sqrt(dx*dx+dy*dy);
            	if (dR <= 5){
            		dR=5;
            	}
            	//mPaint.setColor(Color.argb(5, 0, 0, 0));
            	fillInGap(mX, mY, x, y, (int)mR, (int)dR);
            	
            	/*mPath.addCircle(x, y, dR, Path.Direction.CW);
            	mCanvas.drawPath(mPath, mPaint);
            	mPath.close();
            	mPath.reset();
            	mPath.moveTo(x, y);
            	mX=x;
            	mY=y;*/
            	mR = dR;
            //}
        }
        private void touch_move2(float x, float y){
        	float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            float dR = baseR;
            dR -= .1*Math.sqrt(dx*dx+dy*dy);
            if (dR <= 5){
        		dR=5;
        	}
            mPath.addCircle(x, y, dR, Path.Direction.CW);
            mCanvas.drawPath(mPath, mPaint);
        	mPath.close();
        	mPath.reset();
        	mPath.moveTo(x, y);
        	mX=x;
        	mY=y;
        	mR = dR;
        }
        private void touch_up() {
            mPath.lineTo(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }
        
        public void fillInGap(float startX, float startY, float endX, float endY,
        		int startR, int endR){
    		
        	int dX = (int) Math.abs(endX-startX);
    		int dY = (int) Math.abs(endY-startY);
    		int distance = (int) Math.sqrt(dX*dX + dY*dY);
    		
    		for (int i = 1; i < distance; i++){
    			int gapX = (int) (startX+(int)(i*(endX-startX)/((float)distance)));
    			int gapY = (int) (startY+(int)(i*(endY-startY)/((float)distance)));
    			int gapR = startR + (int)(i*(endR-startR)/((float)(distance)));
    			//int gapYellow = 2*(prevDistance + (int)(i*(distance-prevDistance)/((float)distance)));
    			//_graphics.add(gapCircle);
    			mPath.addCircle(gapX, gapY, gapR, Path.Direction.CW);
            	mCanvas.drawPath(mPath, mPaint);
            	mPath.close();
            	mPath.reset();
            	mPath.moveTo(gapX, gapY);
            	mX=gapX;
            	mY=gapY;
            	mR = gapR;
    		}
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