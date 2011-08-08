package com.doomonafireball.bemusicment;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class BemusicmentApplication extends Application {
	public static final String TAG = "Bemusicment";

	private static final String SETTINGS_PREFS_NAME = "BemusicmentPrefs";

	private SharedPreferences settings;
	private SharedPreferences.Editor settingsEditor;

	@Override
	public void onCreate() {
		super.onCreate();

		// Set up SharedPreferences
		settings = getSharedPreferences(SETTINGS_PREFS_NAME, 0);
		settingsEditor = settings.edit();
	}

	public int getNumRows() {
		return settings.getInt("num_rows", 2);
	}

	public void setNumRows(int i) {
		settingsEditor.putInt("num_rows", i);
		settingsEditor.commit();
		Log.d(TAG, "Put rows: " + i);
	}

	public int getNumCols() {
		return settings.getInt("num_cols", 3);
	}

	public void setNumCols(int i) {
		settingsEditor.putInt("num_cols", i);
		settingsEditor.commit();
		Log.d(TAG, "Put cols: " + i);
	}

	public boolean getIsDefaultSounds() {
		return settings.getBoolean("is_default_sounds", true);
	}

	public void setIsDefaultSounds(boolean b) {
		settingsEditor.putBoolean("is_default_sounds", b);
		settingsEditor.commit();
		Log.d(TAG, "Put boolean: " + b);
	}

	public int[] getDefaultSoundPaths() {
		int[] retArray = new int[6];
		// THESE NEED TO BE HARDCODED
		// CHANGE string-array default_sounds WITH THIS
		retArray[0] = R.raw.bass;
		retArray[1] = R.raw.wob;
		retArray[2] = R.raw.snare;
		retArray[3] = R.raw.bass;
		retArray[4] = R.raw.wob;
		retArray[5] = R.raw.snare;		
		return retArray;
	}

	public String[] getSoundPaths() {
		int soundPathLength = settings.getInt("soundpath_length", 0);
		if (soundPathLength > 0) {
			String[] retArray = new String[soundPathLength];
			for (int i = 0; i < soundPathLength; i++) {
				retArray[i] = settings.getString(
						"soundpath" + Integer.toString(i), "NULL");
				Log.d(TAG, "Added sound path: " + retArray[i]);
			}
			return retArray;
		} else {
			// TODO Handle error that should never happen but probably will?
		}
		return null;
	}

	public void setSoundPaths(String[] s) {
		settingsEditor.putInt("soundpath_length", s.length);
		settingsEditor.commit();
		Log.d(TAG, "Put soundpath_length: " + s.length);
		for (int i = 0; i < s.length; i++) {
			settingsEditor.putString("soundpath" + Integer.toString(i), s[i]);
			settingsEditor.commit();
			Log.d(TAG, "Put soundpath" + i + ": " + s[i]);
		}
	}
	
	public String[] getSoundTitles() {
		if (getIsDefaultSounds()) {
			return getResources().getStringArray(R.array.default_sounds);
		} else {
			int soundTitleLength = settings.getInt("soundtitle_length", 0);
			if (soundTitleLength > 0) {
				String[] retArray = new String[soundTitleLength];
				for (int i = 0; i < soundTitleLength; i++) {
					retArray[i] = settings.getString(
							"soundtitle" + Integer.toString(i), "NULL");
					Log.d(TAG, "Added sound title: " + retArray[i]);
				}
				return retArray;
			} else {
				// TODO Handle error that should never happen but probably will?
			}
		}
		return null;
	}
	
	public void setSoundTitles(String[] s) {
		settingsEditor.putInt("soundtitles_length", s.length);
		settingsEditor.commit();
		Log.d(TAG, "Put soundtitles_length: " + s.length);
		for (int i = 0; i < s.length; i++) {
			settingsEditor.putString("soundtitles" + Integer.toString(i), s[i]);
			settingsEditor.commit();
			Log.d(TAG, "Put soundtitles" + i + ": " + s[i]);
		}		
	}
}
