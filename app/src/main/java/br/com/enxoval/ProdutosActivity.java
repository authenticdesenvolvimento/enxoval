package br.com.enxoval;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import br.com.mvc.Db_mvc;
import br.com.mvc.Metodos_auxiliares;

public class ProdutosActivity extends MenuEnxovalActivity {
	String suca_id;
	ArrayList<Produtos> produtos;
	ListView lst_produtos;
	TextView cate_suca;
	EditText pesquisar;
	Produtos prod;
	Db_mvc database;
	Categorias cate;
	boolean menu_item1 = false;
	boolean menu_item2 = false;
	boolean menu_item7 = false;
	private Menu meu_menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.produtos, R.id.lly_produtos);
		//super.onCreate(savedInstanceState);
		//setContentView(R.layout.produtos);

	    Bundle extras = getIntent().getExtras();
	    suca_id = extras.getString("suca_id");
		super.suca_id = suca_id;
	    menu_item1 = extras.getBoolean("menu_item1");
	    menu_item2 = extras.getBoolean("menu_item2");
	    menu_item7 = extras.getBoolean("menu_item7");
	    
	    inicializa_elementos();
	    listeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		meu_menu = menu;
		if(!(menu_item1 || menu_item2 || menu_item7))
		{
			menu.getItem(0).setVisible(true);
		}
		return true;
	}

	@Override
	protected void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    setIntent(intent);
	}
/*
	@Override
	protected void onResume()
	{
		//super.onResume();
		//setContentView(R.layout.produtos);
	    Bundle extras = getIntent().getExtras();
	    suca_id = extras.getString("suca_id");
	    menu_item1 = extras.getBoolean("menu_item1");
	    menu_item2 = extras.getBoolean("menu_item2");
	    menu_item7 = extras.getBoolean("menu_item7");
	    inicializa_elementos();
	    listeners();
	}*/
	private void inicializa_elementos() {

		lst_produtos = (ListView) findViewById(R.id.produtos_lst_produtos);
		cate_suca = (TextView) findViewById(R.id.produtos_txt_cate_suca);
		pesquisar = (EditText) findViewById(R.id.produtos_txt_pesquisar);
		cate = new Categorias();
		produtos = new ArrayList<Produtos>();
		database = new Db_mvc(this);
		
		if(menu_item1)
		{
			cate_suca.setText("Produtos que já completei");
			carrega_produtos_tenho();
		}
		else if(menu_item2)
		{
			cate_suca.setText("Produtos que faltam completar");
			carrega_produtos_falta();
		}
		else if(menu_item7)
		{
			cate_suca.setText("Todos os produtos");
			carrega_produtos_todos("");
		}
		else
		{
			try {
				cate_suca.setText(cate.getCate_nome(this, suca_id) + " -> " + cate.getSuca_nome(this, suca_id));
				carrega_produtos_subcategoria();
			} catch (Exception e) {
				Metodos_auxiliares.mensagem("Erro ao carregar os produtos", this);
			}
		}				
	}
	
	private void listeners() {
		pesquisar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					if (event.getAction() == KeyEvent.ACTION_DOWN) {
						InputMethodManager in = (InputMethodManager) ProdutosActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
						in.hideSoftInputFromWindow(pesquisar.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}
					return true;
				}
				return false;
			}
		});

		pesquisar.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub				
			}

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 0)
				{
					cate_suca.setText("Todas as categorias");
					carrega_produtos_todos(s.toString());
					if(meu_menu != null)
						meu_menu.getItem(0).setVisible(false);
				}
				else
				{
					if(menu_item1)
					{
						cate_suca.setText("Produtos que já completei");
						carrega_produtos_tenho();
					}
					else if(menu_item2)
					{
						cate_suca.setText("Produtos que faltam completar");
						carrega_produtos_falta();
					}
					else if(menu_item7)
					{
						cate_suca.setText("Todos os produtos");
						carrega_produtos_todos("");
					}
					else
					{
						if(meu_menu != null)
							meu_menu.getItem(0).setVisible(true);
						try {
							cate_suca.setText(cate.getCate_nome(ProdutosActivity.this, suca_id) + " -> " + cate.getSuca_nome(ProdutosActivity.this, suca_id));
						} catch (Exception e) {
							Metodos_auxiliares.mensagem("Erro ao processar a categoria", ProdutosActivity.this);
						}
						carrega_produtos_subcategoria();
					}										
				}				
			}
		});		
	}

	
	private void carrega_produtos_subcategoria() {
		try
    	{
	    	produtos.clear();
	    	carrega_adapter_produtos(Produtos.getProdutosSubcategoria(this, suca_id));	    	
	    }
	    catch(Exception e)
		{
			Metodos_auxiliares.mensagem("Erro ao carregar os produtos.", this);
		}		
	}
	
	private void carrega_produtos_todos(String s)
	{
		try
    	{
			produtos.clear();
			carrega_adapter_produtos(Produtos.getProdutosTodos(this, null, s));
    	}
		catch(Exception e)
		{
			Metodos_auxiliares.mensagem("Erro ao carregar os produtos.", this);
		}
	}
	
	private void carrega_produtos_tenho() {
		try
    	{   
	    	produtos.clear();
	    	carrega_adapter_produtos(Produtos.getProdutosTenho(this));   		
	    	
    	}
    	catch(Exception e)
		{
			Metodos_auxiliares.mensagem("Erro ao carregar os produtos.", this);
		}		
	}
	
	private void carrega_produtos_falta() {
		try
    	{   
	    	produtos.clear();
	    	carrega_adapter_produtos(Produtos.getProdutosFalta(this));   		
	    	
    	}
    	catch(Exception e)
		{
			Metodos_auxiliares.mensagem("Erro ao carregar os produtos.", this);
		}		
	}
	
	private void carrega_adapter_produtos(Cursor c)
	{
		if (c.moveToFirst()) 
    	{ 
    		String mes;
			try {
				mes = Sistema.getMesNascimento(this);
			
	            do 
	            {
	            	prod = new Produtos();
	        		prod.setComprado(Float.parseFloat(c.getString(c.getColumnIndex("prod_qtd_comprada"))));
	        		prod.setProduto(c.getString(c.getColumnIndex("prod_nome")));
	        		prod.setSugestao(Integer.parseInt(c.getString(c.getColumnIndex(mes))));	   
	        		prod.setId(Integer.parseInt(c.getString(c.getColumnIndex("_id"))));
	        		produtos.add(prod);
	            } 
	            while (c.moveToNext());
			} catch (Exception e) {				 
				Metodos_auxiliares.mensagem("Erro ao verificar o clima em que o bebê irá nascer", this);
			}
		}    	
    	AdapterProdutos adapter = new AdapterProdutos(this, produtos);	    	
		lst_produtos.setAdapter(adapter);
        c.close();
	}
}
