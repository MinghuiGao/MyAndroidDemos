package cn.gaomh;

import java.util.ArrayList;

import cn.gaomh._2dbarcode.CaptureActivity;
import cn.gaomh.animations.AllAniActivity;
import cn.gaomh.camera.MultipleCameraActivity;
import cn.gaomh.camera.MyCameraActivity;
import cn.gaomh.location.GetLocationActivity;
import cn.gaomh.paint.PaintActivity;
import cn.gaomh.paint.PaintActivity2;
import cn.gaomh.paint.PaintActivity3;
import cn.gaomh.shake.ShakeActivity;
import cn.gaomh.shortcut.CreateShortCutActivity;
import cn.gaomh.sqlite.SqliteActivity;
import cn.gaomh.vedioCapture.VedioCaptureActivity;
import cn.gaomh.viewFliper.FliperActivity;
import cn.gaomh.webview.WebViewActivity;
import cn.gaomh.ysyt.IIActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

public class GaomhDemosActivity extends Activity implements OnClickListener, OnItemClickListener, OnDrawerCloseListener, OnDrawerOpenListener {
	
	private EditText et_search;
	private Button bt_search;
	private Button bt_shake;
	private Button bt_anims;
	private Button bt_createShortCut;
	private Button bt_captureVedio;
	
	//menu
	private ListView lv_menu;
	private MySlidingDrawer sd_menu;
//	String[] menu = {"one","two","three","one","two","three","one","two","three","one","two","three"};
	ArrayList<String> menu = new ArrayList<String>();
	private ImageView iv_menu;
	private View root;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = View.inflate(GaomhDemosActivity.this, R.layout.main, null);
        setContentView(root);
        lv_menu = (ListView)root.findViewById(R.id.lv_menu);
        sd_menu = (MySlidingDrawer) root.findViewById(R.id.sd_menu);
        iv_menu = (ImageView) root.findViewById(R.id.iv_menu);
        lv_menu.setDivider(null);
        lv_menu.setAdapter(new MyAdapter());
        
        lv_menu.setOnItemClickListener(this);
        
        //让抽屉实现开关监听。
        sd_menu.setOnDrawerOpenListener(this);
        sd_menu.setOnDrawerCloseListener(this);
        
        //添加菜单。。。=========================
        menu.add("摇一摇");
        menu.add("刷新动画");
        menu.add("创建快捷方式");
        menu.add("照相");
        menu.add("二维码扫描");
        menu.add("二维码生成");
        menu.add("画画");
        menu.add("画画2");
        menu.add("画画3");
        menu.add("Implicit intention");
        menu.add("NOTE");
        menu.add("webview");
        menu.add("Fliper");
        menu.add("Location");
        menu.add("自定义相机");
        menu.add("相机类");
        
//        PackageManager pm = getPackageManager();
//        		PackageParser.Package pkg = packageParser.parsePackage(
//                        sourceFile, archiveFilePath, metrics, 0);
//                if (pkg == null) {
//                    return null;
//                }
//                if ((flags & GET_SIGNATURES) != 0) {
//                    packageParser.collectCertificates(pkg, 0);
//                }
        
    }
    //自定义adapter
	class MyAdapter extends BaseAdapter{
		public int getCount() {return menu.size();}
		public Object getItem(int position) {return null;}
		public long getItemId(int position) {return position;}
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)convertView = View.inflate(GaomhDemosActivity.this, R.layout.menu_item,null);
			Button bt_menu_list = (Button) convertView.findViewById(R.id.bt_menu_list);
			bt_menu_list.setText(menu.get(position));
			return convertView;
		}
	}
	//====================
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		System.out.println("viewID : " + view.getId()+ "position : "+ position);
		switch(position){
			case 0://摇一摇
				ShakeActivity.loadShake(GaomhDemosActivity.this);
				break;
			case 1://刷新动画
				AllAniActivity.loadAllAniActivity(GaomhDemosActivity.this);
				break;
			case 2://创建快捷方式
				Intent createShortCut = new Intent(GaomhDemosActivity.this,CreateShortCutActivity.class);
				startActivity(createShortCut);
				break;
			case 3://照相
				Intent captureVedio = new Intent(GaomhDemosActivity.this,VedioCaptureActivity.class);
				startActivity(captureVedio);
				break;
			case 4://二维码扫描
				Intent barCode = new Intent(GaomhDemosActivity.this,CaptureActivity.class);
				startActivityForResult(barCode, Constant.BarCode);
				break;
			case 5://二维码生成
				break;
			case 6://画画
				Intent paint = new Intent(GaomhDemosActivity.this,PaintActivity.class);
				startActivity(paint);
				break;
			case 7://画画2--较流畅
				Intent paint2 = new Intent(GaomhDemosActivity.this,PaintActivity2.class);
				startActivity(paint2);
				break;
			case 8://画画3
				Intent paint3 = new Intent(GaomhDemosActivity.this,PaintActivity3.class);
				startActivity(paint3);
				break;
			case 9://Implicit intention
				Intent implicit = new Intent(GaomhDemosActivity.this,IIActivity.class);
//				Intent implicit = new Intent();
//				implicit.setAction("cn.gaomh.ii");
				startActivity(implicit);
				break;
			case 10://sqlite demo
				Intent sqlLite = new Intent(GaomhDemosActivity.this,SqliteActivity.class);
				startActivity(sqlLite);
				break;
			case 11://webview
				Intent webview = new Intent(GaomhDemosActivity.this,WebViewActivity.class);
				startActivity(webview);
				break;
			case 12:
				Intent fliper = new Intent(GaomhDemosActivity.this,FliperActivity.class);
				startActivity(fliper);
				break;
			case 13:
				Intent getLocation = new Intent(GaomhDemosActivity.this,GetLocationActivity.class);
				startActivity(getLocation);
				break;
			case 14:
				Intent camera = new Intent(GaomhDemosActivity.this,MyCameraActivity.class);
				startActivity(camera);
				break;
			case 15:
				Intent camera2 =  new Intent(GaomhDemosActivity.this,MultipleCameraActivity.class);
				startActivity(camera2);
			default:
				break;
		}
	}
	
	@Override//handle the data return from 2d barcode captureActivity....
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(resultCode){
		case Activity.RESULT_OK:
			TextView tv_barcode = new TextView(GaomhDemosActivity.this);
			String res = data.getExtras().getString("res");
			tv_barcode.setText("扫描结果：" + res);
			ArrayList<View> views = new ArrayList<View>();
			views.add(tv_barcode);
			root.addTouchables(views);
			break;
		default: break;
		}
	}
	
	@Override//button's onclick...
	public void onClick(View v) {}
	
	//处理抽屉开关
	public void onDrawerClosed() {iv_menu.setImageDrawable(getResources().getDrawable(R.drawable.buttton_in));}
	public void onDrawerOpened() {iv_menu.setImageDrawable(getResources().getDrawable(R.drawable.button_out));}
}