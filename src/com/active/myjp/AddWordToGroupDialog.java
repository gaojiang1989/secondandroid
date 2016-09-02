package com.active.myjp;

import java.util.ArrayList;
import java.util.List;

import com.active.myjp.R;
import com.db.DataBaseHelper;
import com.model.Group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class AddWordToGroupDialog {
	private Context ctx;
	SQLiteDatabase db = null;
	private int wordId;
	
	public AddWordToGroupDialog(Context c,int wid){
		this.ctx = c;
		this.wordId = wid;
		SQLiteOpenHelper database = new DataBaseHelper(ctx);
		db = database.getReadableDatabase();
	}
	public void showGroupDialog() {
		LayoutInflater factory = LayoutInflater.from(ctx);
		
		final View myView = factory.inflate(R.layout.add_word_group, null);
		final Spinner sp = (Spinner) myView.findViewById(R.id.sp_word_group);
		sp.setAdapter(getGroupAdapter());

		Dialog dialog = new AlertDialog.Builder(ctx)
				.setTitle("添加到分组")
				.setView(myView)
				.setPositiveButton("添加", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Group g = (Group) sp.getSelectedItem();
						if (isWordInGroup(g.getId())) {
							Toast.makeText(ctx, "添加失败，该单词已存在该词组中！",
									Toast.LENGTH_LONG).show();
						} else {
							addGroupWord(g.getId());
							// System.out.println("选择的值是id："+g.getId()+"==name:"+g.getGroupName());
							Toast.makeText(ctx, "*****添加完成*****",
									Toast.LENGTH_LONG).show();
						}

					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				}).create();
		dialog.show();
	}
	
	private ArrayAdapter<Group> getGroupAdapter() {
		List<Group> list = new ArrayList<Group>();

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

		
		ArrayAdapter<Group> aa = new ArrayAdapter<Group>(ctx,android.R.layout.simple_spinner_item, list);
		return aa;
	}
	
	private boolean isWordInGroup(int groupId) {
		String sql = "select * from gj_groupword where groupid=" + groupId
				+ " and wordid=" + wordId;
		Cursor c = db.rawQuery(sql, null);
		if (c.getCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 添加一个单词到组中
	 * 
	 * @param groupId
	 */
	private void addGroupWord(int groupId) {
		String sql = "insert into gj_groupword(groupid,wordid) values("
				+ groupId + "," + wordId + ");";
		db.execSQL(sql);
	}
}
