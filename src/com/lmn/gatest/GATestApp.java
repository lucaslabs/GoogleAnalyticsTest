package com.lmn.gatest;

import com.lmn.gatest.tracker.GoogleAnalyticsTrackerManager;

import android.app.Application;

/**
 * Main entry poin of this app.
 * 
 * @author Lucas Nobile
 */
public class GATestApp extends Application {

    public static GoogleAnalyticsTrackerManager TRACKER_MANAGER;

    @Override
    public void onCreate() {
        super.onCreate();
        TRACKER_MANAGER = new GoogleAnalyticsTrackerManager(this);
    }
}