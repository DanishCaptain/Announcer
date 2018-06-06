package org.mendybot.announcer.common.model.dto;

import java.util.UUID;

public final class Announcement
{
  private UUID uuid;
  private String displayText;
  private String sayText;
  private boolean played;

  public UUID getUuid()
  {
    return uuid;
  }

  public void setUuid(UUID uuid)
  {
    this.uuid = uuid;
  }

  public String getDisplayText()
  {
    return displayText;
  }

  public void setDisplayText(String text)
  {
    displayText = text;
  }

  public String getSayText()
  {
    return sayText;
  }

  public void setSayText(String text)
  {
    sayText = text;
  }

  public void setPlayed(boolean played)
  {
    this.played = played;
  }

  public boolean getPlayed()
  {
    return played;
  }
}
