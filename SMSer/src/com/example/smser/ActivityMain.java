package com.example.smser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityMain extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void spam(View v) {
        Intent intent = new Intent(ActivityMain.this, Spam.class);
        startActivity(intent);
    }

}
