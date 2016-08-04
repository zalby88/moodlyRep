package com.zalbyeco.albertolaz.moodly.service;

import java.lang.ref.WeakReference;
import android.os.Binder;

/**
 * Created by Alberto Lazzarin on 04/08/2016.
 */
public class LocalBinder<S> extends Binder {
    private final WeakReference<S> mService;

    public LocalBinder(final S service) {
        mService = new WeakReference<S>(service);
    }

    public S getService() {
        return mService.get();
    }

}
