package org.mendybot.announcer.engine.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;

import org.mendybot.announcer.common.Resource;
import org.mendybot.announcer.common.ResourceType;
import org.mendybot.announcer.common.model.dto.Announcement;
import org.mendybot.announcer.common.model.dto.SyncRequest;
import org.mendybot.announcer.log.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EngineModel
{
//  @Autowired
//  private AppProperties appProperties;  
  private HashMap<ResourceType, List<Resource>> rMap = new HashMap<>();
  private HashMap<String, SyncRequest> cubesNamesMap = new HashMap<>();
  private HashMap<String, Long> cubesTimesMap = new HashMap<>();
  private HashMap<String, ArrayList<Announcement>> announcementsMap = new HashMap<>();
  private ArrayList<Announcement> announcementsList = new ArrayList<>();

  @Value("${version}")
  private String version;
  @Value("${archive.home}")
  private String archiveHome;

  private boolean initialized;
  private File archiveDir;

  public EngineModel()
  {
    Properties p = new Properties();
    p.put("log-mode", "NORMAL");
    p.put("log-level", "INFO");
  }

  private void initFiles()
  {
    archiveDir = new File(archiveHome);
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

  public void handle(SyncRequest request)
  {
    cubesNamesMap.put(request.getCube().getName(), request);
    cubesTimesMap.put(request.getCube().getName(), System.currentTimeMillis());
//    System.out.println("-->"+request);
//    System.out.println("i--->"+request.getCube().getArchive().getImageFiles());
//    System.out.println("s--->"+request.getCube().getArchive().getSoundFiles());
  }

  public List<SyncRequest> getCubes()
  {
    ArrayList<SyncRequest> list = new ArrayList<>();
    for (Entry<String, SyncRequest> e : cubesNamesMap.entrySet()) {
      list.add(e.getValue());
    }
    return list;
  }

  public File getArchiveDirectory()
  {
    return archiveDir;
  }
  
  /*
  public List<Announcement> getAllAnnouncements()
  {
    ArrayList<Announcement> list = new ArrayList<>();
    synchronized (announcementsMap) {
      for (Entry<String, ArrayList<Announcement>> e : announcementsMap.entrySet()) {
        list.addAll(e);
      }
    }
    return list;
  }
  */

  @SuppressWarnings("unchecked")
  public List<Announcement> takeAnnouncements(String cubeName)
  {
    ArrayList<Announcement> working;
    synchronized (announcementsMap) {
      ArrayList<Announcement> archive = announcementsMap.get(cubeName);
      if (archive == null) {
        archive = new ArrayList<>();
        announcementsMap.put(cubeName, archive);
      }
      working = (ArrayList<Announcement>) archive.clone();
      archive.clear();
    }
    return working;
  }

  public void addAnnouncement(Announcement q)
  {
    announcementsList.add(q);
    for (Entry<String, SyncRequest> e : cubesNamesMap.entrySet()) {
      String cubeName = e.getKey();
      synchronized (announcementsMap) {
        ArrayList<Announcement> archive = announcementsMap.get(cubeName);
        if (archive == null) {
          archive = new ArrayList<>();
          announcementsMap.put(cubeName, archive);
        }
        archive.add(q);
      }
    }

  }

  public List<Announcement> getAnnouncements()
  {
    ArrayList<Announcement> list = new ArrayList<>();
    list.addAll(announcementsList);
    return list;
  }

}
