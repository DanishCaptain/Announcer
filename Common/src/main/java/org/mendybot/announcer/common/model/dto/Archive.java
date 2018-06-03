package org.mendybot.announcer.common.model.dto;

import java.util.ArrayList;
import java.util.List;

public class Archive
{
  private ArrayList<ArchiveResource> sList = new ArrayList<>();
  private ArrayList<ArchiveResource> iList = new ArrayList<>();

  public void addSoundFile(ArchiveResource ar)
  {
    sList.add(ar);
  }
  
  public List<ArchiveResource> getSoundFiles() {
    ArrayList<ArchiveResource> list = new ArrayList<>();
    list.addAll(sList);
    return list;    
  }

  public void setSoundFiles(List<ArchiveResource> list) {
    sList.clear();
    if (list != null) {
      sList.addAll(list);
    }
  }

  public void addImageFile(ArchiveResource ar)
  {
    iList.add(ar);
  }

  public List<ArchiveResource> getImageFiles() {
    ArrayList<ArchiveResource> list = new ArrayList<>();
    list.addAll(iList);
    return list;    
  }

  public void setImageFiles(List<ArchiveResource> list) {
    iList.clear();
    if (list != null) {
      iList.addAll(list);
    }
  }

  @Override
  public String toString()
  {
    return sList+":"+iList;
  }

  public ArchiveResource lookupArchiveResource(String name)
  {
    for (ArchiveResource ar : sList) {
      if (ar.getName().equals(name)) {
        return ar;
      }
    }
    for (ArchiveResource ar : iList) {
      if (ar.getName().equals(name)) {
        return ar;
      }
    }
    return null;
  }

}
