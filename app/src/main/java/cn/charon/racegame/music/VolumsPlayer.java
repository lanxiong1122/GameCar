package cn.charon.racegame.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.rtp.AudioStream;
import android.util.Log;


import java.util.HashMap;
import java.util.Map;

import cn.charon.racegame.R;

public class VolumsPlayer {
	private static Context context;
	private static SoundPool soundPool;
	private static Map<Integer, Integer> soundMap;
	private static boolean flag = true;
	public static void init(Context c) {
		context = c;
		soundPool = new SoundPool(10, AudioStream.MODE_NORMAL, 0);
		soundMap = new HashMap<Integer, Integer>();
		soundMap.put(R.raw.gang1, soundPool.load(context, R.raw.gang1, 1));
		soundMap.put(R.raw.gang2, soundPool.load(context, R.raw.gang2, 1));
		soundMap.put(R.raw.hit, soundPool.load(context, R.raw.hit, 1));
		//这里的音效资源比较奇怪 经过测试是由通话音量控制的
		AudioManager mAudioManager = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		int max= mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);//音量最大值
		Log.w("call_maxvalue",max+"-");
		mAudioManager.setStreamVolume( AudioManager.STREAM_VOICE_CALL ,1,0); //音乐音量
	}

	public static void playit(Integer i) {
		if (!flag) {return; }
		else {
			Integer id = soundMap.get(i);
			if (id != null) {
				soundPool.setVolume(soundPool.play(id, 1, 1, 1, 0, 1), (float)0.5, (float)0.5);
			}
		}
	}
	public static void setFlag(boolean Flag) {
		flag = Flag;
	}
	public static boolean getflag(){
		return flag;
	}
	public static void ReleaseIt() {
		soundPool.release();
	}

}
