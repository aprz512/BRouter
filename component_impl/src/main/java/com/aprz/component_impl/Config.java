package com.aprz.component_impl;

import android.app.Application;

import androidx.annotation.NonNull;

public class Config {
    @NonNull
    private Application application;

    private Config(@NonNull Builder builder) {
        this.application = builder.application;
    }

    @NonNull
    public static Builder with(@NonNull Application application) {
        return new Builder(application);
    }

    @NonNull
    public Application getApplication() {
        return application;
    }

    public static class Builder {

        private Application application;

        public Builder(@NonNull Application application) {
            this.application = application;
        }

        @NonNull
        public Config build() {
            Config config = new Config(this);
            return config;
        }
    }
}
