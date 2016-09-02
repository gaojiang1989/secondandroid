package com.active.myjp;

import com.db.DataBaseHelper;
import com.model.MyWord;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class WordDetail extends Activity {

	private int wordId;
	TableLayout layout;
	SQLiteDatabase db = null;
	public void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		
		Intent it = super.getIntent();
		wordId = it.getIntExtra("wordId",0);
		
		SQLiteOpenHelper database = new DataBaseHelper(this);
		db = database.getReadableDatabase();
		
		
		layout = new TableLayout(this);
		ScrollView sv = new ScrollView(this);
		TableLayout.LayoutParams layoutParam = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		sv.addView(layout);
		super.setContentView(sv, layoutParam);
		
		MyWord w = getWordById(wordId);
		addTextView(w.getJpword());
//		addTextView(w.getJpch());
		if(w.getJpch().equals(w.getJpword())||w.getChword().equals(w.getJpword())){
			addTextView(w.getChword());
		}else{
			addTextView(w.getJpch());
			addTextView(w.getChword());
			
		}
		addTextView(w.getSpeech());
//		addTextView(w.getDateid());
		
//		List<Example> list = getExampleByWord(w,w.getLessonid());
//		for(Example emp:list){
//			if(emp.getChtext()!=null&&!emp.getChtext().equals("")){
//				addTextView(emp.getJptext());
//				addTextView(emp.getChtext());
//			}else{
//				addTextView(emp.getJptext());
//			}
//		}
	}
	
	private int getScreenWidth(){
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	private void addTextView(String msg){
		TableRow row = new TableRow(this);
		TextView tv = new TextView(this);
		tv.setPadding(10,10,10,10);
		tv.setWidth(getScreenWidth());
//		tv.setBackgroundColor(Color.GRAY);
//		tv.setTextSize(20);
		tv.setText(msg);
		row.addView(tv);
		layout.addView(row);
	}
	
    /**
     * 根据id查询word
     * @param wordid
     * @return
     */
	public MyWord getWordById(int wordid) {
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordDetail.this);
		SQLiteDatabase db = null;
		db = database.getReadableDatabase();
		Cursor c = db
				.rawQuery("SELECT * FROM myword where id=" + wordid, null);
		MyWord w = null;
		while (c.moveToNext()) {
			w = setWord(c);
		}
		c.close();
		return w;
	}

	/**
	 * 使用游标设置word
	 * @param c
	 * @return
	 */
	private MyWord setWord(Cursor c) {
		MyWord w;
		int id = c.getInt(c.getColumnIndex("id"));
		String jpword = c.getString(c.getColumnIndex("jpword"));
		String chword = c.getString(c.getColumnIndex("chword"));
		String jpch = c.getString(c.getColumnIndex("jpch"));
		String speech = c.getString(c.getColumnIndex("speech"));
		int type = c.getInt(c.getColumnIndex("typeid"));
		int dateid = c.getInt(c.getColumnIndex("dateid"));
		String desc = c.getString(c.getColumnIndex("desc"));
		w = new MyWord(id, jpword, jpch, chword,speech,type,dateid,desc);
		return w;
	}
	
}
