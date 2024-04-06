package cn.charon.racegame.music;

import android.content.Context;
import android.media.MediaPlayer;

import cn.charon.racegame.R;


public class musicPlayer {
	private static Context context;
	private static MediaPlayer mediaPlayer;
	private static boolean flag=true;
	public static void init(Context c) {
		context=c;
		mediaPlayer=MediaPlayer.create(context, R.raw.bgm);
		mediaPlayer.setLooping(true);
	}
	public static void play(){
		mediaPlayer.start();
	}
	public static boolean getflag(){
		return flag;
	}
	public static void setFlag(boolean Flag) {
		if (Flag != flag) {
			if (Flag == true) {
				play();
				flag = Flag;
			} else {
				mediaPlayer.pause();
				mediaPlayer.seekTo(0);
				flag = Flag;
			}
		}
	}
	public static void ReleaseIt(){
		mediaPlayer.stop();
		mediaPlayer.release();
	}

}
