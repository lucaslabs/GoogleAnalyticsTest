package com.lmn.gatest.tracker;

import android.content.Context;

import com.google.analytics.tracking.android.ExceptionReporter;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.StandardExceptionParser;
import com.google.analytics.tracking.android.Tracker;
import com.lmn.gatest.R;

/**
 * Custom Tracker for Google Analytics.
 *  
 * @author Lucas Nobile
 */
public class GoogleAnalyticsTrackerManager {

    private GoogleAnalytics mGa;
    private Tracker mTracker;
    private ExceptionReporter mUncaughtExceptionHandler;

    /*
     * Google Analytics configuration values.
     */

    // The period of time after all the collected data should be sent to the
    // server, in seconds.
    private static final int GA_DISPATCH_PERIOD = 30;

    // If set to true, prevent data from appearing in production reports. Useful for testing.
    private static final boolean GA_IS_DRY_RUN = true;

    // GA Logger verbosity. For debugging.
    private static final LogLevel GA_LOG_VERBOSITY = LogLevel.VERBOSE;

    public GoogleAnalyticsTrackerManager(Context context) {
        initGa(context);
    }

    public Tracker getTracker() {
        return mTracker;
    }

    /**
     * Initializes Google Analytics.
     */
    private void initGa(Context context) {
        mGa = GoogleAnalytics.getInstance(context);
        mTracker = mGa.getTracker(context.getString(R.string.ga_trackingId));

        /*
         * Set dispatch period.
         * 
         * NOTE: the setLocalDispatchPeriod method have been marked as
         * deprecated due to the forthcoming availability of Google Analytics as
         * part of Google Play Services.
         */
        GAServiceManager.getInstance().setLocalDispatchPeriod(GA_DISPATCH_PERIOD);

        // Set dryRun flag.
        mGa.setDryRun(GA_IS_DRY_RUN);

        // Set Logger verbosity.
        mGa.getLogger().setLogLevel(GA_LOG_VERBOSITY);

        /*
         * Create uncaught exception handler.
         * 
         * IMPORTANT: all exceptions sent using automatic exception measurement
         * are reported as fatal in Google Analytics. Also by default, the
         * description field is automatically set using the exception type,
         * class name, method name and thread name.
         */
        mUncaughtExceptionHandler = new ExceptionReporter(mTracker, GAServiceManager.getInstance(),
                Thread.getDefaultUncaughtExceptionHandler(), context);

        // Set default uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
    }

    /**
     * Tracks the starting of screens such as Activities and Fragments.
     *  <p>
     * Screen view data is used primarily in the following standard Google
     * Analytics reports:<br>
     * 1. Screens report<br>
     * 2. Engagement flow
     * 
     * @param screenName
     */
    public void trackScreenStart(String screenName) {
        mTracker.set(Fields.SCREEN_NAME, screenName);
        mTracker.send(MapBuilder.createAppView().build());
    }

    /**
     * Tracks caught Exceptions.
     * 
     * @param context
     * @param exception
     */
    public void trackException(Context context, String screenName, Throwable exception) {
        mTracker.set(Fields.SCREEN_NAME, screenName);
        mTracker.send(MapBuilder.createException(
        // Use this Exception parser to get meaningful Exception descriptions.
                new StandardExceptionParser(context, null).getDescription(
                // The name of the thread on which the exception occurred.
                        Thread.currentThread().getName(),
                        // The exception.
                        exception),
                // False indicates a non fatal exception
                false).build());
    }

    /**
     * Tracks an event from a screen.
     * 
     * @param screenName
     * @param category
     * @param action
     * @param label
     * @param value
     */
    public void trackEvent(String screenName, String category, String action, String label, Long value) {
        mTracker.set(Fields.SCREEN_NAME, screenName);
        mTracker.send(MapBuilder.createEvent(
        // Event category (required)
                category,
                // Event action (required)
                action,
                // Event label
                label,
                // Event value
                value).build());
    }

    /**
     * Track a custom dimension.<br>
     * 
     * Dimensions describe the data. Think of a dimension as describing the
     * "what", as in "what Android version do they use" or "what city is the
     * visitor from" or "what screen were viewed".<br>
     * Dimensions correspond to the rows in a report.
     * 
     * @param screenName
     * @param index
     * @param value
     */
    public void trackCustomDimension(String screenName, int index, String value) {
        mTracker.set(Fields.SCREEN_NAME, screenName);
        mTracker.send(MapBuilder.createAppView().set(Fields.customDimension(index), value).build());
    }

    /**
     * Track a custom metric.<br>
     * 
     * Metrics measure the data. Metrics are elements about a dimension that can
     * be measured. Think of a metric as answering "how many" or "how long", as
     * in "how many visits" or "how long a user was on the app".<br>
     * A metric corresponds to a column in a report.
     * 
     * @param screenName
     * @param index
     * @param value
     */
    public void trackCustomMetric(String screenName, int index, String value) {
        mTracker.set(Fields.SCREEN_NAME, screenName);
        mTracker.set(Fields.customMetric(index), value);
        mTracker.send(MapBuilder.createAppView().build());
    }
}
