package cn.gaomh.animations;

import cn.gaomh.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;

public class AllAniActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allanims);
		
		final Button bt = (Button) findViewById(R.id.bt_allanimas_fresh);
		bt.setVisibility(View.VISIBLE);
		bt.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				RotateAnimation rotateAnimation = new RotateAnimation(0f,
						3600f, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				rotateAnimation.setDuration(3000);
				bt.startAnimation(rotateAnimation);
			}
		});
	}
	
	public static void loadAllAniActivity(Context context,Object...objects){
		Intent intent = new Intent(context,AllAniActivity.class);
		context.startActivity(intent);
	}
	
}
