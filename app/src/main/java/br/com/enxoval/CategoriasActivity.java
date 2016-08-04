package br.com.enxoval;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import br.com.mvc.Db_mvc;
import br.com.mvc.Metodos_auxiliares;


public class CategoriasActivity extends MenuEnxovalActivity {
	ListView lst_categorias;
	ListView lst_sub_categorias;
	Db_mvc database = new Db_mvc(this);
	SubMenuAdapter dataAdapter;
	String campos[] = {"cate_descricao"};
	ArrayList <String> categorias;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState, R.layout.categorias, R.id.categorias_lly_principal);
		//super.onCreate(savedInstanceState);
        //setContentView(R.layout.categorias);
        inicializa_elementos();

        //listeners();
        
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);		
        return true;
    }
    @SuppressWarnings("deprecation")
	private void inicializa_elementos() {
    	try
    	{
    		categorias = new ArrayList<String>();
    		Cursor c = carrega_categorias();
    		if(c == null)
    		{
    			Metodos_auxiliares.mensagem("Erro ao iniciar os elementos", getApplicationContext());
    			return;
    		}
    		if(c.moveToFirst())
    		{
    			do
    			{
    				categorias.add(c.getString(c.getColumnIndex("cate_descricao")));
    			}while (c.moveToNext());
    		}
    		c.close();
	    	lst_categorias = (ListView) findViewById(R.id.categorias_lst_categorias);	    	
	    	dataAdapter = new SubMenuAdapter(getApplicationContext(), categorias);
	    	lst_categorias.setAdapter(dataAdapter);
	    	
    	}
    	catch(Exception e)
		{
			Metodos_auxiliares.mensagem("Erro ao iniciar os elementos" + e.getMessage() + e.toString(), getApplicationContext());
		}
    	
    }
    
    private Cursor carrega_categorias()
    {
    	try
    	{
    		return database.select("select cate_descricao, cate_id as _id from categorias");    		
    	}
    	catch(Exception e)
    	{
    		Metodos_auxiliares.mensagem("Erro ao executar a consulta" + e.getMessage() + e.toString(), getApplicationContext());
    		return null;
    	}
    	
    }
    
/*    private void listeners()
    {
    	lst_categorias.setOnItemClickListener(new OnItemClickListener() {
    		  @Override
    		  public void onItemClick(AdapterView<?> parent, View view,
    		    int position, long id) {
    			Intent it = new Intent(getApplicationContext(), SubCategoriasActivity.class);
				it.putExtra("cate_id", String.valueOf(position + 1));
				startActivity(it);				      
    		  }
		}); 
    }   */ 
}
