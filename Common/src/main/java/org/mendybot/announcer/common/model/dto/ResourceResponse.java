package org.mendybot.announcer.common.model.dto;

public class ResourceResponse
{
  private boolean success = true;
  private ArchiveResource resource;
  private byte[] data;
  private String message="";

  public ArchiveResource getResource()
  {
    return resource;
  }

  public void setResource(ArchiveResource resource)
  {
    this.resource = resource;
  }

  public byte[] getData()
  {
    return data;
  }

  public void setData(byte[] data)
  {
    this.data=data;
  }

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

}
