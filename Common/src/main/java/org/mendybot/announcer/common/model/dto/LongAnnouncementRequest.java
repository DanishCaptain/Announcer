package org.mendybot.announcer.common.model.dto;

import java.util.UUID;

public class LongAnnouncementRequest
{
  private UUID uuid;
  private String key;
  private String text;
  private ArchiveResource sound

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

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
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
    return text.hashCode();
  }

}
