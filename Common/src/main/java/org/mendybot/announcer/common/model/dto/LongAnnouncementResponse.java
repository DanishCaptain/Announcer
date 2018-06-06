package org.mendybot.announcer.common.model.dto;

public class LongAnnouncementResponse
{
  private boolean success = true;
  private String message="";

  public void setError(String message)
  {
    success = false;
    this.message = message;
  }
  
  public boolean isSuccess()
  {
    return success;
  }

  public String getMessage()
  {
    return message;
  }
  
  @Override
  public String toString()
  {
    return success+":"+message;
  }

}
