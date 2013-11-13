package com.lmn.gatest.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Set of usefun methods.
 * 
 * @author Lucas Nobile
 */
public class Util {

	/**
	 * Get device orientation.
	 * 
	 * @param context
	 * @return int representing orientation value.
	 */
	public static int getOrientation(Context context) {
		return context.getResources().getConfiguration().orientation;
	}

	/**
	 * Get device orientation name.
	 * 
	 * @param context
	 * @return string representing orientation name.
	 */
	public static String getOrientationName(Context context) {
		switch (getOrientation(context)) {
		case Configuration.ORIENTATION_PORTRAIT:
			return "portrait";
		case Configuration.ORIENTATION_LANDSCAPE:
			return "landscape";
		default:
			return "unknown";
		}
	}
}