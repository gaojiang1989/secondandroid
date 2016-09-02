package com.active.myjp;

import com.active.myjp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WordMenu extends Activity {
	
	private Button btnWordLearn = null;       //单词学习
	private Button btnGroupLearn = null;      //分组学习
	private Button btnWordManage = null;      //单词管理
	private Button btnTypeManage = null;      //类别管理
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordmenu);
        
        btnWordLearn = (Button)super.findViewById(R.id.wordlearn);
        btnWordLearn.setOnClickListener(new WordOnClick());
        
        btnGroupLearn = (Button)super.findViewById(R.id.grouplearn);
        btnGroupLearn.setOnClickListener(new GroupLearnOnClick());
        
        btnWordManage = (Button)super.findViewById(R.id.wordmanage);
        btnWordManage.setOnClickListener(new WordManageOnClick());
        
        btnTypeManage = (Button)super.findViewById(R.id.typemanage);
        btnTypeManage.setOnClickListener(new TypeManageOnClick());
    }
	
	private class WordOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent it = new Intent(WordMenu.this,WordLearn.class);
			it.putExtra("info","单词学习");
			WordMenu.this.startActivity(it);
		}
	}
	
	private class GroupLearnOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent it = new Intent(WordMenu.this,GroupMenu.class);
//			it.putExtra("info","单词学习");
			WordMenu.this.startActivity(it);
		}
	}
	private class WordManageOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent it = new Intent(WordMenu.this,WordManage.class);
//			it.putExtra("info","单词学习");
			WordMenu.this.startActivity(it);
		}
	}
	private class TypeManageOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
//			Intent it = new Intent(WordMenu.this,GrammarView.class);
////			it.putExtra("info","单词学习");
//			WordMenu.this.startActivity(it);
		}
	}
}
