package com.doomonafireball.bemusicment;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;

public class BemusicmentActivity extends Activity {
	private TableLayout buttonContainerTL;
	private LinearLayout rightSideBarLL;
	private VerticalSeekBar pitchVSB;
	private Button optionsBTN;

	private Context mContext;
	private BemusicmentApplication application;

	private int mWindowWidth;
	private int mWindowHeight;
	private int mRightSideBarWidth;
	private int mButtonContainerWidth;
	private int mButtonContainerHeight;
	private int mButtonWidth;
	private int mButtonHeight;
	private int mNumCols;
	private int mNumRows;

	private LayoutParams mTableLayoutParams;
	private LinearLayout.LayoutParams mButtonLayoutParams;

	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mContext = this;
		application = ((BemusicmentApplication) getApplication());

		// Declare layout elements
		buttonContainerTL = (TableLayout) findViewById(R.id.TLButtonContainer);
		rightSideBarLL = (LinearLayout) findViewById(R.id.LLRightSideBar);
		pitchVSB = (VerticalSeekBar) findViewById(R.id.VSBRightSide);
		optionsBTN = (Button) findViewById(R.id.BTNOptions);

		// Get measurements of window
		mWindowWidth = getWindowManager().getDefaultDisplay().getWidth();
		mWindowHeight = getWindowManager().getDefaultDisplay().getHeight();
		mRightSideBarWidth = rightSideBarLL.getLayoutParams().width;
		mButtonContainerWidth = mWindowWidth - mRightSideBarWidth;
		mButtonContainerHeight = mWindowHeight;
		Log.d(BemusicmentApplication.TAG, "Window width: " + mWindowWidth
				+ ", Window height: " + mWindowHeight
				+ ", Right side bar width: " + mRightSideBarWidth
				+ ", Button container width: " + mButtonContainerWidth
				+ ", Button container height: " + mButtonContainerHeight);

		// Set up button container
		setUpButtonContainer();

		// Set up pitch SeekBar
		pitchVSB.setMax(100);
		pitchVSB.setProgress(50);
		pitchVSB.setOnSeekBarChangeListener(pitchSeekBarChangeListener);

		// Set up options Button
		optionsBTN.setOnClickListener(optionsClickListener);
	}

	private void setUpButtonContainer() {
		// Set up button container
		// Check for saved preferences on number of buttons
		mNumCols = application.getNumCols();
		mNumRows = application.getNumRows();
		Log.d(BemusicmentApplication.TAG, "Got cols: " + mNumCols
				+ " and rows: " + mNumRows);
		mButtonWidth = mButtonContainerWidth / mNumCols;
		mButtonHeight = mButtonContainerHeight / mNumRows;

		// Clear the table
		buttonContainerTL.removeAllViews();

		// Initialize the sounds
		initSounds();

		// Set up LayoutParams
		mTableLayoutParams = new TableLayout.LayoutParams(
				TableLayout.LayoutParams.WRAP_CONTENT,
				TableLayout.LayoutParams.WRAP_CONTENT);
		mButtonLayoutParams = new LinearLayout.LayoutParams(mButtonWidth,
				mButtonHeight);

		String[] soundTitles = application.getSoundTitles();
		int soundIndex = 0;
		for (int i = 0; i < mNumRows; i++) {
			TableRow thisRow = new TableRow(mContext);
			LinearLayout thisRowButtons = new LinearLayout(mContext);
			thisRowButtons.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = 0; j < mNumCols; j++) {
				Button thisButton = new Button(mContext);
				thisButton.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.aslctr_blue_button));
				thisButton.setLayoutParams(mButtonLayoutParams);
				createButtonClickListener(thisButton, soundIndex);
				if (soundIndex > (soundTitles.length - 1)) {
					thisButton.setText(getResources().getString(
							R.string.button_empty));
					thisButton.setTextColor(mContext.getResources().getColor(
							R.color.red));
					thisButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
				} else {
					thisButton.setText(soundTitles[soundIndex]);
					thisButton.setTextColor(mContext.getResources().getColor(
							R.color.black));
					thisButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
				}

				thisRowButtons.addView(thisButton);

				soundIndex++;
			}
			thisRow.addView(thisRowButtons);
			buttonContainerTL.addView(thisRow, mTableLayoutParams);
		}
	}

	private OnClickListener optionsClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Auto-generated method stub
			BemusicmentActivity.this.openOptionsMenu();
		}
	};

	private VerticalSeekBar.OnSeekBarChangeListener pitchSeekBarChangeListener = new VerticalSeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(
				com.doomonafireball.bemusicment.VerticalSeekBar seekBar,
				int progress, boolean fromUser) {
			// Auto-generated method stub
			Log.d(BemusicmentApplication.TAG, "onProgressChanged: " + progress);
		}

		@Override
		public void onStartTrackingTouch(
				com.doomonafireball.bemusicment.VerticalSeekBar seekBar) {
			// Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(
				com.doomonafireball.bemusicment.VerticalSeekBar seekBar) {
			// Auto-generated method stub

		}
	};

	private void createButtonClickListener(Button btn, final int soundIndex) {
		// Set sound for this button
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Auto-generated method stub
				playSound(soundIndex);
				Log.d(BemusicmentApplication.TAG, "Playing sound index: "
						+ soundIndex);
			}
		});
	}

	private void initSounds() {
		soundPool = new SoundPool((mNumRows * mNumCols),
				AudioManager.STREAM_MUSIC, 100);
		soundPoolMap = new HashMap<Integer, Integer>();
		if (application.getIsDefaultSounds()) {
			int[] defaultSoundPaths = application.getDefaultSoundPaths();
			for (int i = 0; i < defaultSoundPaths.length; i++) {
				soundPoolMap.put(i,
						soundPool.load(mContext, defaultSoundPaths[i], 1));
			}
		} else {
			String[] soundPaths = application.getSoundPaths();
			for (int i = 0; i < soundPaths.length; i++) {
				soundPoolMap.put(i, soundPool.load(soundPaths[i], 1));
				Log.d(BemusicmentApplication.TAG, "SoundPool loaded: "
						+ soundPaths[i]);
			}
		}
	}

	public void playSound(int sound) {
		// Calculate the current volume in a scale of 0.0 to 1.0
		AudioManager mgr = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;

		// Play the sound with the correct volume
		if (sound > (soundPoolMap.size() - 1)) {
			// Do nothing
		} else {
			soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);
		}
	}

	public void changeRowsCols() {
		// Show NumberPicker to change number
		final Dialog changeRowsColsDialog = new Dialog(mContext);

		changeRowsColsDialog.setContentView(R.layout.change_rows_cols_dialog);
		changeRowsColsDialog.setTitle("Change Rows and Columns");

		final NumberPicker rowsNP = (NumberPicker) changeRowsColsDialog
				.findViewById(R.id.NPRows);
		rowsNP.setValue(mNumRows);
		final NumberPicker colsNP = (NumberPicker) changeRowsColsDialog
				.findViewById(R.id.NPCols);
		colsNP.setValue(mNumCols);
		Button setBTN = (Button) changeRowsColsDialog.findViewById(R.id.BTNSet);
		Button cancelBTN = (Button) changeRowsColsDialog
				.findViewById(R.id.BTNCancel);
		setBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Persist to SharedPrefs
				application.setNumRows(rowsNP.getValue());
				application.setNumCols(colsNP.getValue());
				changeRowsColsDialog.dismiss();
				setUpButtonContainer();
			}
		});
		cancelBTN.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Auto-generated method stub
				changeRowsColsDialog.dismiss();
			}
		});
		changeRowsColsDialog.show();
	}

	public void editButtons() {
		// TODO Launch Edit Buttons Activity
	}

	public void about() {
		// TODO Launch About Activity/Dialog
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.changerowscols:
			changeRowsCols();
			return true;
		case R.id.editbuttons:
			editButtons();
			return true;
		case R.id.about:
			about();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}