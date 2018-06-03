package org.mendybot.announcer.common.model.dto;

public class ResourceRequest
{
  private String key;
  private ArchiveResource ar;

  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public ArchiveResource getResource()
  {
    return ar;
  }

  public void setResource(ArchiveResource ar)
  {
    this.ar = ar;
  }

}
