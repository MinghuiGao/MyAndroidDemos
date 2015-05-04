package cn.gaomh.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOPenHelper extends SQLiteOpenHelper {//http://mobile.51cto.com/android-320675.htm  --->sqlite command...

	public MyOPenHelper(Context context) {
		super(context, "gaomh_demo", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/**
		 * cont
		 */
		db.execSQL("create table note (id integer primary key autoincrement,title text,content text,date_time text,photo text,rec text,vedio text)");
		System.out.println("database was created!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
