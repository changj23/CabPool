package com.a04.cabpool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseObject;


public class RequestInProgressGUI extends AbstractGUIActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
/*        Intent intent = getIntent();
        ParseProxyObject offerProxy = (ParseProxyObject) intent.getSerializableExtra("offer");
        ParseObject offer = offerProxy.getObject();
        
        Toast.makeText(this, offer.getObjectId(), Toast.LENGTH_SHORT).show();*/
        
    }
}
