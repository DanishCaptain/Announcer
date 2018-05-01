package org.mendybot.announcer.widgets.sound;

import java.io.BufferedReader;
import java.io.File;
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
  private File file;

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
      if (file != null)
      {
        play(file);
      }
    }
  }

  @Override
  public void submit(File file)
  {
    this.file = file;
    synchronized (this)
    {
      notifyAll();
    }
  }

  private void play(File file)
  {
    synchronized (this)
    {
      String command = "aplay -c 2 " + file.getPath();
      Runtime run = Runtime.getRuntime();
      try
      {
        LOG.logInfo("play", "calling for " + file);
        Process proc = run.exec(command);
        LOG.logInfo("play", "starting for " + file);
        proc.waitFor();
        LOG.logInfo("play", "ending for " + file);
        BufferedReader is = new BufferedReader(new InputStreamReader((proc.getInputStream())));
        String line;
        while ((line = is.readLine()) != null)
        {
          LOG.logSevere("play", line);
        }
        is.close();
        is = new BufferedReader(new InputStreamReader((proc.getErrorStream())));
        while ((line = is.readLine()) != null)
        {
          LOG.logSevere("play", "E: " + line);
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
