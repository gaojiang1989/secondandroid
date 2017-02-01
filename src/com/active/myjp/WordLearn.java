package com.active.myjp;

import java.util.ArrayList;
import java.util.List;

import com.control.MyDateArrayAdapter;
import com.model.MyDate;
import com.model.MyWord;
import com.tool.CommonTool;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.AdapterView.OnItemSelectedListener;

public class WordLearn extends Activity {
    
	TableLayout layout;
	Spinner sp ;
	SQLiteDatabase db = null;
	private int wordId;
	private int lesId;
	
//	private boolean isChToJp = true;
	private int isChToJp = 1;
	private boolean isRandom = false;

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
		
		ArrayAdapter<MyDate> aa = new MyDateArrayAdapter(WordLearn.this, list);
		
		
		sp.setAdapter(aa);
		sp.setOnItemSelectedListener(new OnClassSelect());
		line.addView(sp);
		
		Button btnChange = new Button(this);
		if(isChToJp==1)
			btnChange.setText("日译中");
		else if(isChToJp==2)
			btnChange.setText("中译日");
		else if(isChToJp==3)
			btnChange.setText("汉字译日");
		btnChange.setOnClickListener(new JpToChOnClick());
		line.addView(btnChange);
		
		Button btnSeq = new Button(this);
		if(isRandom)
			btnSeq.setText("顺序");
		else
			btnSeq.setText("随机");
		btnSeq.setOnClickListener(new RandomOnClick());
		line.addView(btnSeq);
		
		
		layout.addView(line);
	}
	
	/**
	 * 点击日译中或者中译日按钮后改变当前学习模式
	 * @author epri
	 *
	 */
	private class JpToChOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Button btn = (Button)arg0;
			if(btn.getText().equals("日译中")){    //
				isChToJp = 2;
				btn.setText("中译日");
			}else if(btn.getText().equals("中译日")){
				isChToJp = 3;
				btn.setText("汉字译日");
			}else if(btn.getText().equals("汉字译日")){
				isChToJp = 1;
				btn.setText("日译中");
			}
			initWord(lesId);
		}
		
	}
	
	/**
	 * 点击随机或者顺序记忆的按钮后触发的事件
	 * @author epri
	 *
	 */
	private class RandomOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Button btn = (Button)arg0;
			if(btn.getText().equals("随机")){
				isRandom = true;
				btn.setText("顺序");
			}else{
				isRandom = false;
				btn.setText("随机");
			}
			initWord(lesId);
		}
		
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
			lesId = lessonId;
			CommonTool.setCurLesson(WordLearn.this, lessonId,"wordLessonId");
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
			TableRow row = new TableRow(WordLearn.this);
			row.setPadding(10,10,0,0);
			// 创建显示日语单词的文本组件
			TextView tv = new TextView(WordLearn.this);
			if(isChToJp==1){
				tv.setText(list.get(x).getJpword());
			}else if(isChToJp==2){
				tv.setText(list.get(x).getChword());
			}else if(isChToJp==3){
				tv.setText(list.get(x).getJpch());
			}
			
			tv.setWidth(layout.getWidth() / 3);
			tv.setTag(list.get(x).getId());
			tv.setPadding(10,10,0,10);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			WordLearn.super.registerForContextMenu(tv);
			row.addView(tv, 0);
			// 创建显示按钮
			Button bw = new Button(WordLearn.this);
			bw.setTag(list.get(x).getId());
			bw.setText("查看");
			bw.setWidth(layout.getWidth() * 2 / 3);
			bw.setHeight(bw.getHeight()-2);
			bw.setOnClickListener(new OnBtnSearch());
			bw.setBackgroundColor(Color.YELLOW);
			
//			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
//			lp.setMargins(10, 10, 10, 10);  
//			bw.setLayoutParams(lp);
			bw.setBottom(10);
			row.addView(bw, 1);
			layout.addView(row);
		}
		
		if(list.size()<5){
			Button btnAdd = new Button(WordLearn.this);
			btnAdd.setText("今日添加单词任务尚未完成，快来添加吧！！");
			btnAdd.setOnClickListener(new OnBtnAdd());
			layout.addView(btnAdd);
		}
	}
	
	TextView curTv;

	
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// String tag = v.getTag().toString();
		curTv = (TextView)v;
		wordId = (Integer) v.getTag();
		// System.out.println("tag:"+tag);
		menu.setHeaderTitle("信息操作");
		menu.add(Menu.NONE, Menu.FIRST + 1, 11, "添加到分组");
		menu.add(Menu.NONE, Menu.FIRST + 2, 22, "详细信息");
		curTv.setBackgroundColor(Color.GRAY);
		// menu.add(Menu.NONE,Menu.FIRST+2,22,"添加单词组");
		// menu.add(Menu.NONE,Menu.FIRST+3,33,"删除单词组");
	}

	public boolean onContextItemSelected(MenuItem item) {
		// ContextMenu.ContextMenuInfo cmi = item.getMenuInfo();
		AddWordToGroupDialog awt = new AddWordToGroupDialog(this,wordId);
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			// Toast.makeText(this,
			// "您选择的是添加单词组"+item.getGroupId(),Toast.LENGTH_LONG).show();
			awt.showGroupDialog();
			break;
		case Menu.FIRST + 2:
			Intent it = new Intent(WordLearn.this,WordDetail.class);
			it.putExtra("wordId",wordId);
			WordLearn.this.startActivity(it);
			break;
		}
		return false;
	}


	private class OnBtnAdd implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent it = new Intent(WordLearn.this,WordAdd.class);
			WordLearn.this.startActivity(it);

		}

	}

	private class OnBtnSearch implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int tag = (Integer) arg0.getTag();
			MyWord w = getWordById(tag);
			Button but = (Button) arg0;
			if(but.getText().equals("查看")){
				if(isChToJp==2){
					if (w.getChword().equals(w.getJpch())
							|| w.getJpch().equals(w.getJpword())) {
						but.setText(w.getJpword());
					} else {
						but.setText(w.getJpword() + "(" + w.getJpch() + ")");
					}
				}else if(isChToJp==1){
					if (w.getChword().equals(w.getJpch())
							|| w.getJpch().equals(w.getJpword())) {
						but.setText(w.getChword());
					} else {
						but.setText(w.getChword()+ "(" + w.getJpch() + ")");
					}
				}else if(isChToJp==3){
					but.setText(w.getJpword()+ "(" + w.getChword()+ ")");
				}
				but.setBackgroundColor(Color.WHITE);
			}else{
				but.setText("查看");
				but.setBackgroundColor(Color.YELLOW);
			}
			
			

		}

	}

    
    /**
     * 获取所有日期
     * @return
     */
    public List<MyDate> getDates(){
    	List<MyDate> list = new ArrayList<MyDate>();
    	SQLiteOpenHelper database = new com.db.DataBaseHelper(WordLearn.this);
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
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordLearn.this);
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
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordLearn.this);
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
		SQLiteOpenHelper database = new com.db.DataBaseHelper(WordLearn.this);
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
