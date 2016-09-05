package com.slashyounger.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        float density = getResources().getDisplayMetrics().density;
        Toast.makeText(this, "density:" + density, Toast.LENGTH_LONG).show();
        System.out.println("density:" + density);
    }
}
