package br.com.mvc;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db_mvc extends SQLiteOpenHelper {

	//version 3 update mosqueteiro para mosquiteiro;
	private static final int DATABASE_VERSION = 3;
	private Context context;
	
	public Db_mvc(Context context) {
		super(context, "enxoval", null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {		
			executar_querys(db, "script.txt");
			
		} catch (IOException e) {
			Metodos_auxiliares.mensagem("Erro ao cadastrar os produtos", context);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
		try {		
			executar_querys(db, "update.txt");
			
		} catch (IOException e) {
			Metodos_auxiliares.mensagem("Erro ao cadastrar os produtos", context);
		}
	}
	
	public void executar_querys(SQLiteDatabase db, String arquivo) throws IOException {
		AssetManager manager = context.getAssets();
 
		InputStream inputStream = null;
		BufferedReader reader = null;
 
		try {
			inputStream = manager.open(arquivo);
			reader = new BufferedReader(new InputStreamReader(inputStream));
 
			StringBuilder stringBuilder = new StringBuilder();
 
			String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
 
			String[] sqls = stringBuilder.toString().split(";");
 
			for (String sql : sqls) {
				db.execSQL(sql);
			}
 
		} catch (IOException e) {
			throw e;
			
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
 
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				throw e;
			}
		}
	}
	
	public Cursor select(String query) throws Exception{
		SQLiteDatabase sqLite = this.getReadableDatabase();
		
		Cursor cursor = null;		
 
		cursor = sqLite.rawQuery(query, null);	
					
		return cursor;
	}
	
	public void query(String query){
		try
		{
			SQLiteDatabase sqlLite = this.getWritableDatabase(); 
			sqlLite.execSQL(query);
		}
		catch(Exception e)
		{
			Metodos_auxiliares.mensagem("Erro ao executar a consulta da query", null);
		}
	}

}
