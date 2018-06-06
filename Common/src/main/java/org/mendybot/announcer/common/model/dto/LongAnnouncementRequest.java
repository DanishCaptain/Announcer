package org.mendybot.announcer.common.model.dto;

import java.util.UUID;

public class LongAnnouncementRequest
{
  private UUID uuid;
  private String key;
  private String displayText;
  private String sayText;
  private ArchiveResource sound;

  public UUID getUuid()
  {
    return uuid;
  }

  public void setUuid(UUID uuid)
  {
    this.uuid = uuid;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public String getDisplayText()
  {
    return displayText;
  }

  public void setDisplayText(String text)
  {
    this.displayText = text;
  }

  public String getSayText()
  {
    return sayText;
  }

  public void setSayText(String text)
  {
    this.sayText = text;
  }

  public ArchiveResource getSound()
  {
    return sound;
  }

  public void setSound(ArchiveResource sound)
  {
    this.sound = sound;
  }

  @Override
  public int hashCode()
  {
    return uuid.hashCode();
  }

}
