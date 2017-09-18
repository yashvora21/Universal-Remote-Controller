package com.example.BOLO;


import com.example.BOLO.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity{
	protected MyApplication app;

	    Button next;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        next = (Button)findViewById(R.id.button3);
        next.setOnClickListener(btnNextListener);

		app = (MyApplication)getApplication();


	
        
    }
       
    
    private OnClickListener btnNextListener = new OnClickListener() {
        public void onClick(View v){
        	onDestroy();
        	Intent startSuccess=new Intent(MainActivity.this, Home.class);
    		MainActivity.this.startActivity(startSuccess);
            }
    };
    
    
}
    
    
    
    
    
