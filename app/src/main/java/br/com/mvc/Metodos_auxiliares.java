package br.com.mvc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public abstract class Metodos_auxiliares {

	public static void mensagem(String msg, Context contexto)
	{
		/*
		AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
		builder.setMessage(msg)
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		                return;
		           }
		       });
	AlertDialog alert = builder.create();
	alert.show();
	*/
		Toast toast = Toast.makeText(contexto, msg, Toast.LENGTH_LONG);
		toast.show();
	}
}
