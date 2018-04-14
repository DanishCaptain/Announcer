package org.mendybot.announcer.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class AnnouncerModel
{
  private AnnouncerOs os;

  public AnnouncerModel()
  {
    String osType = System.getProperty("os.name");
    if ("linux".equalsIgnoreCase(osType))
    {
      initLinux();
    }
    else
    {
      throw new RuntimeException("Unknown OS Type: "+osType);
    }
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
      String osName = p.getProperty("ID");
      if ("raspbian".equalsIgnoreCase(osName))
      {
        initRaspbian();
      }
      else if ("ubuntu".equalsIgnoreCase(osName))
      {
        initUbunto();
      }
      else
      {
        throw new RuntimeException("Unknown OS ID: "+osName);
      }
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

  private void initRaspbian()
  {
    os = AnnouncerOs.RASPBIAN;
  }

  private void initUbunto()
  {
    os = AnnouncerOs.UBUNTO;
  }

  public AnnouncerOs getOs()
  {
    return os;
  }

}
