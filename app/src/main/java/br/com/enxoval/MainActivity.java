package br.com.enxoval;

import android.app.Activity;
import android.content.Intent;
//import android.database.Cursor;
import android.os.Bundle;
//import br.com.mvc.Db_mvc;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try
    	{
   			if(Sistema.getStatusConfigSistema(getApplicationContext()) != null)
   				if(Sistema.getStatusConfigSistema(getApplicationContext()) == 1)	   		
		    	{
	   				Intent it = new Intent(MainActivity.this, CategoriasActivity.class);		   		
	   		   		startActivity(it);
	   				finish();
	   				return;
		    	}
	   		Intent it = new Intent(MainActivity.this, ConfigBebeActivity.class);		   		
	   		startActivity(it);
	   		finish();
	    }
    	catch(Exception e)
		{
    		Intent it = new Intent(MainActivity.this, ConfigBebeActivity.class);			    		
    		startActivity(it);
    		finish();
		}
		
	}

}
