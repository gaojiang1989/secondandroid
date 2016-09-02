package com.active.myjp;

import java.util.ArrayList;
import java.util.List;

import com.active.myjp.R;
import com.control.MyDateArrayAdapter;
import com.model.MyDate;
import com.model.MyType;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class WordAdd extends Activity {
	
	private Button btnAdd = null;       //添加单词按钮
	private EditText etJpword;
	private EditText etJpch;
	private EditText etChword;
	private EditText etSpeech;
	private EditText etDesc;
	private Spinner spType;
	private Spinner spDate;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordadd);
        
        btnAdd = (Button)super.findViewById(R.id.wordaddbtnadd);
        etJpword = (EditText)super.findViewById(R.id.wordaddjpword);
        etJpch = (EditText)super.findViewById(R.id.wordaddjpch);
        etChword = (EditText)super.findViewById(R.id.wordaddchword);
        etSpeech = (EditText)super.findViewById(R.id.wordaddspeech);
        etDesc = (EditText)super.findViewById(R.id.wordadddesc);
        spType = (Spinner)super.findViewById(R.id.wordaddtype);
        spDate = (Spinner)super.findViewById(R.id.wordadddate);
        
        btnAdd.setOnClickListener(new WordAddOnClick());
//        wordBtn = (Button)super.findViewById(R.id.word);
//        wordBtn.setOnClickListener(new WordOnClick());
        initSpinner();
    }
	
	/**
	 * 初始化兩個下拉框
	 */
	private void initSpinner(){
		//1.初始化date下拉框
//		List<MyDate> dates = getDates();
		 //将可选内容与ArrayAdapter连接起来  
		ArrayAdapter<MyDate> aa = new MyDateArrayAdapter(WordAdd.this,  getDates());
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);  
          
        //设置下拉列表的风格  
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
          
        //将adapter 添加到spinner中  
        spDate.setAdapter(aa);  
          
        //添加事件Spinner事件监听    
//        spType.setOnItemSelectedListener(new SpinnerSelectedListener());  
          
        //设置默认值  
//        spType.setVisibility(View.VISIBLE); 
        
        //2.初始化類型下拉框
        List<MyType> types = getTypes();
        String[]m = new String[types.size()];
        for(int i=0;i<types.size();i++){
        	m[i] = types.get(i).getTypename();
        }
        ArrayAdapter<String> adtType = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);  
        
        //设置下拉列表的风格  
        adtType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
          
        //将adapter 添加到spinner中  
        spType.setAdapter(adtType);  
	}
	
	private class WordAddOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
//			Intent it = new Intent(WordAdd.this,WordMenu.class);
//			it.putExtra("info","单词学习");
//			WordAdd.this.startActivity(it);
			if(validate()){
				addMyWord();
				Toast.makeText(WordAdd.this, "*****添加成功*****",Toast.LENGTH_LONG).show();
				Intent it = new Intent(WordAdd.this,WordManage.class);
				WordAdd.this.startActivity(it);
			}
		}
	}
	
	/**
	 * 非空驗證
	 * @return
	 */
	private boolean validate(){
		if(etJpword.getText().toString().trim().equals("")){
			Toast.makeText(WordAdd.this, "*****平仮名不能為空*****",Toast.LENGTH_LONG).show();
			return false;
		}else if(etJpch.getText().toString().trim().equals("")){
			Toast.makeText(WordAdd.this, "*****漢字不能為空*****",Toast.LENGTH_LONG).show();
			return false;
		}else if(etChword.getText().toString().trim().equals("")){
			Toast.makeText(WordAdd.this, "*****中文不能為空*****",Toast.LENGTH_LONG).show();
			return false;
		}else{
			return true;
		}
	}
	
	private void addMyWord(){
		String jpword = etJpword.getText().toString().trim();
		String jpch = etJpch.getText().toString().trim();
		String chword = etChword.getText().toString().trim();
		String speech = etSpeech.getText().toString().trim();
		String date = ((MyDate)spDate.getSelectedItem()).getDate();
		String type = spType.getSelectedItem().toString();
		String desc = etDesc.getText().toString().trim();
		String str= "INSERT INTO myword(jpword,jpch,chword,speech,dateid,typeid,desc) VALUES ('%s','%s','%s','%s',(SELECT id FROM mydate WHERE mydate.date='%s'),(SELECT id FROM mytype WHERE mytype.typename='%s'),'%s');";
		String sql = String.format(str,jpword,jpch,chword,speech,date,type,desc);
		System.out.println(sql);
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordAdd.this);
		SQLiteDatabase db = database.getReadableDatabase();
//		db = database.getReadableDatabase();
		db.execSQL(sql);
//		return sql;
	}
	
    /**
     * 获取所有日期
     * @return
     */
    public List<MyDate> getDates(){
    	List<MyDate> list = new ArrayList<MyDate>();
    	SQLiteOpenHelper database = new com.db.DataBaseHelper(WordAdd.this);
		SQLiteDatabase db = null;
		db = database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM mydate", null);
		while (c.moveToNext()) {
			MyDate w = new MyDate();
			w.setId(c.getInt(c.getColumnIndex("id")));
			w.setDate(c.getString(c.getColumnIndex("date")));
			w.setDesc(c.getString(c.getColumnIndex("desc")));
			list.add(w);
		}
		return list;
    }
    
    /**
     * 获取所有類型
     * @return
     */
    public List<MyType> getTypes(){
    	List<MyType> list = new ArrayList<MyType>();
    	SQLiteOpenHelper database = new com.db.DataBaseHelper(WordAdd.this);
		SQLiteDatabase db = null;
		db = database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM mytype", null);
		while (c.moveToNext()) {
			MyType w = new MyType();
			w.setId(c.getInt(c.getColumnIndex("id")));
			w.setTypename(c.getString(c.getColumnIndex("typename")));
			w.setDesc(c.getString(c.getColumnIndex("desc")));
			list.add(w);
		}
		return list;
    }
}
