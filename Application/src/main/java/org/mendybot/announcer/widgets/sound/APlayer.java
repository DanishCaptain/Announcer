package org.mendybot.announcer.widgets.sound;

import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.tools.CommandTool;
import org.mendybot.announcer.widgets.CommandWidget;

public class APlayer extends CommandWidget implements SoundWidget, Runnable {
	private static Logger LOG = Logger.getInstance(APlayer.class);
	private static APlayer singleton;
	private Thread t = new Thread(this);
	private boolean running;
	private PlayFile playFile;
	private int soundLevel;

	private APlayer() {
		t.setName(getClass().getSimpleName());
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e1) {
			}
			if (playFile != null) {
				play(playFile);
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void submit(PlayFile file) {
		this.playFile = file;
		synchronized (this) {
			notifyAll();
		}
	}

	@Override
	public void checkSoundLevel(int soundLevel) {
		if (this.soundLevel != soundLevel) {
			String command = "amixer -c 1 sset Speaker,0 " + soundLevel + "%";
			CommandTool.execute("soundLevel:" + soundLevel, command);
			this.soundLevel = soundLevel;
		}
	}

	private void play(PlayFile pf) {
		synchronized (this) {
			String command = "aplay -c 2 " + pf.getFile().getPath();
			CommandTool.execute(pf.getFile().toString(), command);
		}
	}

	public synchronized static APlayer getInstance() {
		if (singleton == null) {
			singleton = new APlayer();
		}
		return singleton;
	}
}
