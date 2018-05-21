package org.mendybot.announcer.common.model.dto;

import java.util.UUID;

public class Cube
{
  private UUID uuid;
  private String name;

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

  @Override
  public String toString()
  {
    return uuid+":"+name;
  }
}
