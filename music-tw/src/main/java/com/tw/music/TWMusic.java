package com.tw.music;

import android.graphics.Bitmap;
import android.tw.john.TWUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.Locale;

public class TWMusic extends TWUtil {
	private static TWMusic mTW = new TWMusic();
	private static int mCount = 0;
	public static TWMusic open() {
		if(mCount++ == 0) {
	        if(mTW.open(new short[] { (short)0x9e1f}) != 0) {
	        	mCount--;
	        	return null;
	        }
	        mTW.start();
	        mTW.resume();
	        mTW.mPlaylistRecord = new Record("Playlist", 0, 0);
	        mTW.loadFile(mTW.mPlaylistRecord, mTW.mCurrentPath);
	        mTW.toRPlaylist(mTW.mCurrentIndex);
		}
		return mTW;
	}

	public void close() {
		if(mCount > 0) {
			if(--mCount == 0) {
				stop();
				super.close();
			}
		}
	}

	public static final int REQUEST_SOURCE = 0x9e11;
	public static final int REQUEST_MEDIA = 0x0502;
	public static final int REQUEST_SERVICE = 0x9e00;
	public static final int RETURN_MOUNT = 0x9e1f;

	public void requestSource(int source) {
		write(REQUEST_SOURCE, (1<<7) | (1<<6), source);
	}

	public void requestSource(boolean is) {
		requestSource(is ? 0x03 : 0x83);
	}

	public static final int ACTIVITY_RUSEME = 0x03;
	public static final int ACTIVITY_PAUSE = 0x83;

	private int mService = 0;
	public void requestService(int activity) {
		mService = activity;
		write(REQUEST_SERVICE, activity);
	}

	public int getService() {
		return mService;
	}

	public void media(int type, int cindex, int tindex, int ctime, int percent) {
		write(REQUEST_MEDIA, (tindex<<16) | (cindex & 0xffff), (type<<31) | ((percent & 0x7f)<<24) | (ctime & 0xffffff));
	}

	public Record mPlaylistRecord;
    public int[] mRPlaylist;
	public int mCurrentRIndex;

    public String mCurrentAPath;
	public String mCurrentPath;
	public int mCurrentIndex;
	public int mCurrentPos;
	public int mShuffle;
	public int mRepeat = 1;

	public String mCurrentArtist;
	public String mCurrentAlbum;
	public String mCurrentSong;
	public Bitmap mAlbumArt;

	private void resume() {
        try {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader("/data/tw/music"));
				mCurrentAPath = br.readLine();
				mCurrentIndex = Integer.valueOf(br.readLine());
				mCurrentPos = Integer.valueOf(br.readLine());
				mShuffle = Integer.valueOf(br.readLine());
				mRepeat = Integer.valueOf(br.readLine());
			} catch (Exception e) {
			} finally {
				if(br != null) {
					br.close();
					br = null;
				}
			}
	        if(mRepeat < 1) {
	            mRepeat = 1;
	        }
	        if(mCurrentAPath != null) {
	            mCurrentPath = mCurrentAPath.substring(0, mCurrentAPath.lastIndexOf("/"));
	        }
		} catch (Exception e) {
		}
    }

    public void loadFile(Record record, String path) {
    	if ((record != null) && (path != null)) {
    		File[] file = new File(path).listFiles(new FileFilter() {
				@Override
				public boolean accept(File f) {
					String n = f.getName().toUpperCase(Locale.ENGLISH);
					if (f.isFile() && !n.startsWith(".") &&
							(n.endsWith(".MP3") ||
									n.endsWith(".WMA") ||
									n.endsWith(".AAC") ||
									n.endsWith(".OGG") ||
									n.endsWith(".PCM") ||
									n.endsWith(".M4A") ||
									n.endsWith(".AC3") ||
									n.endsWith(".EC3") ||
									n.endsWith(".DTSHD") ||
									n.endsWith(".MKA") ||
									n.endsWith(".RA") ||
									n.endsWith(".WAV") ||
									n.endsWith(".CD") ||
									n.endsWith(".AMR") ||
									n.endsWith(".MP2") ||
									n.endsWith(".APE") ||
									n.endsWith(".DTS") ||
									n.endsWith(".FLAC") ||
									n.endsWith(".MIDI") ||
									n.endsWith(".MID") ||
									n.endsWith(".MPC") ||
									n.endsWith(".TTA") ||
									n.endsWith(".ASX") ||
									n.endsWith(".AIFF") ||
									n.endsWith(".AU"))) {
						return true;
					}
					return false;
				}
			});
    		record.mCLength = 0;
    		if(file != null) {
        		record.setLength(file.length);
        		for(File f : file) {
        			String n = f.getName();
        			record.add(n.substring(0, n.lastIndexOf(".")), f.getAbsolutePath());
        		}
    		}
    	}
    }

    private int getPRandom(int index, int length) {
    	int p;
		int j;
    	while(((p = (int)(Math.random() * length)) == 0) && (index == 0));
		for(j = p; j < length; j++) {
			if(mRPlaylist[j] == 0) {
				break;
			}
		}
		if(j == length) {
			for(j = 1; j < p; j++) {
				if(mRPlaylist[j] == 0) {
					break;
				}
			}
		}
		return j;
    }

    public void toRPlaylist(int index) {
    	mRPlaylist = null;
    	mCurrentRIndex = 0;
    	if(mPlaylistRecord != null) {
    		int length = mPlaylistRecord.mCLength;
    		if(length > 0) {
        		mRPlaylist = new int [length];
        		if(index >= length) {
        			index = 0;
        		}
        		mRPlaylist[0] = index;
        		if(length > 1) {
	    			for(int i = index + 1; i < length; i++) {
	    				int p = i - index;
	    				if(mShuffle != 0) {
	    					p = getPRandom(index, length);
	    				}
	    				mRPlaylist[p] = i;
	    			}
	    			for(int i = 0; i < index; i++) {
	    				int p = i + length - index;
	    				if(mShuffle != 0) {
	    					p = getPRandom(index, length);
	    				}
	    				mRPlaylist[p] = i;
	    			}
        		}
    		}
    	}
    }
}
