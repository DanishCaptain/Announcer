package org.mendybot.announcer.widgets.sound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.widgets.CommandWidget;

public class APlayer extends CommandWidget implements SoundWidget, Runnable
{
  private static Logger LOG = Logger.getInstance(APlayer.class);
  private static APlayer singleton;
  private Thread t = new Thread(this);
  private boolean running;
  private PlayFile playFile;

  private APlayer()
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

  private void play(PlayFile pf)
  {
    synchronized (this)
    {
      String command = "aplay -c 2 " + pf.getFile().getPath();
      Runtime run = Runtime.getRuntime();
      try
      {
        LOG.logInfo("play", "calling for " + pf.getFile());
        Process proc = run.exec(command);
        LOG.logDebug("play", "starting for " + pf.getFile());
        proc.waitFor();
        LOG.logDebug("play", "ending for " + pf.getFile());
        BufferedReader is = new BufferedReader(new InputStreamReader((proc.getInputStream())));
        String line;
        while ((line = is.readLine()) != null)
        {
          LOG.logDebug("play", line);
        }
        is.close();
        is = new BufferedReader(new InputStreamReader((proc.getErrorStream())));
        while ((line = is.readLine()) != null)
        {
          LOG.logDebug("play", "E: " + line);
        }
        is.close();
      }
      catch (IOException e)
      {
        LOG.logSevere("play", e);
      }
      catch (InterruptedException e)
      {
        LOG.logInfo("play", e);
      }
    }
  }

  public synchronized static APlayer getInstance()
  {
    if (singleton == null)
    {
      singleton = new APlayer();
    }
    return singleton;
  }
}
