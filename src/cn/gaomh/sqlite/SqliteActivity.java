package cn.gaomh.sqlite;

import cn.gaomh.R;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class SqliteActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqlite_note);
		MyOPenHelper myHelper = new MyOPenHelper(SqliteActivity.this);
		SQLiteDatabase writableDatabase = myHelper.getWritableDatabase();
//		writableDatabase.
		
		
		
	}
	
	
	
}
