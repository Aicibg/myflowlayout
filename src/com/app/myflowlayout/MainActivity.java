package com.app.myflowlayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
   private FlowLayout flayout;
   private String[] strs=new String[]{
	   "Hello Word","Hello","Hello Android","Hello Java","Word",
	   "Hello Word","Hello","Are you ok","Hello Joe","Hello Word",
	   "Hello","PHp","C++","Hello Word","C",
   };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		flayout=(FlowLayout) findViewById(R.id.fl_flowlayout);
		initData();
	}

	private void initData() {
		LayoutInflater lif=LayoutInflater.from(this);
		for(int i=0;i<strs.length;i++){
			TextView tv=(TextView) lif.inflate(R.layout.tv_layout, flayout,false);
			tv.setText(strs[i]);
			flayout.addView(tv);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
