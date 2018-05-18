package org.mendybot.announcer.common;

import java.io.File;
import java.util.UUID;

public class Resource
{
  private ResourceType type;
  private UUID uuid;
  private File file;

  public Resource(ResourceType type, UUID uuid)
  {
    this.type = type;
    this.uuid = uuid;
  }
  
  public ResourceType getType()
  {
    return type;
  }

  public UUID getUuid()
  {
    return uuid;
  }

  public void setFile(File file)
  {
    this.file = file;
  }
  
  public File getFile()
  {
    return file;
  }

}
