package org.mendybot.announcer.engine.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.mendybot.announcer.engine.fault.ExecuteException;
import org.mendybot.announcer.engine.log.Logger;

public class EngineModel
{
  private File archiveDir;

  public EngineModel()
  {
    Properties p = new Properties();
    try
    {
      Logger.init("/var/log/mendybot/", p);
    }
    catch (ExecuteException e)
    {
      e.printStackTrace();
    }
    archiveDir = new File("/opt/mendybot/announcer/archive");
  }

  public File getArchiveDirectory()
  {
    return archiveDir;
  }

  private void initLinux()
  {
    File osInfo = new File("/etc/os-release");
    try
    {
      FileReader fr = new FileReader(osInfo);
      Properties p = new Properties();
      p.load(fr);
      fr.close();
    }
    catch (FileNotFoundException e)
    {
      throw new RuntimeException(e);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

}
