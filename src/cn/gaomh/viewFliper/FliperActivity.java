package cn.gaomh.viewFliper;

import cn.gaomh.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

/**
 * AndroidÊµÏÖ×óÓÒ»¬¶¯Ð§¹û
 * @author Administrator
 *
 */
public class FliperActivity extends Activity implements OnGestureListener {
	private ViewFlipper flipper;
	private GestureDetector detector;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fliper);
        
        detector = new GestureDetector(this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper1);

		flipper.addView(addTextView(R.drawable.bg_s_1));
		flipper.addView(addTextView(R.drawable.bg_s_2));
		flipper.addView(addTextView(R.drawable.bg_s_3));
//		flipper.addView(addTextView(R.drawable.four));
//		flipper.addView(addTextView(R.drawable.five));
    }
    
    private View addTextView(int id) {
		ImageView iv = new ImageView(this);
		iv.setImageResource(id);
		return iv;
	}
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
    	return this.detector.onTouchEvent(event);
    }
    
    @Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
    
    @Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
			this.flipper.showNext();
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			this.flipper.showPrevious();
			return true;
		}
		
		return false;
	}
    
    @Override
    public void onLongPress(MotionEvent e) {
    	// TODO Auto-generated method stub
    	
    }
    
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
    		float distanceY) {
    	// TODO Auto-generated method stub
    	return false;
    }
    
    @Override
    public void onShowPress(MotionEvent e) {
    	// TODO Auto-generated method stub
    	
    }
    
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
    	// TODO Auto-generated method stub
    	return false;
    }
}