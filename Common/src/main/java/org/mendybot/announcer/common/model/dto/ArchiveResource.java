package org.mendybot.announcer.common.model.dto;

import java.util.UUID;

public class ArchiveResource
{
  private UUID uuid;
  private String name;
  private long size;
  private long ts;

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

  public long getSize()
  {
    return size;
  }

  public void setSize(long size)
  {
    this.size = size;
  }

  public long getTs()
  {
    return ts;
  }

  public void setTs(long ts)
  {
    this.ts = ts;
  }

  @Override
  public String toString()
  {
    return uuid+":"+name+":"+size+":"+ts;
  }
}
