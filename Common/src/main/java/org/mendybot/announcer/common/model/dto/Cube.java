package org.mendybot.announcer.common.model.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cube
{
  private UUID uuid;
  private String name;
  private Archive archive = new Archive();
  private ArrayList<Announcement> announcements = new ArrayList<>();

  public UUID getUuid()
  {
    return uuid;
  }

  public void setUuid(UUID uuid)
  {
    this.uuid = uuid;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public Archive getArchive()
  {
    return archive;
  }

  public void addAnnouncement(Announcement q)
  {
    announcements.add(q);
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
    return uuid+":"+name;
  }

}
