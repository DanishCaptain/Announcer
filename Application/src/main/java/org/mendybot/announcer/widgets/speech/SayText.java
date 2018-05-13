package org.mendybot.announcer.widgets.speech;

public class SayText {
	private String text;
	private int repeat = 1;
	private int soundLevel;

	public SayText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public void setSoundLevel(int soundLevel) {
		this.soundLevel = soundLevel;
	}

	public int getSoundLevel() {
		return soundLevel;
	}
}
