package com.aprz.wallet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aprz.multitypeknife.annotation.ItemBinder;
import com.aprz.multitypeknife.annotation.ItemLayoutId;
import com.aprz.multitypeknife.api.BaseViewHolder;
import com.aprz.wallet.R;
import com.aprz.wallet.model.FunctionEntry;

import me.drakeet.multitype.MultiTypeAdapter;

public class FunctionEntryAdapter extends MultiTypeAdapter {

    public FunctionEntryAdapter() {
        register(FunctionEntry.class, new FunctionEntryItemBinder());
    }

    @ItemBinder(name = "FunctionEntryItemBinder")
    static class FunctionEntryViewHolder extends BaseViewHolder<FunctionEntry> {

        @ItemLayoutId
        public static int layoutId = R.layout.wallet_item_wallet_function;

        private final TextView desc;
        private final ImageView icon;

        public FunctionEntryViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.credit_card_desc);
            icon = itemView.findViewById(R.id.icon_credit_card);
        }

        @Override
        public void bindView(FunctionEntry entry) {
            desc.setText(entry.getStringId());
            icon.setImageResource(entry.getIconId());

            bindEvents(entry);
        }

        private void bindEvents(FunctionEntry entry) {
            itemView.setOnClickListener(v -> entry.getClickRunnable().run());
        }
    }
}
