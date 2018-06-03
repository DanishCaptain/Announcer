package org.mendybot.announcer.common.model.dto;

import java.util.ArrayList;
import java.util.List;

public class QuickAnnouncementResponse
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
