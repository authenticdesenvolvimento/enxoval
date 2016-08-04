package br.com.enxoval;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import br.com.mvc.Metodos_auxiliares;

public abstract class MenuEnxovalActivity extends ActionBarActivity{
	static String suca_id;
	private static List<Bitmap> bmp = new ArrayList<Bitmap>();
	private static List<String> links = new ArrayList<String>();
	private static List<String> ads_id = new ArrayList<String>();
	private static ImageView image;
	private static Integer i_ads = 0;

	private String jsonResult;
	private String base_url = "http://www.authenticdesenvolvimento.com.br/";
	//private String base_url = "http://192.168.1.104:8080/enxoval/mysql/";
	//private String url = "http://www.authenticdesenvolvimento.com.br/db_select_teste.php";
	private String url = base_url + "db_select_ads.php";
	private LinearLayout linearLayout1;

	private static Timer timerAtual;
	private static TimerTask task;
	private final Handler handler = new Handler();
	private AdView mAdView;

	protected void onCreate(Bundle savedInstanceState, int view_id, int linear_layout_id) {
		super.onCreate(savedInstanceState);

		setContentView(view_id);
		linearLayout1 = (LinearLayout) findViewById(linear_layout_id);
		if (isOnline())
			cria_ads();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Resume the AdView.
		if(mAdView != null)
			mAdView.resume();
		else {
			try {
				if (bmp.size() != 0) {
					ativaTimer();
				}
			} catch (Exception e) {}
		}
	}

	@Override
	public void onPause() {
		if(mAdView != null)
			mAdView.pause();
		else {
			if (null != task)
				task.cancel();
			if (timerAtual != null) {
				timerAtual.cancel();
				timerAtual.purge();
			}
			try {linearLayout1.removeView(image);} catch (Exception e){}
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {
		// Destroy the AdView.
		if(mAdView != null)
			mAdView.destroy();

		super.onDestroy();
	}

	private void cria_ads() {
		try {
			image = new ImageView(this);
			if (bmp.size() == 0 && mAdView == null) {
				accessWebService();
			}
		}
		catch (Exception e)
		{}
	}

	// Async Task to access the web
	public void accessWebService() {
		JsonReadTask task = new JsonReadTask();
		// passes values for the urls string array
		task.execute(new String[] { url });
	}

	private class JsonReadTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpClient httpclient = new DefaultHttpClient();
			try {
				HttpPost httppost = new HttpPost(params[0]);
				HttpResponse response = httpclient.execute(httppost);
				jsonResult = inputStreamToString(
						response.getEntity().getContent()).toString();
			}

			catch (Exception e) {
				return null;
			}
			return null;
		}

		private StringBuilder inputStreamToString(InputStream is) {
			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((rLine = rd.readLine()) != null) {
					answer.append(rLine);
				}
			}

			catch (Exception e) {}
			return answer;
		}
		@Override
		protected void onPostExecute(String result) {
			ListDrwaer();
		}

	}// end async task

	public void ListDrwaer() {
		try {
			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("ads");
			if (jsonMainNode.length() > 0)
				for (int i = 0; i < jsonMainNode.length(); i++) {
					JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
					String ads_imagem = jsonChildNode.optString("ads_imagem");
					String ads_link = jsonChildNode.optString("ads_link");
					String ads_id = jsonChildNode.optString("ads_id");
					get_imagem(ads_imagem, ads_link, ads_id);
				}
			else
				cria_admob();
		}
		catch (Exception e)
		{
			cria_admob();
		}
	}

	private void get_imagem(final String imagem_str, final String link, final String id)
	{
		new AsyncTask<Void, Void, Void>() {
			InputStream in;
			@Override
			protected Void doInBackground(Void... params) {
				try {
					//in = new URL("http://www.authenticdesenvolvimento.com.br/imagem/" + imagem_str + "_" + get_escala() + ".png").openStream();
					in = new URL(base_url + "imagem/" + imagem_str + "_" + get_escala() + ".png").openStream();
					bmp.add(BitmapFactory.decodeStream(in));
					links.add(link);
					ads_id.add(id);

				} catch (Exception e) {
					return null;
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				try {
					if(bmp.size() == 1)
					ativaTimer();
				}
				catch (Exception e){}
			}

		}.execute();
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(0).setVisible(false);
        return true;
    }
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent it;

        switch (id) {

		case R.id.menu_facebook:
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_BROWSABLE);

			intent.setData(Uri.parse("http://www.facebook.com.br/enxovaldebebe"));
			startActivity(intent);
			return true;

		case R.id.menu_novo:
			it = new Intent(getApplicationContext(), EditarProdutosActivity.class);
			it.putExtra("suca_id", String.valueOf(suca_id));
			startActivity(it);
			return true;

		case R.id.menu_item1:
			it = new Intent(getApplicationContext(), ProdutosActivity.class);
			it.putExtra("menu_item1", true);
			startActivity(it);
			return true;

		case R.id.menu_item2:
			it = new Intent(getApplicationContext(), ProdutosActivity.class);
			it.putExtra("menu_item2", true);
			startActivity(it);
			return true;

		case R.id.menu_item7:
			it = new Intent(getApplicationContext(), ProdutosActivity.class);
			it.putExtra("menu_item7", true);
			startActivity(it);
			return true;

		case R.id.menu_item8:
			Metodos_auxiliares.mensagem("Desenvolvido por:\nAuthentic Desenvolvimento - Robinson batista da silva \n\nE-mail:\nauthentic.desenvolvimento@gmail.com", this);
			return true;

		/*case R.id.menu_item3:

			return true;

		case R.id.menu_item4:

			return true;

		case R.id.menu_item5:

			return true;
			*/
		case R.id.menu_item6:
			it = new Intent(getApplicationContext(), ConfiguracoesActivity.class);
			startActivity(it);
			return true;

		case android.R.id.home:
            // app icon in action bar clicked; goto parent activity.
			if(!this.getClass().getSimpleName().equals(CategoriasActivity.class.getSimpleName()))
				finish();
            return true;

		default:
			return super.onOptionsItemSelected(item);
		}

    }

	private String get_escala()
	{
		int density= getResources().getDisplayMetrics().densityDpi;
		String escala = "";
		switch(density)
		{
			case DisplayMetrics.DENSITY_LOW:
				escala = "ldpi";
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				escala = "mdpi";
				break;
			case DisplayMetrics.DENSITY_HIGH:
				escala =  "hdpi";
				break;
			case DisplayMetrics.DENSITY_XHIGH:
				escala =  "xhdpi";
				break;
			default:
				escala = "xhdpi";
				break;
		}
		return escala;
	}

	private void ativaTimer() throws Exception{
		if(bmp.size() == 0)
			return;

		if(bmp.size() == 1 && i_ads == 1)
			conta_impressao(String.valueOf(ads_id.get(i_ads - 1)));

		linearLayout1.addView(image);
		timerAtual = new Timer();
		task = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							if (i_ads == 0 || (i_ads > 0 && bmp.size() > 1)) {
								i_ads++;

								if (i_ads > bmp.size()) {
									i_ads = 1;
								}
								conta_impressao(String.valueOf(ads_id.get(i_ads - 1)));
							}

							image.setImageBitmap(bmp.get(i_ads - 1));

							image.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									try {
										conta_clique(String.valueOf(ads_id.get(i_ads - 1)));

										String uri = Uri.parse(links.get(i_ads - 1)).toString();
										if (uri.equals(""))
											return;

										Intent intent = new Intent();
										intent.setAction(Intent.ACTION_VIEW);
										intent.addCategory(Intent.CATEGORY_BROWSABLE);

										intent.setData(Uri.parse(uri));
										startActivity(intent);
									} catch (Exception e) {
										return;
									}
								}
							});
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							params.gravity = Gravity.CENTER;
							image.setLayoutParams(params);
						}catch (Exception e) {
							return;
						}
						//linearLayout1.addView(image);
					}
				});
			}
		};
		timerAtual.schedule(task, 2000, 10000);
	}

	public boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	private void cria_admob()
	{
		try {
			if (!isOnline())
				return;

			// Create a banner ad. The ad size and ad unit ID must be set before calling loadAd.
			mAdView = new AdView(this);
			mAdView.setAdSize(AdSize.SMART_BANNER);
			mAdView.setAdUnitId("ca-app-pub-8027025344367466/3021603033");

			// Create an ad request.
			AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

			// Optionally populate the ad request builder.
			adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);

			// Start loading the ad.
			mAdView.loadAd(adRequestBuilder.build());

			mAdView.setAdListener(new AdListener() {
				@Override
				public void onAdClosed() {
					super.onAdClosed();
				}

				@Override
				public void onAdFailedToLoad(int errorCode) {
					super.onAdFailedToLoad(errorCode);
				}

				@Override
				public void onAdLeftApplication() {
					super.onAdLeftApplication();
				}

				@Override
				public void onAdOpened() {
					super.onAdOpened();
				}

				@Override
				public void onAdLoaded() {
					super.onAdLoaded();
					// Add the AdView to the view hierarchy.
					try{linearLayout1.addView(mAdView);}catch (Exception e){}
				}
			});
		}
		catch (Exception e){}
	}

	public void conta_clique(final String id)
	{
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				ArrayList<NameValuePair> nameValuePairs;
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("id",id));

				try {
					HttpClient httpclient = new DefaultHttpClient();
					//HttpPost httppost = new HttpPost("http://www.authenticdesenvolvimento.com.br/db_conta_clique.php");
					HttpPost httppost = new HttpPost(base_url + "db_conta_clique_ads.php");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					httpclient.execute(httppost);
				} catch (Exception e) {}
				return null;
			}
		}.execute();
	}

	public void conta_impressao(final String id)
	{
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				ArrayList<NameValuePair> nameValuePairs;
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("id",id));

				try {
					HttpClient httpclient = new DefaultHttpClient();
					//HttpPost httppost = new HttpPost("http://www.authenticdesenvolvimento.com.br/db_conta_impressao.php");
					HttpPost httppost = new HttpPost(base_url + "db_conta_impressao_ads.php");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					httpclient.execute(httppost);
				} catch (Exception e) {}
				return null;
			}
		}.execute();
	}

}
