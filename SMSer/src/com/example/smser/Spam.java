package com.example.smser;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Spam extends Activity 
{
	Button btnSendSMS;
	EditText txtPhoneNo;
	EditText txtMessage;
	TextView counter;
	SeekBar countSlider;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spam);   
        
        counter = (TextView) findViewById(R.id.counter);
        countSlider = (SeekBar) findViewById(R.id.countSlider);
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        
        countSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				if(arg1 == 0)
					countSlider.setProgress(1);
				counter.setText("Anzahl: " + String.valueOf(countSlider.getProgress()));
				
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {	
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				
			}
        	
        });
                
        btnSendSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {            	
            	String phoneNo = txtPhoneNo.getText().toString();
            	String message = txtMessage.getText().toString();             	
                if (phoneNo.length() >0 && message.length() >0)
                {
                	btnSendSMS.setEnabled(false);
                	countSlider.setEnabled(false);
                	txtPhoneNo.setEnabled(false);
                	txtMessage.setEnabled(false);
                	sendSMS(phoneNo, message, countSlider.getProgress());
                	 
                }
                               
                else
                	Toast.makeText(getBaseContext(), "Nummer und Nachricht eintragen!", Toast.LENGTH_SHORT).show();
            }
        });        
    }
    

    private void sendSMS(String phoneNumber, String message, int count)
    {      
    	String SENT = "SMS_SENT";
    	String DELIVERED = "SMS_DELIVERED";
    	
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
    	

        registerReceiver(new BroadcastReceiver(){

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					    Toast.makeText(getBaseContext(), "Generic failure", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NO_SERVICE:
					    Toast.makeText(getBaseContext(), "No service", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_NULL_PDU:
					    Toast.makeText(getBaseContext(), "Null PDU", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case SmsManager.RESULT_ERROR_RADIO_OFF:
					    Toast.makeText(getBaseContext(), "Radio off", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				}
			}
        }, new IntentFilter(SENT));
        
        registerReceiver(new BroadcastReceiver(){
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode())
				{
				    case Activity.RESULT_OK:
					    Toast.makeText(getBaseContext(), "SMS delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;
				    case Activity.RESULT_CANCELED:
					    Toast.makeText(getBaseContext(), "SMS not delivered", 
					    		Toast.LENGTH_SHORT).show();
					    break;					    
				}
			}
        }, new IntentFilter(DELIVERED));        
    	
        SmsManager sms = SmsManager.getDefault();
        for(int i = 0;i < count;i++)
        {
        	message.replaceAll("<n>", String.valueOf(i));
        	sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        }
        	
        
    }    
}