package br.com.enxoval;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import br.com.mvc.Metodos_auxiliares;
import br.com.mvc.SpinnerAdapter;
import br.com.mvc.SpinnerClass;


public class ConfigBebeActivity extends Activity {
	private Spinner spn_nascimento;
	private Button btn_proximo;
	private RadioGroup rdg_sexo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_bebe);
        inicializa_elementos();
	}
    
    private void inicializa_elementos() {
    	try
    	{
	    	//spinner
	        spn_nascimento = (Spinner) findViewById(R.id.config_bebe_spn_nascimento);        
	        SpinnerClass[] meses = {
	        	new SpinnerClass("Selecione", 0),
		        new SpinnerClass("Calor", 1),
		        new SpinnerClass("Frio", 2)
	        };
	        SpinnerAdapter meses_adapter = new SpinnerAdapter(this, R.layout.spinner, meses);
	        //meses_adapter.setDropDownViewResource(R.layout.spinner_dropdown); 
	        spn_nascimento.setAdapter(meses_adapter);
	        
	        //button        
	        btn_proximo = (Button) findViewById(R.id.config_bebe_btn_proximo);
    	}
    	catch(Exception e)
		{
			Metodos_auxiliares.mensagem("Erro ao iniciar os elementos", ConfigBebeActivity.this);
		}
        
        btn_proximo.setOnClickListener(new OnClickListener() {
           	@Override
			public void onClick(View v) {
           		try{
					
					rdg_sexo = (RadioGroup) findViewById(R.id.config_bebe_rdg_sexo);
					
					if(rdg_sexo.getCheckedRadioButtonId() == -1)
					{
						Metodos_auxiliares.mensagem("Selecione uma opção para o sexo do bebê", ConfigBebeActivity.this);			
						return;
					}
					
					switch (rdg_sexo.getCheckedRadioButtonId()) {
				    case R.id.config_bebe_rdb_menino:
				        Sistema.setSexo("M", getApplicationContext());
				        break;
				    case R.id.config_bebe_rdb_menina:
				    	Sistema.setSexo("F", getApplicationContext());
				        break;
				    case R.id.config_bebe_rdb_nao_definido:
				    	Sistema.setSexo("U", getApplicationContext());
				        break;
					}
					
									
					SpinnerClass mes = (SpinnerClass) spn_nascimento.getSelectedItem();
					if(mes.getValue()==0)
					{
						Metodos_auxiliares.mensagem("Selecione o clima que o bebê irá nascer", ConfigBebeActivity.this);
						return;
					}
					switch (mes.getValue())
					{
					case 1:
						Sistema.setMesNascimento("prod_calor", getApplicationContext());
						break;						
					case 2:
						Sistema.setMesNascimento("prod_frio", getApplicationContext());
						break;
					}

					Sistema.setStatusConfigSistema(1, getApplicationContext());
					Intent it = new Intent(ConfigBebeActivity.this, CategoriasActivity.class);
					startActivity(it);
					finish();
           		}
           		catch(Exception e)
				{
					Metodos_auxiliares.mensagem("Erro ao processar os dados", ConfigBebeActivity.this);
				}
			}		
        });
    }
}
