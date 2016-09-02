package com.active.myjp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.db.DataBaseHelper;
import com.model.MyWord;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
//import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GroupWord extends Activity {
	TableLayout layout;
	SQLiteDatabase db = null;
	private int groupId;
	private int isChToJp = 1;
	private int wordId;
	public void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		
		Intent it = super.getIntent();
		groupId = it.getIntExtra("groupId",0);
		
		SQLiteOpenHelper database = new DataBaseHelper(GroupWord.this);
		db = database.getReadableDatabase();
		
		ScrollView sv = new ScrollView(this);
		
		
		layout = new TableLayout(this);
		TableLayout.LayoutParams layoutParam = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		
		initButton();
		
		initWord();
		
		
		sv.addView(layout);
		super.setContentView(sv, layoutParam);
	}
	
	private void initWord(){
		//清除原有的单词
		int count = layout.getChildCount();
		for(int i=0;i<count-1;i++){
			layout.removeView(layout.getChildAt(1));
		}
		//开始绘制
		List<MyWord> list = getWordByGroupId();
//		List<Word> list = tbhandle.getWordByGroupId(groupId);
		System.out.println("查询到数据"+list.size());
		for(MyWord w:list){
			TableRow row = new TableRow(this);
			TextView tvWord = new TextView(this);
			tvWord.setTextSize(18);
			tvWord.setTag(w);
			tvWord.setPadding(10,10,0,10);
			tvWord.setWidth(layout.getWidth() / 3);
			tvWord.setGravity(Gravity.CENTER_VERTICAL);
			super.registerForContextMenu(tvWord);
			
			row.addView(tvWord,0);
//			tvWord.setWidth(layout.getWidth()/2);
			Button btnWord = new Button(this);
			btnWord.setText("查看");
			btnWord.setWidth(layout.getWidth() * 2 / 3);
//			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//			btnWord.setLayoutParams(param);
//			btnWord.setWidth(layout.getWidth()/2);
			btnWord.setTag(w);
			btnWord.setOnClickListener(new SearchOnClick());
			if(isChToJp==1){
				tvWord.setText(w.getChword());
			}else if(isChToJp==2){
				tvWord.setText(w.getJpword());
			}else if(isChToJp==3){
				tvWord.setText(w.getJpch());
			}
			
			row.addView(btnWord,1);
			layout.addView(row);
		}
	}
	
	private class SearchOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			MyWord w = (MyWord) arg0.getTag();
			Button btnSearch = (Button)arg0;
//			Word w = getWordByid(id);
			if(btnSearch.getText().equals("查看")){
				if(isChToJp==1){
					if(w.getJpword().equals(w.getJpch())||w.getJpch().equals(w.getChword())){
						btnSearch.setText(w.getJpword());
					}else{
						String msg = w.getJpword()+"（"+w.getJpch()+"）";
//						if(msg.length()>7){
//							btnSearch.setHeight(btnSearch.getHeight()+30);
//							msg  =  w.getJpword()+"\n（"+w.getJpch()+"）";
//						}
						btnSearch.setText(msg);
					}
					
				}else if(isChToJp==2){
					if(w.getJpword().equals(w.getJpch())||w.getJpch().equals(w.getChword())){
						btnSearch.setText(w.getChword());
					}else{
//						btnSearch.setText(w.getChword()+"("+w.getJpch()+")");
						String msg = w.getChword()+"（"+w.getJpch()+"）";
//						if(msg.length()>7){
//							btnSearch.setHeight(90);
//							msg = w.getChword()+"\n（"+w.getJpch()+"）";
//						}
						btnSearch.setText(msg);
					}
				}else if(isChToJp==3){
					if(w.getJpword().equals(w.getChword())||w.getJpch().equals(w.getChword())){
						btnSearch.setText(w.getJpword());
					}else{
//						btnSearch.setText(w.getChword()+"("+w.getJpch()+")");
						String msg = w.getJpword()+"（"+w.getChword()+"）";
//						if(msg.length()>7){
//							btnSearch.setHeight(90);
//							msg = w.getJpword()+"\n（"+w.getChword()+"）";
//						}
						btnSearch.setText(msg);
					}
				}
			}else{
				btnSearch.setText("查看");
			}

			
		}
		
	}
	
	/**
	 * 根据组ID，查询出该组所有的单词
	 * @return
	 */
	private List<MyWord> getWordByGroupId(){
		List<MyWord> list = new ArrayList<MyWord>();
		String sql = "select * from myword where id IN (SELECT wordid from gj_groupword where groupid="+groupId+");";
		Cursor c = db.rawQuery(sql, null);
		Set<MyWord> set = new HashSet<MyWord>();
		while(c.moveToNext()){
			int id = c.getInt(c.getColumnIndex("id"));  
            String jpword = c.getString(c.getColumnIndex("jpword"));  
            String chword = c.getString(c.getColumnIndex("chword")); 
            String jpch = c.getString(c.getColumnIndex("jpch")); 
            int dateid = c.getInt(c.getColumnIndex("dateid"));  
            int typeid = c.getInt(c.getColumnIndex("typeid"));    
            String speech = c.getString(c.getColumnIndex("speech"));
            String desc = c.getString(c.getColumnIndex("desc"));
			MyWord w = new MyWord(id, jpword, chword, jpch, speech, dateid,typeid,desc);
            set.add(w);
		}
		list = new ArrayList<MyWord>(set);
		return list;
	}
	
	/**
	 * 初始化中译日和日译中的按钮
	 */
	private void initButton(){
		LinearLayout line = new LinearLayout(this);
//		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		line.setOrientation(LinearLayout.HORIZONTAL);
//		TableRow row = new TableRow(this);
		Button btnChToJp = new Button(this);
//		System.out.println("layout宽度："+getScreenWidth());
		btnChToJp.setText("中译日");
		//btnChToJp.setWidth(getScreenWidth()/2-10);
		btnChToJp.setOnClickListener(new ChangeOnClick());
//		row.addView(btnChToJp);
		line.addView(btnChToJp);
		Button btnJpToCh = new Button(this);
		btnJpToCh.setText("日译中");
		btnJpToCh.setOnClickListener(new ChangeOnClick());
		//btnJpToCh.setWidth(getScreenWidth()/2-10);
//		row.addView(btnJpToCh);
		line.addView(btnJpToCh);
		
		Button btnKanToJp = new Button(this);
		btnKanToJp.setText("汉子译日");
		btnKanToJp.setOnClickListener(new ChangeOnClick());
		//btnJpToCh.setWidth(getScreenWidth()/2-10);
//		row.addView(btnJpToCh);
		line.addView(btnKanToJp);
		
//		layout.addView(row);
		layout.addView(line);
	}
	
//	private int getScreenWidth(){
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		return dm.widthPixels;
//	}
	
	/**
	 * 点击切换中译日和日译中的按钮时触发此事件
	 * @author epri
	 *
	 */
	private class ChangeOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Button btnChange = (Button)arg0;
			if(btnChange.getText().equals("中译日")){
				isChToJp = 1;
				initWord();
			}else if(btnChange.getText().equals("日译中")){
				isChToJp = 2;
				initWord();
			}else if(btnChange.getText().equals("汉子译日")){
				isChToJp = 3;
				initWord();
			}
		}
		
	}
	
	public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
//		String tag = v.getTag().toString();
//		wordId = (Integer)v.getTag();
		MyWord w = (MyWord)v.getTag();
		wordId = w.getId();
		System.out.println("单词ID："+w.getId());
//		System.out.println("tag:"+tag);
		menu.setHeaderTitle("信息操作");
		menu.add(Menu.NONE,Menu.FIRST+1,11,"删除单词");
		menu.add(Menu.NONE,Menu.FIRST+2,22,"详细信息");
//		menu.add(Menu.NONE,Menu.FIRST+2,22,"添加单词组");
//		menu.add(Menu.NONE,Menu.FIRST+3,33,"删除单词组");
	}
	
	public boolean onContextItemSelected(MenuItem item){
//		ContextMenu.ContextMenuInfo cmi = item.getMenuInfo();
		
		switch(item.getItemId()){
		case Menu.FIRST+1:
//			Toast.makeText(this, "您选择的是添加单词组"+item.getGroupId(),Toast.LENGTH_LONG).show();
//			showGroupDialog();
			
			delDialog();
			
		break;
		case Menu.FIRST+2:
			Intent it = new Intent(GroupWord.this,WordDetail.class);
			it.putExtra("wordId",wordId);
			GroupWord.this.startActivity(it);
		break;
		}
		return false;
	}
	
	private void delWordGroup(){
		String sql = "delete from gj_groupword where wordid="+wordId+" and groupid="+groupId;
		db.execSQL(sql);
	}
	
	private void delDialog(){
		Dialog dialog = new AlertDialog.Builder(GroupWord.this).setTitle("确定删除？").setMessage("您确定要删除单词吗？").setPositiveButton("确定",new DialogInterface.OnClickListener() {
			
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				delWordGroup();
				initWord();
				Toast.makeText(GroupWord.this, "*****删除完成*****",Toast.LENGTH_LONG).show();
			}
		}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		}).create();
		dialog.show();
	}
}
