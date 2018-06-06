package org.mendybot.announcer.common.model.dto;

import java.util.UUID;

public class QuickAnnouncementRequest
{
  private UUID uuid;
  private String key;
  private String text;

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

  @Override
  public int hashCode()
  {
    return text.hashCode();
  }

}