package cn.gaomh.webview;

import java.util.ArrayList;
import java.util.List;





import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.gaomh.R;

public class RightMenuActivity extends Activity {

    private static final String STATE_ACTIVE_POSITION = "net.simonvt.menudrawer.samples.RightMenuSample.activePosition";
    private static final String STATE_CONTENT_TEXT = "net.simonvt.menudrawer.samples.RightMenuSample.contentText";
	
    private static final int MENU_OVERFLOW = 1;

    private MenuDrawer mMenuDrawer;

    private DraggableDrawer dragableDrawer ;
    
    private MenuAdapter mAdapter;
    private ListView mList;
    
    private int mActivePosition = -1;
    private String mContentText;
    private TextView mContentTextView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        if (savedInstanceState != null) {
            mActivePosition = savedInstanceState.getInt(STATE_ACTIVE_POSITION);
            mContentText = savedInstanceState.getString(STATE_CONTENT_TEXT);
        }
        //draggableDrawer
//        dragableDrawer = new DraggableDrawer(RightMenuActivity.this);
        
        
        
        //Attaches the MenuDrawer to the Activity.
        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.MENU_DRAG_CONTENT, Position.LEFT);
        mMenuDrawer.setOffsetMenuEnabled(true);
        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_FULLSCREEN);
        mMenuDrawer.setContentView(R.layout.webview_mainmenu);

        
        //添加菜单的项目
        List<Object> items = new ArrayList<Object>();
        items.add(new Item("Item 1", R.drawable.ic_action_refresh_dark));
        items.add(new Item("Item 2", R.drawable.ic_action_select_all_dark));
        items.add(new Category("Cat 1"));
        items.add(new Item("Item 3", R.drawable.ic_action_refresh_dark));
        items.add(new Item("Item 4", R.drawable.ic_action_select_all_dark));
        items.add(new Category("Cat 2"));
        items.add(new Item("Item 5", R.drawable.ic_action_refresh_dark));
        items.add(new Item("Item 6", R.drawable.ic_action_select_all_dark));
		
     // A custom ListView is needed so the drawer can be notified when it's scrolled. This is to update the position
        // of the arrow indicator.
        mList = new ListView(this);
        mAdapter = new MenuAdapter(items);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mActivePosition = position;
				mMenuDrawer.setActiveView(view,position);
				mContentTextView.setText(((TextView)view).getText());
				mMenuDrawer.closeMenu(true);
				
			}
		});

        mMenuDrawer.setMenuView(mList);
        
        mContentTextView = (TextView) findViewById(R.id.contentText);
        mContentTextView.setText(mContentText);
        
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
        outState.putString(STATE_CONTENT_TEXT, mContentText);
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem overflowItem = menu.add(0, MENU_OVERFLOW, 0, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            overflowItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        overflowItem.setIcon(R.drawable.ic_menu_moreoverflow_normal_holo_light);
        Log.i("gaomh", "createOptionmenu...");
        return true;
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.i("gaomh", "on option item selected!");
        switch (item.getItemId()) {
            case MENU_OVERFLOW:
                mMenuDrawer.toggleMenu();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    	//user press the back key ...
        final int drawerState = mMenuDrawer.getDrawerState();
        Log.i("gaomh", "the drawer state is " + drawerState);
        if (drawerState == MenuDrawer.STATE_OPEN || drawerState == MenuDrawer.STATE_OPENING) {
            mMenuDrawer.closeMenu();
            return;
        }

        super.onBackPressed();
    }
	
	private class MenuAdapter extends BaseAdapter{
		
		private List<Object> mItems;
		
		public MenuAdapter(List<Object> items){
			this.mItems = items;
		}
		

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
        public int getViewTypeCount() {
            return 2;
        }

		
		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}


		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return getItem(position) instanceof Item;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
            Object item = getItem(position);

            if (item instanceof Category) {
                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.menu_row_category, parent, false);
                }

                ((TextView) v).setText(((Category) item).mTitle);

            } else {
                if (v == null) {
                    v = getLayoutInflater().inflate(R.layout.menu_row_item, parent, false);
                }

                TextView tv = (TextView) v;
                tv.setText(((Item) item).mTitle);
                tv.setCompoundDrawablesWithIntrinsicBounds(((Item) item).mIconRes, 0, 0, 0);
            }

            v.setTag(R.id.mdActiveViewPosition, position);

            if (position == mActivePosition) {
                mMenuDrawer.setActiveView(v, position);
            }

            return v;
		}
	}
	
	 private static class Item {

	        String mTitle;
	        int mIconRes;

	        Item(String title, int iconRes) {
	            mTitle = title;
	            mIconRes = iconRes;
	        }
	    }

	    private static class Category {

	        String mTitle;

	        Category(String title) {
	            mTitle = title;
	        }
	    }
	
}
