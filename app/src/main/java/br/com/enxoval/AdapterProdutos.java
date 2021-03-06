package br.com.enxoval;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import br.com.mvc.Metodos_auxiliares;

public class AdapterProdutos extends BaseAdapter {
	
	private LayoutInflater mInflater; 
	private List<Produtos> itens;
	private ActionMode mActionMode;
	private Context context;
	
	public AdapterProdutos(Context context, List<Produtos> itens) 
	{
		// TODO Auto-generated constructor stub
		 //Itens do listview
		this.itens = itens; 
		this.context = context;		
		//Objeto responsável por pegar o Layout do item.
		mInflater = LayoutInflater.from(context); 
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return itens.size();
	}

	@Override
	public Produtos getItem(int position) {
		// TODO Auto-generated method stub
		return itens.get(position);
		
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		Produtos produto = itens.get(position);
		return produto.getId();
		
	}

	@Override
	public View getView(final int position, View view, final ViewGroup parent) {
		// TODO Auto-generated method stub
		if(view == null)
			view = mInflater.inflate(R.layout.lst_view_produtos, null);
		final View outra_view = view;
		final Produtos produto = getItem(position);
		TextView txt_produto = (TextView) view.findViewById(R.id.lst_view_produtos_txt_produto);
		TextView txt_sugestao = (TextView) view.findViewById(R.id.lst_view_produtos_txt_sugestao);
		final TextView txt_qtd_comprado = (TextView) view.findViewById(R.id.lst_view_produtos_lbl_qtd_comprado);
		final TextView txt_comprado = (TextView) view.findViewById(R.id.lst_view_produtos_txt_comprado);				
		Button btn_mais = (Button) view.findViewById(R.id.lst_view_produtos_btn_mais);
		final Button btn_menos = (Button) view.findViewById(R.id.lst_view_produtos_btn_menos);
		final ImageButton btn_editar = (ImageButton) view.findViewById(R.id.lst_view_produtos_btn_editar);
		final ImageButton btn_salvar = (ImageButton) view.findViewById(R.id.lst_view_produtos_btn_salvar);
		final ImageButton btn_cancelar = (ImageButton) view.findViewById(R.id.lst_view_produtos_btn_cancelar);
		final LinearLayout lly_opcao = (LinearLayout) view.findViewById(R.id.lst_view_produtos_lly_opcao);
		final LinearLayout lly_editar = (LinearLayout) view.findViewById(R.id.lst_view_produtos_lly_editar);
		final LinearLayout lly_botoes = (LinearLayout) view.findViewById(R.id.lst_view_produtos_lly_botoes);
		
		lly_editar.setVisibility(View.GONE);
		lly_botoes.setVisibility(View.GONE);
		lly_opcao.setVisibility(View.VISIBLE);		
		btn_editar.setVisibility(View.VISIBLE);			
		
		
		txt_produto.setText(produto.getProduto());
		txt_sugestao.setText(String.valueOf(produto.getSugestao()));
		
		String string;
		float n = produto.getComprado();
		if (n % 1 == 0) {
		    string = String.valueOf((int) n);
		} else {
		    string = String.valueOf(n).replace(".", ",");
		}
		txt_comprado.setText(string);
		txt_qtd_comprado.setText(string);

		btn_editar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Escolha...");
                builder.setItems(R.array.menu_poup_up_editar_produto, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0)
                    {
                        txt_comprado.setText(txt_qtd_comprado.getText());

                        lly_opcao.setVisibility(View.GONE);
                        lly_editar.setVisibility(View.VISIBLE);
                        btn_editar.setVisibility(View.GONE);
                        lly_botoes.setVisibility(View.VISIBLE);
                        //txt_comprado.setFocusable(true);
                        txt_comprado.requestFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(txt_comprado, InputMethodManager.SHOW_IMPLICIT);

                        ListView lt = (ListView) parent;
                        lt.setSelection(position);
                    }

                    if(which == 1)
                    {
						Intent it = new Intent(context, EditarProdutosActivity.class);
						it.putExtra("id", String.valueOf(produto.getId()));
						it.putExtra("nome", produto.getProduto());
						it.putExtra("sugestao", String.valueOf(produto.getSugestao()));
						it.putExtra("ja_tenho", String.valueOf(produto.getComprado()));
						it.putExtra("label", "Editar produto");
						context.startActivity(it);
						return;
                    }
					if(which == 2)
					{
						AlertDialog alerta;
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("Atenção");
						builder.setMessage("Confirma a exclusão do produto?");
						builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								ListView lt = (ListView) parent;
								if(produto.excluir(context, produto)) {

									try {
										itens.remove(position);
										notifyDataSetChanged();
										//lt.removeView(outra_view);
									}
									catch (Exception e)
									{

									}
									Metodos_auxiliares.mensagem("Produto excluído com sucesso!", context);
								}
								else
									Metodos_auxiliares.mensagem("Erro ao excluir o produto!", context);
							}
						});
						builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface arg0, int arg1) {
								return;
							}
						});
						alerta = builder.create();
						alerta.show();
					}
                }
            });

                builder.setInverseBackgroundForced(true);
                builder.create();
                builder.show();
            }
		});
		
		txt_comprado.setOnEditorActionListener(new OnEditorActionListener() {  
            @Override  
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {  
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {  
                        btn_salvar.performClick();
                    }  
                    return true;  
                }  
                return false;  
            }  
        });  
		
		btn_salvar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lly_opcao.setVisibility(View.VISIBLE);
				lly_editar.setVisibility(View.GONE);
				btn_editar.setVisibility(View.VISIBLE);			
				lly_botoes.setVisibility(View.GONE);
				
				float number = 0;
				try
				{
					number = Float.valueOf(txt_comprado.getText().toString().replace(',','.'));
					produto.soma_comprado(mInflater.getContext(), number);
					produto.setComprado(number);
					txt_qtd_comprado.setText(txt_comprado.getText().toString());
					InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
                    in.hideSoftInputFromWindow(txt_comprado.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
				catch(NumberFormatException e){
					Metodos_auxiliares.mensagem("Erro ao editar o valor.", context);
				}			
				
			}
		});
		
		btn_cancelar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lly_opcao.setVisibility(View.VISIBLE);
				lly_editar.setVisibility(View.GONE);
				btn_editar.setVisibility(View.VISIBLE);			
				lly_botoes.setVisibility(View.GONE);			
				InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
                in.hideSoftInputFromWindow(txt_comprado.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		
		btn_mais.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //produto.soma_comprado(mInflater.getContext(), produto.getComprado() + 1);
                //produto.setComprado(produto.getComprado() + 1);

                String string;
                float n = Float.valueOf(txt_comprado.getText().toString().replace(",", "."));
                n = n + 1;
                if (n % 1 == 0) {
                    string = String.valueOf((int) n);
                } else {
                    string = String.valueOf(n).replace(".", ",");
                }
                txt_comprado.setText(string);
                //txt_qtd_comprado.setText(string);
                //txt_comprado.setText((String.valueOf(produto.getComprado())).replace(".", ","));
            }
        });
		
		btn_menos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//produto.soma_comprado(mInflater.getContext(), produto.getComprado() - 1);
				//produto.setComprado(produto.getComprado() - 1);

				String string;
				float n = Float.valueOf(txt_comprado.getText().toString().replace(",", "."));
				n = n - 1;
				if (n % 1 == 0) {
					string = String.valueOf((int) n);
				} else {
					string = String.valueOf(n).replace(".", ",");
				}
				txt_comprado.setText(string);
				//txt_qtd_comprado.setText(string);
				//txt_comprado.setText(String.valueOf(produto.getComprado()).replace(".", ",").replace(",0",""));
			}
		});
		
		txt_comprado.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

/*				float number = 0;
				try
				{
					number = Float.valueOf(s.toString().replace(',','.'));
					produto.soma_comprado(mInflater.getContext(), number);
					produto.setComprado(number);
				}
				catch(NumberFormatException e){
					return;
				}			*/
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub


			}
		});
		
		return view;
	}

	/*private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO Auto-generated method stub
			mActionMode = null;
		}
		
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.editar_produtos, menu);
			return true;
		}
		
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO Auto-generated method stub
			return false;
		}

	};*/
	
}


