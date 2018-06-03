package org.mendybot.announcer.common.model.dto;

import java.util.ArrayList;
import java.util.List;

public class SyncResponse
{
  private ArrayList<Announcement> announcements = new ArrayList<>();
  private boolean success = true;
  private String name;
  private String host;
  private String ip;
  private String version;
  private Archive archive;
  private String message="";

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getHost()
  {
    return host;
  }

  public void setHost(String host)
  {
    this.host = host;
  }

  public String getIp()
  {
    return ip;
  }

  public void setIp(String ip)
  {
    this.ip = ip;
  }

  public String getVersion()
  {
    return version;
  }

  public void setVersion(String version)
  {
    this.version = version;
  }

  public Archive getArchive()
  {
    return archive;
  }

  public void setArchive(Archive archive)
  {
    this.archive=archive;
  }

  public void setError(String message)
  {
    success = false;
    this.message = message;
  }
  
  public boolean isSuccess()
  {
    return success;
  }

  public String getMessage()
  {
    return message;
  }
  
  public void addAnnouncement(Announcement announcement)
  {
    announcements.add(announcement);
  }
  
  public List<Announcement> getAnnouncements() {
    ArrayList<Announcement> list = new ArrayList<>();
    list.addAll(announcements);
    return list;    
  }

  public void setAnnouncements(List<Announcement> list) {
    announcements.clear();
    if (list != null) {
      announcements.addAll(list);
    }
  }

  @Override
  public String toString()
  {
    return success+":"+name+":"+host+":"+ip+":"+version+":"+archive+":"+announcements+":"+message;
  }

}
