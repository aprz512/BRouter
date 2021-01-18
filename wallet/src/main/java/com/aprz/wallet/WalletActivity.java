package com.aprz.wallet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aprz.brouter.annotation.Route;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.card.sdk.CardRouteUrl;
import com.aprz.wallet.adapter.FunctionEntryAdapter;
import com.aprz.wallet.model.FunctionEntry;
import com.aprz.wallet.sdk.WalletRouteUrl;

import java.util.ArrayList;
import java.util.List;

@Route(path = WalletRouteUrl.WALLET_ACTIVITY)
public class WalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity);

        // data
        List<FunctionEntry> functionEntryList = new ArrayList<>();
        FunctionEntry function = new FunctionEntry(
                R.drawable.wallet_icon_credit_card,
                R.string.wallet_credit_card,
                () -> BRouter.getInstance().path(CardRouteUrl.CARD_ACTIVITY).navigate(this));
        functionEntryList.add(function);

        // adapter
        FunctionEntryAdapter adapter = new FunctionEntryAdapter();

        // recyclerView
        RecyclerView recyclerView = findViewById(R.id.icons);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        adapter.setItems(functionEntryList);
        adapter.notifyDataSetChanged();
    }

}
