package org.mendybot.announcer.widgets.sound;

public interface SoundWidget {

	void submit(PlayFile file);

	void checkSoundLevel(int soundLevel);

}
