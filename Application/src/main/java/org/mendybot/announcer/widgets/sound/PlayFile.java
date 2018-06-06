package org.mendybot.announcer.widgets.sound;

import java.io.File;

public class PlayFile {
	private File file;
	private int repeat = 1;
	private int soundLevel=35;

	public PlayFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public int getSoundLevel() {
		return soundLevel;
	}

	public void setSoundLevel(int soundLevel) {
		this.soundLevel = soundLevel;
	}

}
