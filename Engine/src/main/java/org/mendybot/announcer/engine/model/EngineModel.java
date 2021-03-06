package org.mendybot.announcer.engine.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.mendybot.announcer.engine.fault.ExecuteException;
import org.mendybot.announcer.engine.log.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EngineModel
{
//  @Autowired
//  private AppProperties appProperties;  
  
  @Value("${version}")
  private String version;
  @Value("${logging.home}")
  private String logHome;
  @Value("${archive.home}")
  private String archiveHome;
    
  public EngineModel()
  {
    Properties p = new Properties();
    try
    {
      Logger.init(logHome, p);
    }
    catch (ExecuteException e)
    {
      e.printStackTrace();
    }
  }

  public String getVersion()
  {
    return version;
  }

  public List<File> getSoundFiles()
  {
    ArrayList<File> list = new ArrayList<>();
    File archiveDir = new File(archiveHome);
    File[] files = archiveDir.listFiles();
    for (File f : files)
    {
      if (f.getName().endsWith(".wav")) {
        if (!f.getName().equals("say.wav")) {
          list.add(f);
        }
      }
    }
    return list;
  }

  public List<File> getImageFiles()
  {
    ArrayList<File> list = new ArrayList<>();
    File archiveDir = new File(archiveHome);
    File[] files = archiveDir.listFiles();
    for (File f : files)
    {
      if (f.getName().endsWith(".ppm")) {
        list.add(f);
      }
    }
    return list;
  }

}
