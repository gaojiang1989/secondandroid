package com.active.myjp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.active.myjp.R;
import com.tool.CommonTool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Menu extends Activity {
	
	private Button wordBtn = null;       //单词学习
	private Button btnGrammar = null;    //句子学习
	private Button btnDatainit = null;   //数据初始化
	private Button btnOut=null;         //退出系统

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        wordBtn = (Button)super.findViewById(R.id.word);
        wordBtn.setOnClickListener(new WordOnClick());
        
        btnGrammar = (Button)super.findViewById(R.id.sentence);
        btnGrammar.setOnClickListener(new GrammarOnClick());
        
        btnDatainit = (Button)super.findViewById(R.id.datainit);
        btnDatainit.setOnClickListener(new DatainitOnClick());
        
        btnOut = (Button)super.findViewById(R.id.outsystem);
        btnOut.setOnClickListener(new OutSystemOnClick());
        
        initDb();
        initDate();
//        btnTranslate = (Button)super.findViewById(R.id.translate);
//        btnTranslate.setOnClickListener(new TranslateOnClick());
//        
//        btnArticle = (Button)super.findViewById(R.id.article);
//        btnArticle.setOnClickListener(new ArticleOnClick());
    }
	private class OutSystemOnClick implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			android.os.Process.killProcess(android.os.Process.myPid());  
		}
	}
	
	
	private class DatainitOnClick implements OnClickListener{
		
		@Override
		public void onClick(View arg0) {
			new AlertDialog.Builder(Menu.this).  
            setIcon(R.drawable.ic_launcher).//图标  
            setTitle("初始化?").//标题  
            setMessage("您确认要初始化数据库吗？").//提示内容  
            setPositiveButton("确认", new DialogInterface.OnClickListener() {//确定  
                @Override  
                public void onClick(DialogInterface arg0, int arg1) {  
                    //yes to do  
                	writeDb();
        			initDate();
        			Toast.makeText(Menu.this, "*****初始化成功*****",Toast.LENGTH_LONG).show();
                }  
            }).setNegativeButton("取消", new DialogInterface.OnClickListener(){//取消  
                @Override  
                public void onClick(DialogInterface arg1,int witch){  
                    //no to do  
                }  
            }). 
            show(); 
			
		}
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
			Intent it = new Intent(Menu.this,SentenceMenu.class);
//			it.putExtra("info","单词学习");
			Menu.this.startActivity(it);
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
	
	private void initDb(){
		//判断，如果是打包后重新进来，则更改了版本号，就要重新数据库。否则，就不重写数据库
		SharedPreferences share = Menu.this.getSharedPreferences("gj_rmm",Activity.MODE_PRIVATE);
		String version = share.getString("curVersion","");
//		boolean isFirstIn = share.getBoolean("isFirstIn",true);
		if(!version.equals(CommonTool.curVersion)){
			writeDb();
	    	SharedPreferences.Editor edit = share.edit();
	    	edit.putString("curVersion",CommonTool.curVersion);
	    	edit.commit();
		}
	}
	
	@SuppressLint("SdCardPath") 
	private void writeDb(){
    	String DB_PATH = "/data/data/com.active.myjp/databases/";
    	String DB_NAME = "jp.db";
    	File f = new File(DB_PATH);
		if(!f.exists()){
			f.mkdir();
		}
		try{
			InputStream is = getBaseContext().getAssets().open(DB_NAME);
			OutputStream os = new FileOutputStream(DB_PATH+DB_NAME);
			byte[]buffer = new byte[1024];
			int length;
			while((length=is.read(buffer))>0){
				os.write(buffer,0,length);
			}
			
			os.flush();
			os.close();
			is.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
