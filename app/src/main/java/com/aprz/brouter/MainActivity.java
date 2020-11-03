package com.aprz.brouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.aprz.brouter_annotation.Route;
import com.aprz.brouter_api.core.BRouter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.navigate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().navigate("wallet/main");
            }
        });
    }
}