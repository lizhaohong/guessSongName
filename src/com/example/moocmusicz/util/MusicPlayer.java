package com.example.moocmusicz.util;

import java.io.IOException;

import android.R.integer;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

public class MusicPlayer {
	
	public static MediaPlayer mMusicPlayer;
	public static MediaPlayer[] mMusicSpecialPlayer = new MediaPlayer[3];
	
	public static String[] specialSongName = new String [] {"cancel.mp3","enter.mp3","coin.mp3"};
	public static final int SPECIAL_CANCEL_SONG = 0;
	public static final int SPECIAL_ENTER_SONG = 1;
	public static final int SPECIAL_COIN_SONG = 2;
	
	
	public static void playSong(Context context ,String fileName) {
		if(mMusicPlayer == null) {
			mMusicPlayer = new MediaPlayer();
		}
		mMusicPlayer.reset();
		
		AssetManager assetManager = context.getAssets();
		try {
			AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
			mMusicPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
			
			mMusicPlayer.prepare();
			mMusicPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void playSong(Context context,int index) {
		if(mMusicSpecialPlayer[index] == null) {
			mMusicSpecialPlayer[index] = new MediaPlayer();
			
			AssetManager assetManager = context.getAssets();
			try {
				AssetFileDescriptor assetFileDescriptor = assetManager.openFd(specialSongName[index]);
				
				mMusicSpecialPlayer[index].setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
				
				mMusicSpecialPlayer[index].prepare();
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		mMusicSpecialPlayer[index].start();
	}
	
	public static void stop() {
		if(mMusicPlayer != null) {
			mMusicPlayer.stop();
		}
	}
}
