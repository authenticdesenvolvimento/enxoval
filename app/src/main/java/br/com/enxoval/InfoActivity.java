package br.com.enxoval;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class InfoActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Obtain the shared Tracker instance.
		Tracker mTracker;
		AnalyticsApplication application = (AnalyticsApplication) getApplication();
		mTracker = application.getDefaultTracker();
		mTracker.setScreenName(this.getClass().getName());
		mTracker.send(new HitBuilders.ScreenViewBuilder().build());
		setContentView(R.layout.activity_info);
		Button comecar = (Button) findViewById(R.id.info_btn_comecar);
		
		comecar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent it = new Intent(getApplicationContext(), MainActivity.class);				
				startActivity(it);		
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {

			case R.id.menu_facebook_info:
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);

				intent.setData(Uri.parse("http://www.facebook.com.br/enxovaldebebe"));
				startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}

	}
	
	
}
