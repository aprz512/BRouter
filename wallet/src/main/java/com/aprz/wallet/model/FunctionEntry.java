package com.aprz.wallet.model;

public class FunctionEntry {

    private final int iconId;
    private final int stringId;
    private final Runnable clickRunnable;

    public FunctionEntry(int iconId, int stringId) {
        this(iconId, stringId, () -> {
            // empty
        });
    }

    public FunctionEntry(int iconId, int stringId, Runnable clickRunnable) {
        this.iconId = iconId;
        this.stringId = stringId;
        this.clickRunnable = clickRunnable;
    }

    public int getIconId() {
        return iconId;
    }

    public int getStringId() {
        return stringId;
    }

    public Runnable getClickRunnable() {
        return clickRunnable;
    }
}
