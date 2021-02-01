package com.aprz.card;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aprz.base.viewlisteners.ListenerFactory;
import com.aprz.brouter.annotation.Route;
import com.aprz.card.sdk.CardRouteUrl;

@Route(path = CardRouteUrl.Activity.MAIN)
public class CardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity_card);
        findViewById(R.id.tool_bar).setOnClickListener(ListenerFactory.finish(this));
    }

}
