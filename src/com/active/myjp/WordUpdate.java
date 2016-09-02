package com.active.myjp;

import java.util.ArrayList;
import java.util.List;

import com.active.myjp.R;
import com.control.MyDateArrayAdapter;
import com.model.MyDate;
import com.model.MyType;
import com.model.MyWord;

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

public class WordUpdate extends Activity {
	
	private Button btnAdd = null;       //添加单词按钮
	private EditText etJpword;
	private EditText etJpch;
	private EditText etChword;
	private EditText etSpeech;
	private EditText etDesc;
	private Spinner spType;
	private Spinner spDate;
	
	private int wordId;        //传递过来的单词ID
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordadd);
        Intent it = super.getIntent();
        wordId = it.getIntExtra("wordId",0);
        btnAdd = (Button)super.findViewById(R.id.wordaddbtnadd);
        etJpword = (EditText)super.findViewById(R.id.wordaddjpword);
        etJpch = (EditText)super.findViewById(R.id.wordaddjpch);
        etChword = (EditText)super.findViewById(R.id.wordaddchword);
        etSpeech = (EditText)super.findViewById(R.id.wordaddspeech);
        etDesc = (EditText)super.findViewById(R.id.wordadddesc);
        spType = (Spinner)super.findViewById(R.id.wordaddtype);
        spDate = (Spinner)super.findViewById(R.id.wordadddate);
        
        btnAdd.setText("修改");
        btnAdd.setOnClickListener(new WordAddOnClick());
//        wordBtn = (Button)super.findViewById(R.id.word);
//        wordBtn.setOnClickListener(new WordOnClick());
        init();
    }
	
	private void init(){
		MyWord word = getWordById(wordId);
		//1.加载日期下拉框和类型下拉框的值
		initSpinner();
		//2.加载编辑框的值
		initEditText(word);
		//3.初始化下拉框的选项
		initSpinnerPosition(word);
	}
	
	private void initEditText(MyWord word){
		etJpword.setText(word.getJpword());
		etJpch.setText(word.getJpch());
		etChword.setText(word.getChword());
		etSpeech.setText(word.getSpeech());
		etDesc.setText(word.getDesc());
	}
	
	/**
	 * 初始化下拉框的选项
	 */
	private void initSpinnerPosition(MyWord word){
		for(int i=0;i<spDate.getAdapter().getCount();i++){
			MyDate ic = (MyDate)spDate.getAdapter().getItem(i);
			if(ic.getId()==word.getDateid()){
				spDate.setSelection(i);
			}
		}
		for(int i=0;i<spType.getAdapter().getCount();i++){
			String typename = spType.getAdapter().getItem(i).toString();
			if(typename.equals(getTypeById(word.getType()))){
				spType.setSelection(i);
			}
		}
	}
	
	/**
	 * 初始化兩個下拉框
	 */
	private void initSpinner(){
		//1.初始化date下拉框
		ArrayAdapter<MyDate> aa = new MyDateArrayAdapter(WordUpdate.this,  getDates());
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
        spDate.setAdapter(aa);  
        
        //2.初始化類型下拉框
        List<MyType> types = getTypes();
        String[]m = new String[types.size()];
        for(int i=0;i<types.size();i++){
        	m[i] = types.get(i).getTypename();
        }
        ArrayAdapter<String> adtType = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);  
        adtType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
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
				upMyWord();
				Toast.makeText(WordUpdate.this, "*****修改成功*****",Toast.LENGTH_LONG).show();
				Intent it = new Intent(WordUpdate.this,WordManage.class);
				WordUpdate.this.startActivity(it);
			}
		}
	}
	
	/**
	 * 非空驗證
	 * @return
	 */
	private boolean validate(){
		if(etJpword.getText().toString().trim().equals("")){
			Toast.makeText(WordUpdate.this, "*****平仮名不能為空*****",Toast.LENGTH_LONG).show();
			return false;
		}else if(etJpch.getText().toString().trim().equals("")){
			Toast.makeText(WordUpdate.this, "*****漢字不能為空*****",Toast.LENGTH_LONG).show();
			return false;
		}else if(etChword.getText().toString().trim().equals("")){
			Toast.makeText(WordUpdate.this, "*****中文不能為空*****",Toast.LENGTH_LONG).show();
			return false;
		}else{
			return true;
		}
	}
	
	private void upMyWord(){
		String jpword = etJpword.getText().toString().trim();
		String jpch = etJpch.getText().toString().trim();
		String chword = etChword.getText().toString().trim();
		String speech = etSpeech.getText().toString().trim();
		MyDate date = (MyDate)spDate.getSelectedItem();
		String type = spType.getSelectedItem().toString();
		String desc = etDesc.getText().toString().trim();
		
		String str= "update myword set jpword='%s',jpch='%s',chword='%s',speech='%s',dateid=%s,typeid=(SELECT id FROM mytype WHERE mytype.typename='%s'),desc='%s' where id=%s";
		String sql = String.format(str,jpword,jpch,chword,speech,date.getId(),type,desc,wordId);
		System.out.println(sql);
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordUpdate.this);
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
    	SQLiteOpenHelper database = new com.db.DataBaseHelper(WordUpdate.this);
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
    	SQLiteOpenHelper database = new com.db.DataBaseHelper(WordUpdate.this);
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
    
    /**
     * 根据类型ID，获取类型名称
     * @param id
     * @return
     */
    private String getTypeById(int id){
    	SQLiteOpenHelper database = new com.db.DataBaseHelper(WordUpdate.this);
		SQLiteDatabase db = null;
		db = database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM mytype where id="+id, null);
		while (c.moveToNext()) {
			return c.getString(c.getColumnIndex("typename"));
		}
		return "";
    }
    
    /**
     * 根据id查询word
     * @param wordid
     * @return
     */
	public MyWord getWordById(int wordid) {
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordUpdate.this);
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
