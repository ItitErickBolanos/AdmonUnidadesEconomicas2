package com.example.admonunidadeseconomicas;

import com.objects.beans.Usuario;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class CIIUR2_ABC extends ActionBarActivity implements OnItemClickListener{


	//region PROPIEDADES DE CLASE
		Bundle b;
		Usuario usuario;
		Intent i;
		TextView headerText;
		ListView menu;
	//endregion
								
	//region ELEMENTOS DEL MENU DE ADMINISTRADOR
				String[] menuListaAdmin = {"Altas",
										   "Cambios",
										   "Consultas"
										   };
	//endregion
						
	//region ELEMENTOS DEL MENU DE USUARIO GENERAL
				String[] menuListaUser = {"Altas",
						   				  "Cambios",
						   				  "Consultas"
	    					   			  };
	//endregion

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ciiur2__abc);
		
		menu = (ListView) findViewById(R.id.menuCIIUR2);
		headerText = (TextView) findViewById(R.id.headerCIIUR2);
		
		populateExtras();
		headerText.setText("Bienvenido: "+usuario.getLoggin());
		
		if(usuario.PerfilID == 1){
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(),
																	android.R.layout.simple_list_item_1,
																	menuListaAdmin);
			menu.setAdapter(listAdapter);
		}else{
			ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getApplicationContext(),
					android.R.layout.simple_list_item_1,
					menuListaUser);
			menu.setAdapter(listAdapter);
		}
		
		menu.setOnItemClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ciiur2__abc, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//region METODO PARA RECUPERAR BUNDLE y SU CONTENIDO
	
			//metodo para recuperar el bundle de la actividad anterior.
				public void populateExtras(){
					b = getIntent().getExtras();
					
					usuario = new Usuario();
						usuario.setUsuarioID(b.getInt("UsuarioID"));
						usuario.setLoggin(b.getString("Loggin"));
						usuario.setPassword(b.getString("Password"));
						usuario.setPerfilID(b.getInt("PerfilID"));
						usuario.setDatosID(b.getInt("DatosID"));
						usuario.setEmail(b.getString("Email"));
				}
	//endregion

	//region ENVIO DE DATOS A USUARIOS PROYECTOS y DEMAS
				//Metodo para enviar session a Ramas_ABC
				public void sigAltas(){
					i = new Intent(this,Ramas_ABC.class);
					b = new Bundle();
						b.putInt("UsuarioID", usuario.getUsuarioID());
						b.putString("Loggin", usuario.getLoggin());
						b.putString("Password", usuario.getPassword());
						b.putInt("PerfilID", usuario.getPerfilID());
						b.putInt("DatosID",usuario.getDatosID());
						b.putString("Email",usuario.getEmail());
							
					i.putExtras(b);
					startActivity(i);
				}
				
				//Metodo para enviar session a SubRamas_ABC	
				public void sigCambios(){
					i = new Intent(this,SubRamas_ABC.class);
					b = new Bundle();
						b.putInt("UsuarioID", usuario.getUsuarioID());
						b.putString("Loggin", usuario.getLoggin());
						b.putString("Password", usuario.getPassword());
						b.putInt("PerfilID", usuario.getPerfilID());
						b.putInt("DatosID",usuario.getDatosID());
						b.putString("Email",usuario.getEmail());
							
					i.putExtras(b);
					startActivity(i);
				}
					//Metodo para enviar session a Clases_ABC
				public void sigConsultas(){
					i = new Intent(this,Clases_ABC.class);
					b = new Bundle();
						b.putInt("UsuarioID", usuario.getUsuarioID());
						b.putString("Loggin", usuario.getLoggin());
						b.putString("Password", usuario.getPassword());
						b.putInt("PerfilID", usuario.getPerfilID());
						b.putInt("DatosID",usuario.getDatosID());
						b.putString("Email",usuario.getEmail());
							
					i.putExtras(b);
					startActivity(i);
				}
				
	//endregion
				
	//region MENU PRINCIPAL y MENU DE LISTVIEW
					
				//maneja los eventos de la ListView
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
					if(parent.getCount()==3){
						switch(position){
							case 0:
								//Toast.makeText(this, "Posicion 1", Toast.LENGTH_SHORT).show();
								sigAltas();
								break;
										
							case 1:
								//Toast.makeText(this, "Posicion 2", Toast.LENGTH_SHORT).show();
								sigCambios();
								break;
								
							case 2:
								//Toast.makeText(this, "Posicion 3", Toast.LENGTH_SHORT).show();
								sigConsultas();
								break;
											
						}
					}
				}
		//endregion


	
}
