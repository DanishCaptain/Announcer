package org.mendybot.announcer.widgets.display;

import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.tools.CommandTool;
import org.mendybot.announcer.widgets.CommandWidget;

public class ScrollingTextPlayer extends CommandWidget implements MatrixDisplayWidget, Runnable
{
  private static Logger LOG = Logger.getInstance(ScrollingTextPlayer.class);
  private static ScrollingTextPlayer singleton;
  private Thread t = new Thread(this);
  private boolean running;
  private String commandBase = "sudo /opt/rpi-rgb-led-matrix/examples-api-use/scrolling-text-example";
  private DisplayText displayText;

  private ScrollingTextPlayer()
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
      if (displayText != null)
      {
        play(displayText);
      }
    }
  }

  @Override
  public void show(DisplayText text)
  {
    this.displayText = text;
    synchronized (this)
    {
      notifyAll();
    }
  }

  private void play(DisplayText dt)
  {
    synchronized (this)
    {
      String command = createCommand(dt.getFont(), dt.getRGB(), dt.getRepeat());
      CommandTool.execute("for " + displayText, command+" "+dt.getText());
    }
  }

  private String createCommand(String font, String rgb, int repeat)
  {
    StringBuilder command = new StringBuilder(commandBase);
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
//    command.append(" --led-no-hardware-pulse");
    command.append(" -f /opt/rpi-rgb-led-matrix/fonts/"+font+".bdf");
    command.append(" --led-rows=32");
    command.append(" --led-cols=32");
    command.append(" --led-chain=4");
    command.append(" -C "+rgb);
    command.append(" -b 50");
    command.append(" -l "+repeat);
    return command.toString();
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
  public void show(ImageFile file) { throw new RuntimeException("not implemented");
  }

  @Override
  public void show(Effect effect) { throw new RuntimeException("not implemented");
  }

}
