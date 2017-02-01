package com.tool;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class CommonTool {

	/**
	 * 当前的软件系统版本
	 */
	public static String curVersion = "1.0";
	/**
	 * 返回当前选择的是初级还是中级，初级是1，中级是2
	 * @param ctx
	 * @return
	 */
	public static int getTextbook(Context ctx){
		SharedPreferences share = ctx.getSharedPreferences("gj_rmm",Activity.MODE_PRIVATE);
    	return share.getInt("textbook",0);
	}
	
	public static int getCurLesson(Context ctx,String type){
		SharedPreferences share = ctx.getSharedPreferences("gj_rmm",Activity.MODE_PRIVATE);
    	return share.getInt(type,1);
	}
	
	public static void setCurLesson(Context ctx,int lessonId,String type){
		SharedPreferences share = ctx.getSharedPreferences("gj_rmm",Activity.MODE_PRIVATE);
		SharedPreferences.Editor edit = share.edit();
		edit.putInt(type,lessonId);
		edit.commit();
	}
}
