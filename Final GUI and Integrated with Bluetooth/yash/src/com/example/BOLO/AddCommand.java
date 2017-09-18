package com.example.BOLO;


import java.util.ArrayList;
import java.util.List;

import com.example.BOLO.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class AddCommand extends Activity {


	  private Spinner spinner2;
	  private Button button2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_command);
		addItemsOnSpinner2();
		addListenerOnButton();
		//addListenerOnSpinnerItemSelection();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_add_command, menu);
		return true;
	}

	public void addItemsOnSpinner2() {
		 
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		List<String> list = new ArrayList<String>();
		list.add("SAHIL");
		list.add("SNEH");
		list.add("YASH");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(dataAdapter);
	  }
	 /*
	  public void addListenerOnSpinnerItemSelection() {
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	  }
	 */
	  // get the selected dropdown list value
	  public void addListenerOnButton() {
	 
		//spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		button2 = (Button) findViewById(R.id.button2);
	 
		
		button2.setOnClickListener(new OnClickListener() {
	 
		  @Override
		  public void onClick(View v) {
	 
			  Toast.makeText(AddCommand.this,
						"OnClickListener : " +  
				                "\nSelected: "+ String.valueOf(spinner2.getSelectedItem()),
							Toast.LENGTH_SHORT).show();
					  }
				 
					});
	  }
}
