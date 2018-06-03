package org.mendybot.announcer.common.model.dto;

public class SyncRequest
{
  private String key;
  private Cube cube;

  public String getKey()
  {
    return key;
  }

  public void setKey(String key)
  {
    this.key = key;
  }

  public Cube getCube()
  {
    return cube;
  }

  public void setCube(Cube cube)
  {
    this.cube = cube;
  }

  @Override
  public int hashCode()
  {
    return cube.getName().hashCode();
  }
}
