package org.mendybot.announcer.widgets.display;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.widgets.CommandWidget;

public class ScrollingTextPlayer extends CommandWidget implements MatrixDisplayWidget, Runnable
{
  private static Logger LOG = Logger.getInstance(ScrollingTextPlayer.class);
  private static ScrollingTextPlayer singleton;
  private Thread t = new Thread(this);
  private boolean running;
  private StringBuilder command = new StringBuilder(
      "sudo /opt/rpi-rgb-led-matrix/examples-api-use/scrolling-text-example");
  private String text;

  private ScrollingTextPlayer()
  {
    /*
     * "\t-s <speed>        : Approximate letters per second.\n"
     * "\t-l <loop-count>   : Number of loops through the text. "
     * "-1 for endless (default)\n" "\t-f <font-file>    : Use given font.\n"
     * "\t-b <brightness>   : Sets brightness percent. Default: 100.\n"
     * "\t-x <x-origin>     : X-Origin of displaying text (Default: 0)\n"
     * "\t-y <y-origin>     : Y-Origin of displaying text (Default: 0)\n"
     * "\t-S <spacing>      : Spacing pixels between letters (Default: 0)\n"
     * "\n" "\t-C <r,g,b>        : Color. Default 255,255,0\n"
     * "\t-B <r,g,b>        : Background-Color. Default 0,0,0\n"
     * "\t-O <r,g,b>        : Outline-Color, e.g. to increase contrast.\n"
     */
    command.append(" --led-no-hardware-pulse");
    command.append(" -f /opt/rpi-rgb-led-matrix/fonts/10x20.bdf");
    command.append(" --led-rows=32");
    command.append(" --led-cols=32");
    command.append(" --led-chain=4");
    command.append(" -b 50");
    command.append(" -l 1");

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
      if (text != null)
      {
        play(text);
      }
    }
  }

  @Override
  public void show(String text)
  {
    this.text = text;
    synchronized (this)
    {
      notifyAll();
    }
  }

  private void play(String text)
  {
    synchronized (this)
    {
      Runtime run = Runtime.getRuntime();
      try
      {
        LOG.logInfo("play", "calling for " + text);
        Process proc = run.exec(command + " " + text);
        proc.waitFor();
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

  public synchronized static ScrollingTextPlayer getInstance()
  {
    if (singleton == null)
    {
      singleton = new ScrollingTextPlayer();
    }
    return singleton;
  }

  @Override
  public void show(File file)
  {
    throw new RuntimeException("not implemented");
  }
}
