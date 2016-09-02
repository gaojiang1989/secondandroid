package com.active.myjp;

import java.util.ArrayList;
import java.util.List;

import com.active.myjp.R;
import com.db.DataBaseHelper;
import com.model.Group;
import com.tool.FileSdcard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class GroupMenu extends Activity {

	TableLayout layout;
	
	private SQLiteOpenHelper database = null;
	private SQLiteDatabase db = null;
	private int groupId;

	public void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		
		database = new DataBaseHelper(GroupMenu.this);
		db = database.getReadableDatabase();
		
		ScrollView sv = new ScrollView(this);
		
		
		layout = new TableLayout(this);
		
		Button btnAddGroup = new Button(this);
		btnAddGroup.setText("添加分组");
		btnAddGroup.setOnClickListener(new AddGroupOnClick());
		layout.addView(btnAddGroup);
		
		initLayout();
		
		TableLayout.LayoutParams layoutParam = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		
		

		
		
		sv.addView(layout);
		super.setContentView(sv, layoutParam);
	}
	
	private void initLayout(){
		int count = layout.getChildCount();
		for(int i=0;i<count;i++){
			layout.removeView(layout.getChildAt(1));
		}
		List<Group> list = this.getAllGroup();
		for(Group g:list){
			TableRow row = new TableRow(this);
			
			TextView tv = new TextView(this);
			tv.setText(g.getGroupName());
			tv.setTextSize(18);
			tv.setPadding(10,10,10,10);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			
			tv.setTag(g.getId());
			super.registerForContextMenu(tv);
			row.addView(tv);
			layout.addView(row);
		}
	}
	TextView currentTv;
	
	public void onContextMenuClosed(Menu menu){
//		Toast.makeText(this,"上下文菜单关闭了",Toast.LENGTH_LONG).show();
		currentTv.setBackgroundColor(Color.WHITE);
	}
	
	public void onCreateContextMenu(ContextMenu menu,View v,ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		currentTv = (TextView)v;
		currentTv.setBackgroundColor(Color.GRAY);
		int tag = (Integer)v.getTag();
		groupId = tag;
//		System.out.println("tag:"+tag);
		menu.setHeaderTitle("信息操作");
		menu.add(Menu.NONE,Menu.FIRST+1,tag,"进入单词组");
		menu.add(Menu.NONE,Menu.FIRST+2,tag,"添加单词组");
		menu.add(Menu.NONE,Menu.FIRST+3,tag,"修改单词组");
		menu.add(Menu.NONE,Menu.FIRST+4,tag,"删除单词组");
		
	}
	
	public boolean onContextItemSelected(MenuItem item){
//		ContextMenu.ContextMenuInfo cmi = item.getMenuInfo();
//		System.out.println("单词组ID："+groupId);
		switch(item.getItemId()){
		case Menu.FIRST+1:
//			Toast.makeText(this, "您选择的是添加单词组"+item.getGroupId(),Toast.LENGTH_LONG).show();
			Intent it = new Intent(GroupMenu.this,GroupWord.class);
			it.putExtra("groupId",groupId);
			GroupMenu.this.startActivity(it);
		break;
		case Menu.FIRST+2:
			addGroupView();
		break;
		case Menu.FIRST+3:
			upGroupView();
		break;
		case Menu.FIRST+4:
			delDialog();
		break;
		}
		
		return false;
	}
	
	private class AddGroupOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			addGroupView();
		}
		
	}
	private void upGroupView(){
		LayoutInflater factory = LayoutInflater.from(GroupMenu.this);
		final View myView = factory.inflate(R.layout.add_group,null);
		final EditText etGroup = (EditText)myView.findViewById(R.id.edtAddGroup);
		final EditText etDesc = (EditText)myView.findViewById(R.id.edtAddDesc);
		Group g = getGroupById();
		etGroup.setText(g.getGroupName());
		etDesc.setText(g.getGroupDesc());
		Dialog dialog = new AlertDialog.Builder(GroupMenu.this).setTitle("修改组信息").setView(myView).setPositiveButton("修改",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
				if(etGroup.getText().toString().trim().equals("")){
					Toast.makeText(GroupMenu.this, "修改失败，组名称不能为空",Toast.LENGTH_LONG).show();
				}else{
					if(isGroupExistsOnUp(etGroup.getText().toString())){
						Toast.makeText(GroupMenu.this, "修改失败，“"+etGroup.getText().toString()+"”组已存在！",Toast.LENGTH_LONG).show();
					}else{
						upGroupById(etGroup.getText().toString(),etDesc.getText().toString());
						Toast.makeText(GroupMenu.this, "修改成功",Toast.LENGTH_LONG).show();
						initLayout();
					}
					
				}
			}
		}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		}).create();
		dialog.show();
	}
	
	/**
	 * 添加组信息
	 */
	private void addGroupView(){
		LayoutInflater factory = LayoutInflater.from(GroupMenu.this);
		final View myView = factory.inflate(R.layout.add_group,null);
		
		Dialog dialog = new AlertDialog.Builder(GroupMenu.this).setTitle("添加").setView(myView).setPositiveButton("添加",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				final EditText etGroup = (EditText)myView.findViewById(R.id.edtAddGroup);
				final EditText etDesc = (EditText)myView.findViewById(R.id.edtAddDesc);
				
				if(etGroup.getText().toString().trim().equals("")){
					Toast.makeText(GroupMenu.this, "添加失败，组名称不能为空",Toast.LENGTH_LONG).show();
				}else{
					if(isGroupExists(etGroup.getText().toString())){
						Toast.makeText(GroupMenu.this, "添加失败，“"+etGroup.getText().toString()+"”组已存在！",Toast.LENGTH_LONG).show();
					}else{
						addGroupData(etGroup.getText().toString(),etDesc.getText().toString());
						Toast.makeText(GroupMenu.this, "添加成功",Toast.LENGTH_LONG).show();
						initLayout();
					}
					
				}
			}
		}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		}).create();
		dialog.show();
	}
	
	/**
	 * 删除单词组提示对话框
	 */
	private void delDialog(){
		Dialog dialog = new AlertDialog.Builder(GroupMenu.this).setTitle("确定删除？").setMessage("您确定要删除单词组吗？").setPositiveButton("确定",new DialogInterface.OnClickListener() {
			
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				delGroup(groupId);
				initLayout();
				Toast.makeText(GroupMenu.this, "*****删除成功*****",Toast.LENGTH_LONG).show();
			}
		}).setNegativeButton("取消",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		}).create();
		dialog.show();
	}
	
	/**
	 * 根据组ID，修改组信息
	 * @param groupName
	 * @param groupDesc
	 */
	private void upGroupById(String groupName, String groupDesc) {
		String sql = "update gj_group set groupname='" + groupName
				+ "',groupdesc='" + groupDesc + "' where id=" + groupId;
		db.execSQL(sql);
	}
	
	/**
	 * 根据组ID，获取组信息
	 * @return
	 */
	private Group getGroupById(){
		String sql = "select * from gj_group where id="+groupId;
		Cursor c = db.rawQuery(sql, null);
		Group g = null;
		while(c.moveToNext()){
			int id = c.getInt(c.getColumnIndex("id"));
			String groupName = c.getString(c.getColumnIndex("groupname"));
			int isVisible = c.getInt(c.getColumnIndex("isvisible"));
			String desc = c.getString(c.getColumnIndex("groupdesc"));
			g = new Group(id, groupName, isVisible, desc);
		}
		return g;
	}
	
	/**
	 * 删除分组记录及其单词分组记录表该分组ID对应的所有记录
	 * @param groupid
	 */
	private void delGroup(int groupid){
		String sql = "delete from gj_groupword where groupid="+groupid;
		db.execSQL(sql);
		sql = "delete from gj_group where id="+groupId;
		db.execSQL(sql);
	}
	
	/**
	 * 插入一条单词分组记录
	 * @param groupName
	 * @param desc
	 */
	private void addGroupData(String groupName,String desc){
		String sql = "insert into gj_group(groupname,isvisible,groupdesc) values('"+groupName+"',0,'"+desc+"');";
		
		this.db.execSQL(sql);
//		this.db.close();
	}
	
	private boolean isGroupExistsOnUp(String groupName){
		String sql = "select * from gj_group where groupname='"+groupName+"' and id<>"+groupId;
		System.out.println("sql:"+sql);
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToNext()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断组名称是否存在
	 * @param groupName
	 * @return
	 */
	private boolean isGroupExists(String groupName){
		List<Group> list = getAllGroup();
		boolean result = false;
		for(Group g:list){
			if(g.getGroupName().equals(groupName)){
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 获取所有的单词分组
	 * @return
	 */
	private List<Group> getAllGroup(){
		List<Group> list = new ArrayList<Group>();
		
		try{
			Cursor c = db.rawQuery("SELECT * FROM gj_group", null);

			while (c.moveToNext()) {

				int id = c.getInt(c.getColumnIndex("id"));
				String groupname = c.getString(c.getColumnIndex("groupname"));
				int isvisible = c.getInt(c.getColumnIndex("isvisible"));
				String groupdesc = c.getString(c.getColumnIndex("groupdesc"));
				Group ic = new Group(id, groupname, isvisible, groupdesc);
				list.add(ic);
			}
			c.close(); 
		}catch(Exception e){
			FileSdcard.log(e.getMessage());
		}
		
		return list;
	}
}
