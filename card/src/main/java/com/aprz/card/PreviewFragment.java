package com.aprz.card;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aprz.brouter.annotation.FragmentRoute;
import com.aprz.card.sdk.CardRouteUrl;

@FragmentRoute(path = CardRouteUrl.Fragment.PREVIEW)
public class PreviewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.card_fragment_preview, container, false);
    }
}
