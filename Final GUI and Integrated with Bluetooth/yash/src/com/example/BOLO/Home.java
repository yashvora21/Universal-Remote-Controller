package com.example.BOLO;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
public class Home extends Activity implements OnClickListener {
	public Bluetooth bt;
	ListView lv;
	static final int check=111;
	
	public static final int ADD_DEVICE = 1;
	public static final int DEVICES = 0;
	private static final int COMMAND = 0;
	protected MyApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		bt=Bluetooth.getInstance();
		lv = (ListView)findViewById(R.id.lvVoiceReturn);
		Button b = (Button)findViewById(R.id.bVoice);
		b.setOnClickListener((OnClickListener) this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Now!!!");
		startActivityForResult(i, check);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == check && resultCode == RESULT_OK ){
		ArrayList<String> results =data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		 lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results));


			DatabaseConnector dc=new DatabaseConnector(Home.this);
		 String name="",cname="";
		 int no=0;
		 boolean f=false;
		 loop:for (String s : results)
		 {
			 String dev[]=dc.getAllDevices();
			 for(int i=0;i<dev.length;i++)
			 if(s.toLowerCase().indexOf(dev[i].toLowerCase())!=-1)
			 {
				 name=dev[i];
				 break;
			 }
			 
			 for(int j=0;j<s.length() && !f;j++)
				 {
					 if(s.charAt(j)>='0' && s.charAt(j)<='9')
					 {
						 Log.e("No :",s.charAt(j)+"");
						 f=true;
						no=no*10+s.charAt(j)-'0'; 
					 }
				 }
					 
			 String cmnd[]=dc.getCommand(name);
			 for(int i=0;i<cmnd.length;i++)
			 if(s.toLowerCase().indexOf(cmnd[i].toLowerCase())!=-1)
			 {
				 cname=cmnd[i];
				 break loop;
			 }
		 }
		  Toast.makeText(Home.this,"cname : " + cname+"\nname : " + name+"\nno : " + no,
						Toast.LENGTH_SHORT).show();
		 String cdata[]=dc.getCommandData(cname, name);
		  bt.SendDataToBluetooth(0);
		  bt.SendDataToBluetooth("|");
		  bt.SendDataToBluetooth(cdata[0]);
		  bt.SendDataToBluetooth("|");
		  bt.SendDataToBluetooth(cdata[1]);
		  bt.SendDataToBluetooth("|");
		  bt.SendDataToBluetooth(cdata[2]);
		  bt.SendDataToBluetooth("|");
		  bt.SendDataToBluetooth(no);
		  bt.SendDataToBluetooth("|");

		}
		
		
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_home, menu);
	
		    //menu.add(0, DEVICES, 0, "List of Devices");
	        menu.add(0, COMMAND, 0, "Add/Edit Commands");
	        menu.add(0, ADD_DEVICE, 0, "Add Device");
//	        menu.add(0, DEL_DEVICE, 0, "Delete all device Device");
	        
	        return true;
	}
	public boolean onOptionsItemSelected(MenuItem item){
		
		switch(item.getItemId())
		{
		/*case DEVICES:
		{
		Intent startSuccess1=new Intent(Home.this , Command.class);
			Home.this.startActivity(startSuccess1);
			break;
		
		}	
		*/
		case COMMAND:
		{
		Intent startSuccess1=new Intent(Home.this , Command.class);
			Home.this.startActivity(startSuccess1);
			break;
		
		}	
		
		
		case ADD_DEVICE:
		{
		Intent startSuccess=new Intent(Home.this , AddDevice.class);
			Home.this.startActivity(startSuccess);
		break;
		}	
		}
		
		return false;
	}

}
