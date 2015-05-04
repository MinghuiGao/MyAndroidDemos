package cn.gaomh.shortcut;

import cn.gaomh.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateShortCutActivity extends Activity {

	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortcut);
    }
	
    public void click(View view){
    	Intent intent = new Intent();
    	//快捷方式连接的intent
    	Intent callIntent = new Intent();
    	callIntent.setAction(Intent.ACTION_CALL);
    	callIntent.setData(Uri.parse("tel:"+"12354"));
    	
    	intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
    	intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, callIntent);
    	intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "快捷方式");
    	intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,R.drawable.ic_launcher);
    	sendBroadcast(intent);
    }
    
}
