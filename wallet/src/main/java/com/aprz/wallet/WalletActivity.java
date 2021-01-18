package com.aprz.wallet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aprz.base.activity.BaseActivity;
import com.aprz.base.util.ToastUtils;
import com.aprz.base.viewlisteners.ListenerFactory;
import com.aprz.brouter.annotation.Route;
import com.aprz.brouter.api.core.BRouter;
import com.aprz.card.sdk.CardRouteUrl;
import com.aprz.wallet.adapter.FunctionEntryAdapter;
import com.aprz.wallet.model.FunctionEntry;
import com.aprz.wallet.sdk.WalletRouteUrl;

import java.util.ArrayList;
import java.util.List;

@Route(path = WalletRouteUrl.WALLET_ACTIVITY)
public class WalletActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity);

        findViewById(R.id.tool_bar).setOnClickListener(ListenerFactory.finish(this));

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

        crashDetector();
    }

    private void crashDetector() {
        postDelay(() -> {
            finish();
            ToastUtils.sShow(this, "欸嘿，页面挂了");
            CrashMonitor.pageCrashed(WalletRouteUrl.WALLET_ACTIVITY);
        }, 5000L);
    }

}
