package com.active.myjp;

import java.util.ArrayList;
import java.util.List;

import com.control.MyDateArrayAdapter;
import com.model.MyDate;
import com.model.MyWord;
import com.tool.CommonTool;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class WordManage extends Activity {
    
	TableLayout layout;
	Spinner sp ;
	SQLiteDatabase db = null;
//	private int wordId;
//	private int lesId;
	
//	private boolean isChToJp = true;
//	private int isChToJp = 1;
//	private boolean isRandom = false;

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);

		ScrollView sv = new ScrollView(this);

		layout = new TableLayout(this);
		TableLayout.LayoutParams layoutParam = new TableLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		createSpinner();
		
		initSpinnerPosition();

		sv.addView(layout);
		super.setContentView(sv, layoutParam);
	}
	
	/**
	 * 初始化下拉框的选项
	 */
	private void initSpinnerPosition(){
		int lessonId = CommonTool.getCurLesson(this,"wordLessonId");
		for(int i=0;i<sp.getAdapter().getCount();i++){
			MyDate ic = (MyDate)sp.getAdapter().getItem(i);
			if(ic.getId()==lessonId){
				sp.setSelection(i);
			}
		}
	}

	/**
	 * 创建课程选择的下拉框
	 */
	private void createSpinner() {
		LinearLayout line = new LinearLayout(this);
//		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		line.setOrientation(LinearLayout.HORIZONTAL);
		
		sp = new Spinner(this);
		sp.setPrompt("300");

		List<MyDate> list = getDates();
//		ArrayAdapter<ItemClass> aa = new ArrayAdapter<ItemClass>(this,
//				android.R.layout.simple_spinner_item, list);
		
		ArrayAdapter<MyDate> aa = new MyDateArrayAdapter(WordManage.this, list);
		
		
		sp.setAdapter(aa);
		sp.setOnItemSelectedListener(new OnClassSelect());
		line.addView(sp);
		
		Button btnAdd = new Button(this);
		btnAdd.setText("添加单词");
		btnAdd.setOnClickListener(new OnBtnAdd());
		line.addView(btnAdd);
		
		Button btnBack = new Button(this);
		btnBack.setText("返回");
		btnBack.setOnClickListener(new OnBtnBack());
		line.addView(btnBack);
		
//		Button btnSeq = new Button(this);
//		if(isRandom)
//			btnSeq.setText("顺序");
//		else
//			btnSeq.setText("随机");
//		btnSeq.setOnClickListener(new RandomOnClick());
//		line.addView(btnSeq);
		
		
		layout.addView(line);
	}
	
	public void onContextMenuClosed(Menu menu){
		curTv.setBackgroundColor(Color.WHITE);
	}

	private class OnClassSelect implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long id) {

			
			MyDate value = (MyDate) arg0.getItemAtPosition(position);
//			System.out.println();
			int lessonId = value.getId();
//			lesId = lessonId;
			CommonTool.setCurLesson(WordManage.this, lessonId,"wordLessonId");
			initWord(lessonId);
		}



		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}
	
	/**
	 * 初始化所有单词
	 * @param lessonId
	 */
	private void initWord(int dateid) {
		int count = layout.getChildCount();
		for (int i = 0; i < count - 1; i++) {
			layout.removeView(layout.getChildAt(1));
		}

		List<MyWord> list = getWordsByDate(dateid);
		for (int x = 0; x < list.size(); x++) {
			TableRow row = new TableRow(WordManage.this);
			// 创建显示日语单词的文本组件
			TextView tv = new TextView(WordManage.this);
//			if(isChToJp==1){
//				tv.setText(list.get(x).getJpword());
//			}else if(isChToJp==2){
//				tv.setText(list.get(x).getChword());
//			}else if(isChToJp==3){
//				tv.setText(list.get(x).getJpch());
//			}
			if(list.get(x).getJpword().equals(list.get(x).getJpch())){
				tv.setText(list.get(x).getJpch());
			}else{
				tv.setText(list.get(x).getJpch()+"("+list.get(x).getJpword()+")");
			}
			
			tv.setWidth(layout.getWidth()*2 / 3);
			tv.setTag(list.get(x).getId());
			tv.setPadding(10,10,0,10);
			tv.setGravity(Gravity.CENTER_VERTICAL);
//			WordManage.super.registerForContextMenu(tv);
			row.addView(tv, 0);
			// 创建显示按钮
//			Button bw = new Button(WordManage.this);
//			bw.setTag(list.get(x).getId());
//			bw.setText("查看");
//			bw.setWidth(layout.getWidth() * 2 / 3);
//			bw.setOnClickListener(new OnBtnSearch());
//			row.addView(bw, 1);
			
			Button btnUpdate = new Button(WordManage.this);
			btnUpdate.setTag(list.get(x).getId());
			btnUpdate.setText("修改");
//			btnUpdate.setWidth(layout.getWidth() * 2 / 3);
			btnUpdate.setOnClickListener(new OnBtnUpdate());
			row.addView(btnUpdate, 1);
			
			Button btnDel = new Button(WordManage.this);
			btnDel.setTag(list.get(x).getId());
			btnDel.setText("删除");
//			bw.setWidth(layout.getWidth() * 2 / 3);
			btnDel.setOnClickListener(new OnBtnDel());
			row.addView(btnDel, 2);
			layout.addView(row);
		}
	}
	
	TextView curTv;
	
	private class OnBtnBack implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
//			int tag = (Integer) arg0.getTag();
//			MyWord w = getWordById(tag);
			Intent it = new Intent(WordManage.this,WordMenu.class);
//			it.putExtra("info","单词学习");
			WordManage.this.startActivity(it);
			
		}

	}
	private class OnBtnAdd implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
//			int tag = (Integer) arg0.getTag();
//			MyWord w = getWordById(tag);
			Intent it = new Intent(WordManage.this,WordAdd.class);
			it.putExtra("info","单词学习");
			WordManage.this.startActivity(it);
			
		}

	}

	private class OnBtnUpdate implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int tag = (Integer) arg0.getTag();
			Intent it = new Intent(WordManage.this,WordUpdate.class);
			it.putExtra("wordId",tag);
			WordManage.this.startActivity(it);
//			MyWord w = getWordById(tag);
			
		}

	}
	
	private class OnBtnDel implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			final int tag = (Integer) arg0.getTag();
			new AlertDialog.Builder(WordManage.this).  
            setIcon(R.drawable.ic_launcher).//图标  
            setTitle("删除?").//标题  
            setMessage("您确认要删除句子吗？").//提示内容  
            setPositiveButton("确认", new DialogInterface.OnClickListener() {//确定  
                @Override  
                public void onClick(DialogInterface arg0, int arg1) {  
                    //yes to do  
                	delWord(tag);
        			Toast.makeText(WordManage.this, "*****删除成功*****",Toast.LENGTH_LONG).show();
        			initWord(CommonTool.getCurLesson(WordManage.this,"wordLessonId"));
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
	

	private void delWord(int id){
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordManage.this);
		SQLiteDatabase db = database.getReadableDatabase();
		String sql="delete from myword where id="+id;
		db.execSQL(sql);
	}
    
    /**
     * 获取所有日期
     * @return
     */
    public List<MyDate> getDates(){
    	List<MyDate> list = new ArrayList<MyDate>();
    	SQLiteOpenHelper database = new com.db.DataBaseHelper(WordManage.this);
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
     * 获取所有单词
     * @return
     */
    public List<MyWord> getWords(){
    	List<MyWord> list = new ArrayList<MyWord>();
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordManage.this);
		SQLiteDatabase db = null;
		db = database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM myword", null);
		
		while (c.moveToNext()) {
			MyWord w = setWord(c);
			list.add(w);
		}
		c.close();
    	return list;
    }
    
    public List<MyWord> getWordsByDate(int dateid){
    	List<MyWord> list = new ArrayList<MyWord>();
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordManage.this);
		SQLiteDatabase db = null;
		db = database.getReadableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM myword where dateid="+dateid, null);
		
		while (c.moveToNext()) {
			MyWord w = setWord(c);
			list.add(w);
		}
		c.close();
    	return list;
    }
    
    /**
     * 根据id查询word
     * @param wordid
     * @return
     */
	public MyWord getWordById(int wordid) {
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordManage.this);
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
