package br.com.enxoval;

import android.content.Context;
import android.database.Cursor;
import br.com.mvc.Db_mvc;

public class Categorias {
	Db_mvc database;
	
	private String cate_nome;
	private String suca_nome;

	public String getCate_nome(Context context, String suca_id) throws Exception {
		String categoria;		 
		database = new Db_mvc(context);
		Cursor c;		
		c = database.select("select cate_descricao from subcategorias join categorias on cate_id = suca_cate_id where suca_id = " + suca_id);
		c.moveToFirst();
		categoria = c.getString(c.getColumnIndex("cate_descricao"));
		c.close();
		return categoria;		
	}
	
	public String getSuca_nome(Context context, String suca_id) throws Exception {
		String subcategoria;		 
		database = new Db_mvc(context);
		Cursor c = null;
		c = database.select("select suca_descricao from subcategorias where suca_id = " + suca_id);
		c.moveToFirst();
		subcategoria = c.getString(c.getColumnIndex("suca_descricao"));
		c.close();
		return subcategoria;		
	}

	
}
