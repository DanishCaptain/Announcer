package org.mendybot.announcer.widgets.display;

import java.io.File;

public class ImageFile
{
  private File file;
  private int repeat=1;
  private int ms=1;
  private int t=1;

  public ImageFile(File file)
  {
    this.file = file;
  }

  public File getFile()
  {
    return file;
  }

  public int getRepeat()
  {
    return repeat;
  }

  public void setRepeat(int repeat)
  {
    this.repeat = repeat;
  }

  public int getMs()
  {
    return ms;
  }

  public void setMs(int ms)
  {
    this.ms = ms;
  }

  public int getT()
  {
    return t;
  }

  public void setT(int t)
  {
    this.t = t;
  }

}
