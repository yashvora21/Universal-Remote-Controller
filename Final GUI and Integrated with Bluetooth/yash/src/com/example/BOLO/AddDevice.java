package com.example.BOLO;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
public class AddDevice extends Activity {
	private Spinner spinner4;
	Button btn;
	public void addItemsOnSpinner1() {
		 
		spinner4 = (Spinner) findViewById(R.id.spinner4);
		List<String> list = new ArrayList<String>();
		list.add("Television");
		list.add("DVD Player");
		list.add("AC");
		//list.add("Volume Down");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner4.setAdapter(dataAdapter);
	  }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_device);
		addItemsOnSpinner1();
		btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(btnListener);
		}catch(Exception e){
			
			  Toast.makeText(AddDevice.this,e.toString(),
							Toast.LENGTH_SHORT).show();
			
		}
		/*ListView list_view=(ListView)findViewById(R.id.mylist);
		String[] values=new String[]{"Television","Air Conditioner","VCR"};
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,values);
		list_view.setAdapter(adapter);
		*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_device, menu);
		
		return true;
	}
    private OnClickListener btnListener = new OnClickListener() {
        public void onClick(View v){

        	String type=spinner4.getSelectedItem().toString();
        	String name=((EditText)findViewById(R.id.editText1)).getText().toString();
            
			  
			  DatabaseConnector dc=new DatabaseConnector(AddDevice.this);
			  dc.insertDevice(type, name);
            Intent startSuccess=new Intent(AddDevice.this, Command.class);
    		AddDevice.this.startActivity(startSuccess);
            }
    };

}
