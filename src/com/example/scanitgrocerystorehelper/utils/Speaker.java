package com.example.scanitgrocerystorehelper.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class Speaker implements OnInitListener {
	private Context mContext;
	private TextToSpeech mTTS;
	private boolean ready;

	public Speaker(Context context) {
		mContext = context;
		mTTS = new TextToSpeech(context, this);
		ready = false;
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			mTTS.setLanguage(mContext.getResources().getConfiguration().locale);
			ready = true;
		} else {
			ready = false;
		}
	}

	public void speak(String text) {
		if (ready) {
			mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}
	
	public void destroy(){
		mTTS.shutdown();
		mTTS = null;
		ready = false;
	}

}
