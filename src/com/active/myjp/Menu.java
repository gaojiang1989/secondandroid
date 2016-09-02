package com.active.myjp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.active.myjp.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Menu extends Activity {
	
	private Button wordBtn = null;       //单词学习
	private Button btnGrammar = null;    //句子学习
//	private Button btnTranslate = null;  //句子翻译
//	private Button btnArticle = null;    //文章背诵
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        wordBtn = (Button)super.findViewById(R.id.word);
        wordBtn.setOnClickListener(new WordOnClick());
        
        btnGrammar = (Button)super.findViewById(R.id.sentence);
        btnGrammar.setOnClickListener(new GrammarOnClick());
        
        initDate();
//        btnTranslate = (Button)super.findViewById(R.id.translate);
//        btnTranslate.setOnClickListener(new TranslateOnClick());
//        
//        btnArticle = (Button)super.findViewById(R.id.article);
//        btnArticle.setOnClickListener(new ArticleOnClick());
    }
	
	private class WordOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent it = new Intent(Menu.this,WordMenu.class);
			it.putExtra("info","单词学习");
			Menu.this.startActivity(it);
		}
	}
	
	private class GrammarOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
//			Intent it = new Intent(Menu.this,GrammarView.class);
////			it.putExtra("info","单词学习");
//			Menu.this.startActivity(it);
		}
	}
	
	/**
	 * 每次开启系统，初始化一次mydate表
	 */
	@SuppressLint("SimpleDateFormat") 
	private void initDate(){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		int daynum = getDaynum(getLastDate(),df.format(ca.getTime()));
		Date startTime = null;
		try {
			startTime=df.parse(getLastDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ca.setTime(startTime);
		for(int i=0;i<daynum;i++){
			ca.add(Calendar.DAY_OF_YEAR,1);
			addDate(df.format(ca.getTime()));
		}
	}
	
	/**
	 * 获取mydate表中的最后一天
	 * @return
	 */
	private String getLastDate(){
		String sql = "select * from mydate order by date desc limit 1";
		SQLiteOpenHelper database = new com.db.DataBaseHelper(Menu.this);
		SQLiteDatabase db = database.getReadableDatabase();
		Cursor c = db.rawQuery(sql, null);
		while (c.moveToNext()) {
			return c.getString(c.getColumnIndex("date"));
		}
		c.close();
		return "";
	}
	
	/**
	 * 添加一天
	 * @param date
	 */
	private void addDate(String date){
		String sql = "insert into mydate(date) values('"+date+"')";
		SQLiteOpenHelper database = new com.db.DataBaseHelper(Menu.this);
		SQLiteDatabase db = database.getReadableDatabase();
		db.execSQL(sql);
	}
	
	/**
	 * 根据两个yyyy-MM-dd格式的时间，计算天数差
	 * @param date1
	 * @param date2
	 * @return
	 */
	@SuppressLint("SimpleDateFormat") 
	private int getDaynum(String date1,String date2){
		Calendar ca = Calendar.getInstance();
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
		System.out.println(df.format(ca.getTime()));
		try {
			Date time1 = df.parse(date1);
			Date time2 = df.parse(date2);
			return (int)(time2.getTime()-time1.getTime())/1000/60/60/24;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
