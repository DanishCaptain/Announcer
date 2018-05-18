package org.mendybot.announcer.engine.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.mendybot.announcer.common.Resource;
import org.mendybot.announcer.common.ResourceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EngineModel
{
//  @Autowired
//  private AppProperties appProperties;  
  private HashMap<ResourceType, List<Resource>> rMap = new HashMap<>();
  
  @Value("${version}")
  private String version;
  @Value("${archive.home}")
  private String archiveHome;

  private boolean initialized;

  public EngineModel()
  {
    Properties p = new Properties();
    p.put("log-mode", "NORMAL");
    p.put("log-level", "INFO");
  }

  private void initFiles()
  {
    File archiveDir = new File(archiveHome);
    File[] files = archiveDir.listFiles();
    for (File f : files)
    {
      if (f.getName().endsWith(".wav")) {
        if (!f.getName().equals("say.wav")) {
          addResorce(ResourceType.SOUND, f);
        }
      }
      else if (f.getName().endsWith(".ppm")) {
        addResorce(ResourceType.IMAGE, f);
      }
    }
  }

  private synchronized void addResorce(ResourceType type, File file)
  {
    Resource r = new Resource(type, UUID.randomUUID());
    r.setFile(file);
    List<Resource> list = rMap.get(type);
    if (list == null)
    {
      list = new ArrayList<>();
      rMap.put(type, list);
    }
    list.add(r);
  }

  public String getVersion()
  {
    return version;
  }

  public synchronized List<Resource> getSoundFiles()
  {
    if (!initialized) {
      initFiles();
      initialized = true;
    }
    List<Resource> list = rMap.get(ResourceType.SOUND);
    if (list == null) {
      list = new ArrayList<>();
      rMap.put(ResourceType.SOUND, list);
    }
    return list;
  }

  public synchronized List<Resource> getImageFiles()
  {
    if (!initialized) {
      initFiles();
      initialized = true;
    }
    List<Resource> list = rMap.get(ResourceType.IMAGE);
    if (list == null) {
      list = new ArrayList<>();
      rMap.put(ResourceType.IMAGE, list);
    }
    return list;
  }

}
