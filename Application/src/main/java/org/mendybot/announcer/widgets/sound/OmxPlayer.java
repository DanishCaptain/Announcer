package org.mendybot.announcer.widgets.sound;

import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.tools.CommandTool;
import org.mendybot.announcer.widgets.CommandWidget;

public class OmxPlayer extends CommandWidget implements SoundWidget, Runnable
{
  private static Logger LOG = Logger.getInstance(OmxPlayer.class);
  private static OmxPlayer singleton;
  private Thread t = new Thread(this);
  private boolean running;
  private PlayFile playFile;
  private int soundLevel;

  private OmxPlayer()
  {
    t.setName(getClass().getSimpleName());
    t.setDaemon(true);
    t.start();
  }

  @Override
  public void run()
  {
    running = true;
    while (running)
    {
      try
      {
        synchronized(this) {
          wait();
        }
      }
      catch (InterruptedException e1)
      {
      }
      if (playFile != null)
      {
        play(playFile);
      }
    }
  }

  @Override
  public void submit(PlayFile file)
  {
    this.playFile = file;
    synchronized (this)
    {
      notifyAll();
    }
  }

  @Override
  public void checkSoundLevel(int soundLevel) {
    this.soundLevel = soundLevel;
  }

  private void play(PlayFile pf)
  {
    synchronized (this)
    {
      //TODO:  need to add sound level
      String command = "omxplayer " + pf.getFile().getPath();
      CommandTool.execute(pf.getFile().toString(), command);
    }
  }

  public synchronized static OmxPlayer getInstance()
  {
    if (singleton == null)
    {
      singleton = new OmxPlayer();
    }
    return singleton;
  }
}
