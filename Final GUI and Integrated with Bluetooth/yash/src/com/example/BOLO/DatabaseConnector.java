package com.example.BOLO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DatabaseConnector {
	
	private static final String DB_NAME = "BOLO";
	private SQLiteDatabase database;
	private DatabaseOpenHelper dbOpenHelper;
	   
	public DatabaseConnector(Context context) {
		dbOpenHelper = new DatabaseOpenHelper(context, DB_NAME, null, 2);
	}
	
	   public void open() throws SQLException 
	   {
	      //open database in reading/writing mode
	      database = dbOpenHelper.getWritableDatabase();
	   } 

	   public void close() 
	   {
	      if (database != null)
	         database.close();
	   }	   
	   
	   public void insertDevice(String type ,String name) 
			   {
			      ContentValues newCon = new ContentValues();
			      newCon.put("type", type);
			      newCon.put("name", name);
			      open();
			      database.insert("device", null, newCon);
			      close();
			   }

			
			   public void updateDevice(long id, String type ,String name) 
			   {
			      ContentValues editCon = new ContentValues();
			      editCon.put("name", name);
			      editCon.put("type", type);			  
			      open();
			      database.update("device", editCon, "_id=" + id, null);
			      close();
			   }

			  
			   public String[] getAllDevices() 
			   { open();
			      Cursor cursor= database.query("device", new String[] { "name"}, 
			         null, null, null, null, "name");
			      int i=0;
			      String data[]=new String[cursor.getCount()];
			      if (cursor.moveToFirst()){
			    	   while(!cursor.isAfterLast()){
			    	      data[i] = cursor.getString(cursor.getColumnIndex("name"));
			    	      i++;
			    	      // do what ever you want here
			    	      cursor.moveToNext();
			    	   }
			    	}
			    	cursor.close();
			    	   close();
			    	return data;
			   }
			   public String getDeviceType(String name) 
			   {
				   open();
				   Log.e("name",name);
				   String data="";
//			      Cursor cursor=database.query("device", new String[]{"type"}, "name=" + name, null, null, null, null);
				   try
				   {
				   Cursor cursor= database.rawQuery(
						      "SELECT type "
						    + "FROM device "
						    + "where name='"+name+"';",
						    null
						    
						);
			      if (cursor.moveToFirst()){
			    	   while(!cursor.isAfterLast()){
			    	      data = cursor.getString(cursor.getColumnIndex("type"));
			    	      cursor.moveToNext();
			    	   }
			    	}
			    	cursor.close();
			    	
			    	 close();
			    	 Log.e("Data",data);
				   }catch(Exception e){Log.e("Query",e.toString());}
			    	 return data;
			   }
			   
			   public void deleteDevice(long id) 
			   {
			      open(); 
			      database.delete("device", "_id=" + id, null);
			      close();
			   }
			   public void AddCommand(String cname,String name,String value,String bits,String decodetype)
			   {
				   ContentValues newCon = new ContentValues();
				      newCon.put("cname", cname);
				      newCon.put("name", name);
				      newCon.put("value", value);
				      newCon.put("bits", bits);
				      newCon.put("decodetype", decodetype);
				      
				      open();
				      database.delete("command", "cname='"+cname+"' and name='"+name+"';",null);
				      database.insert("command", null, newCon);
				      close();
			   }
			   
			   
			   
			   public String[] getCommandData(String cname,String name) 
			   {
				   open();
				   String data[]=new String[3];
				   try
				   {
				   Cursor cursor= database.rawQuery(
						      "SELECT value,bits,decodetype "
						    + "FROM command "
						    + "where name='"+name+"' and cname='"+cname+"';",
						    null
						);
			      if (cursor.moveToFirst()){
			    	   while(!cursor.isAfterLast()){
			    		   data[0] = cursor.getString(cursor.getColumnIndex("value"));
			    		   data[1] = cursor.getString(cursor.getColumnIndex("bits"));
			    		   data[2] = cursor.getString(cursor.getColumnIndex("decodetype"));
				    	      
			    	      cursor.moveToNext();
			    	   }
			    	}
			    	cursor.close();
			    	
			    	 close();
				   }catch(Exception e){Log.e("Query",e.toString());}
			    	 return data;
			   }
			   public String[] getCommand(String name) 
			   {
				   open();
				   String data[]=null;
				   try
				   {
				   Cursor cursor= database.rawQuery(
						      "SELECT cname "
						    + "FROM command "
						    + "where name='"+name+"';",
						    null
						);
				  data=new String[cursor.getCount()];
				  int i=0;
			      if (cursor.moveToFirst()){
			    	   while(!cursor.isAfterLast()){
			    		   data[i]=cursor.getString(cursor.getColumnIndex("cname"));
			    		  i++;    
			    	      cursor.moveToNext();
			    	   }
			    	}
			    	cursor.close();
			    	
			    	 close();
				   }catch(Exception e){Log.e("Query",e.toString());}
			    	 return data;
			   }

			   
}