package org.mendybot.announcer.widgets.sound;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;

import org.mendybot.announcer.widgets.CommandWidget;

public class OmxPlayer extends CommandWidget implements SoundWidget, Runnable
{
  private ArrayBlockingQueue<File> queue = new ArrayBlockingQueue<File>(10);
  private Thread t = new Thread(this);
  public final File archive = new File("archive");
  private boolean running;

  public OmxPlayer() {
    t.setName(getClass().getSimpleName());
    t.start();
  }
  
  @Override
  public void run()
  {
    running = true;
    while(running) {
    try
    {
      File file = queue.take();
      play(file);
    }
    catch (InterruptedException e)
    {
      running = false;
      continue;
    }
    }
  }

  public boolean enqueue(String fileName)
  {
    File file = new File(archive, fileName+".mp3");
    if (file.exists()) {
      try
      {
        queue.put(file);
      }
      catch (InterruptedException e)
      {
        return false;
      }
      return true;
    } else {
      return false;
    }
    
  }
  
  private void play(File file)
  {
    String command = "omxplayer " + file.getPath();
    Runtime run = Runtime.getRuntime();
    try
    {
      Process proc = run.exec(command);
      proc.waitFor();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }

}
