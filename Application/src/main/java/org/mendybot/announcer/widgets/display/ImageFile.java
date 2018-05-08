package org.mendybot.announcer.widgets.display;

import java.io.File;

public class ImageFile
{
  private File file;
  private int repeat=1;

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

}
