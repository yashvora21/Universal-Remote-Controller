package com.example.BOLO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
public class Command extends Activity implements
AdapterView.OnItemSelectedListener{
	public Bluetooth bt;
	protected MyApplication app;
	  private Spinner spinner1,spinner3;
	  String cm[][]={
			  {"Television","Volume Up","Volume down","Next channel","Previous channel","up","down","left","right","ok","Power On","Power off","source","mute","menu","exit"},
			  {"AC","temperature Up","temperature down","power on","power off"},
			  {"DVD Player","Next","Previous","Power on","Power off"}
			};
	  public void addItemsOnSpinner1() {
			 
		  spinner1 = (Spinner) findViewById(R.id.spinner1);
		  List<String> list = new ArrayList<String>();
			DatabaseConnector dc=new DatabaseConnector(Command.this);
			String name=spinner3.getSelectedItem().toString();
			
			try{
			 String type=dc.getDeviceType(name);
			 Toast.makeText(Command.this,
						type,
							Toast.LENGTH_SHORT).show();
			for(int i=0;i<cm.length;i++)
			{
				if(cm[i][0].equals(type))
				{
					for(int j=1;j<cm[i].length;j++)
					{
						list.add(cm[i][j]);
					}
				}		
			}
			
			}catch(Exception e){
				Log.i("Command", e.toString());
			}
			//list.add("Volume Down");
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner1.setAdapter(dataAdapter);
		  }
	
	  public void addItemsOnSpinner3() {
			 
		  spinner3 = (Spinner) findViewById(R.id.spinner3);
		  List<String> list = new ArrayList<String>();
			DatabaseConnector dc=new DatabaseConnector(Command.this);
			 String data[]=dc.getAllDevices();
			for(int i=0;i<data.length;i++)
			{
				list.add(data[i]);
			}
			//list.add("Volume Down");
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner3.setAdapter(dataAdapter);
		  }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_command);
		bt=Bluetooth.getInstance();
		addItemsOnSpinner3();
        Button button2 = (Button) findViewById(R.id.button2);
		Spinner spin = (Spinner) findViewById(R.id.spinner3);
		spin.setOnItemSelectedListener(Command.this);
			button2.setOnClickListener(new OnClickListener() {
				 
				  @Override
				  public void onClick(View v) {
					  int cnt=0;
					  Log.i("Bluetooth data","1");
					  Toast.makeText(Command.this,"Press the Remote Button..",
									Toast.LENGTH_SHORT).show();
					  bt.SendDataToBluetooth(1);
					  Log.e("data sent","1");
						DatabaseConnector dc=new DatabaseConnector(Command.this);
					  String s="";
					  String cname,name,value = "",bits="",decodetype="";
					  name=spinner3.getSelectedItem().toString();
					  cname=spinner1.getSelectedItem().toString();
					  
					  while(true)
						  {
						  Log.e("while loop","proc");
						  
						  try {
							if(bt.bis.available()!=-1)
							{
								if(s.equals("|"))
										s="";
								char d=(char)bt.bis.read();
								
							Log.i("DATA",d+"");
							 
								  if(d=='|')
									  cnt++;
								  if(cnt==1)
								  {
									  cnt++;
										s="";
								  }
								  if(cnt==3)
								  {
									  cnt++;
									  Log.i("value",s);
										value=s;
										s="";
								  }
								  if(cnt==5)
								  {

									  cnt++;
									  Log.i("bits",s);
										bits=s;
										s="";
								  }
								  if(cnt==7)
								  {

									  cnt++;
									  Log.i("decodetype",s);
										decodetype=s;
										s="";
										break;
								  }s+=d;
									
							}
							
							} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						  }
					  value=Confirmno(value);
					  bits=Confirmno(bits);
					  decodetype=Confirmno(decodetype);
					  if(value=="" || value==null || bits=="" || bits==null || decodetype=="" || decodetype==null)
					  {
						  Toast.makeText(Command.this,"try again....",
									Toast.LENGTH_SHORT).show();  
					  }
					  else
					  {
						  Toast.makeText(Command.this,"cname : " + cname+"\nname : " + name+
							  "\nvalue : " + value+"\nbits : " +bits+"\ndecodetype :"+decodetype,
									Toast.LENGTH_SHORT).show();

						dc.AddCommand(cname,name,value,bits,decodetype);		  
					  }
				  }
						 String Confirmno(String s)
						 {
							 String sb="";
							 for(int i=0;i<s.length();i++)
								 if(s.charAt(i)>='0' && s.charAt(i)<='9')
									 sb+=s.charAt(i);
								 
							 return sb;
						 }
							});		
		 
	}
	
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		addItemsOnSpinner1();
	}

	public void onNothingSelected(AdapterView<?> parent) {
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_command, menu);
		return true;
	}
    

}

