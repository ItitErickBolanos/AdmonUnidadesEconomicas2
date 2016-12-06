package com.example.admonunidadeseconomicas;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.objects.beans.SubSector;
import com.objects.beans.Usuario;

public class Empresas_Mapa extends Activity {

	private GoogleMap mapa;
	Bundle bundleReceptor;
	Usuario usuarioFromABC;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateExtras();
		setContentView(R.layout.activity_empresas__mapa);
		mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa))
				.getMap();
		mostrarMarcadores();
	}
	
	public void populateExtras() {
		bundleReceptor = getIntent().getExtras();
		if (!bundleReceptor.isEmpty()) {
			usuarioFromABC = new Usuario();
			usuarioFromABC.setUsuarioID(bundleReceptor.getInt("UsuarioID"));
			usuarioFromABC.setLoggin(bundleReceptor.getString("Loggin"));
			usuarioFromABC.setPassword(bundleReceptor.getString("Password"));
			usuarioFromABC.setPerfilID(bundleReceptor.getInt("PerfilID"));
			usuarioFromABC.setDatosID(bundleReceptor.getInt("DatosID"));
			usuarioFromABC.setEmail(bundleReceptor.getString("Email"));
		}
	}

	public void mostrarMarcadores() {
			
			CameraPosition camPos = new CameraPosition.Builder()
			.target(new LatLng(21.7249906, -101.9589915)).zoom(4).build();

			CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);
		
			mapa.animateCamera(camUpd);
			listaMarcadores Marcadores = new listaMarcadores();
			Marcadores.cargarContenido(getApplicationContext());
			Marcadores.execute();
	}
	
	//region CLASE PARA SPINNER ASYNCRONO
			class listaMarcadores extends AsyncTask<Void, Void, String>{
				Context contexto;
				InputStream is;
				
				public void cargarContenido(Context contexto){
					this.contexto = contexto;
				}
				
					@Override
					protected String doInBackground(Void...params) {
						// TODO Auto-generated method stub
						String resultado = "fallo";
						//Creamos la conexion HTTP
						HttpClient cliente = new DefaultHttpClient();
						HttpGet peticionGet =  new HttpGet("http://semestral.esy.es/ConvertidorUnidades/Fetchs.php?opcion=16");
						
						try{
							
							HttpResponse response = cliente.execute(peticionGet);
							HttpEntity contenido = response.getEntity();
							
							is = contenido.getContent();
							
						}catch(ClientProtocolException e){
							e.printStackTrace();
						}catch(IOException e){
							e.printStackTrace();
						}
						
						BufferedReader buferLector = new BufferedReader(new InputStreamReader(is));
						StringBuilder sb = new StringBuilder();
						String linea = null;
						
						try{
							while((linea = buferLector.readLine())!=null){
								sb.append(linea);
							}
						}catch(IOException e){
							e.printStackTrace();
						}
						
						try{
							is.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						
						resultado = sb.toString();
						return resultado;
				}

						@Override
						protected void onPostExecute(String resultado) {
							// TODO Auto-generated method stub
							if(!resultado.equals("null")){
								try {
					
									JSONArray arrayJSON = new JSONArray(resultado);
									for(int i=0; i<arrayJSON.length(); i++){
										JSONObject jsonIterator = new JSONObject(arrayJSON.get(i).toString());
										mapa.addMarker(new MarkerOptions().position(
												new LatLng(Double.parseDouble(jsonIterator.get("Latitud").toString()), Double.parseDouble(jsonIterator.get("Longitud").toString()))).title(
												"Empresa: " + jsonIterator.get("Nombre").toString()).snippet("Razon Social: " + jsonIterator.get("RazonSocial").toString()));
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}

						@Override
						protected void onPreExecute() {

							
						}
						
					}
				//endregion
	
			public void sigLista(){
				Intent i = new Intent(this, Empresas_List.class);
				Bundle b = new Bundle();
					b.putInt("UsuarioID", usuarioFromABC.getUsuarioID());
					b.putString("Loggin", usuarioFromABC.getLoggin());
					b.putString("Password", usuarioFromABC.getPassword());
					b.putInt("PerfilID", usuarioFromABC.getPerfilID());
					b.putInt("DatosID",usuarioFromABC.getDatosID());
					b.putString("Email",usuarioFromABC.getEmail());
						
				i.putExtras(b);
				startActivity(i);
			}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.empresas__list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case R.id.listaEmpresas:
			sigLista();
			break;
		
		case R.id.menuAppSalir:
			System.exit(0);
			break;
		case R.id.menuAppCerrarSesion:
			System.exit(0);
			break;
		case R.id.menuAppSistema:
			Toast.makeText(
					this,
					"Creado por; Legendary Software 'Innovation and Solutions' www.legendarysoftware.com.mx",
					Toast.LENGTH_LONG).show();
			break;
		case R.id.menuAppEstadoServidor:

			try {
				if (InetAddress.getByName("http://semestral.esy.es")
						.isReachable(30)) {
					Toast.makeText(this, "Servidor en linea", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(this, "Servidor fuera de servicio",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Log.i("no llego", "Excepcion de error");
			}
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
