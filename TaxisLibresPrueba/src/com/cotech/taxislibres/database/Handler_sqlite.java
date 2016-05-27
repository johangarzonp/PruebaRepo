package com.cotech.taxislibres.database;

import static android.provider.BaseColumns._ID;

import com.cotech.taxislibres.C;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.util.Log;
import com.cotech.taxislibres.activities.Log;


public class Handler_sqlite extends SQLiteOpenHelper{

	
	
	public Handler_sqlite(Context ctx)
	{
		super(ctx, "MiBase", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String query = "CREATE TABLE usuarios ("+_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
				"user TEXT, password TEXT);";
		db.execSQL(query);
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int version_ant, int version_nue)
	{
		db.execSQL("DROP TABLE IF EXISTS usuarios");
		onCreate(db);
	}
	
	public void insertarReg(String usr, String pass)
	{
		//Hay que verificar si es posible insertar los registros...
		int size=0;
		int campos=0;
		String vector[]=this.leer();
		while(campos < vector.length){
			if(vector[campos]!=null){
				campos++;
			}
			else{
				size=campos-1;
				campos=1000;
			}
		}

		ContentValues valores = new ContentValues();
		valores.put("user", usr); //COLUMNA de usuario (user) un nombre de usuario
		valores.put("password", pass);//COLUMNA password, insertamos un valor que es pass
		
		if(size < (C.NUM_MENSAJES -50)){	//Se puede insertar el registro
			this.getWritableDatabase().insert("usuarios",null, valores);
			Log.i("TAXIS LIBRES", "BASE DE DATOS" + " INSERTA REGISTRO: "+ size);
		}else{	//Hay que borrar la base.... Y despues insertar el registro
			this.getWritableDatabase().delete("usuarios", null , null);
			//this.getWritableDatabase().insert("usuarios",null, valores);
			Log.i("TAXIS LIBRES", "BASE DE DATOS" + "BORRANDO LA BASE DE DATOS...."+ size);
		}
	}
	
	public String[] leer()
	{
		String result[] = new String [C.NUM_MENSAJES];	//Se declara del tamaño NUM_MENSAJES...
		String columnas[]= {_ID,"user","password"};
		Cursor c = this.getReadableDatabase().query("usuarios", columnas, null, null,null, null, null);
		
		int id,iu,ip;
		id = c.getColumnIndex(_ID);
		iu = c.getColumnIndex("user");
		ip = c.getColumnIndex("password");
		
		int contador=0;
		for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		{
			//result[contador] = c.getString(id)+" "+c.getString(iu)+" "+c.getString(ip) +"\n";
			result[contador] = c.getString(iu)+" "+c.getString(ip) +"\n";
			contador++;
		}
		
		
		//c.moveToLast();
		
	   // result = c.getString(id)+" "+c.getString(iu)+" "+c.getString(ip) +"\n";
		
	    return result;
	    
	}
	
	public void abrir()
	{
		this.getWritableDatabase();
		
	}
	
	public void cerrar()
	{
		this.close();
	}
	
	public void deleteAllMessages() {

		this.getWritableDatabase().delete("usuarios", null , null);
		
	}

}
