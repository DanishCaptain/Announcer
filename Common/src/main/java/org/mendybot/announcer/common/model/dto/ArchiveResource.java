package org.mendybot.announcer.common.model.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ArchiveResource
{
  private String name;
  private long size;
  private long ts;
  private String tsString;

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public long getSize()
  {
    return size;
  }

  public void setSize(long size)
  {
    this.size = size;
  }

  public long getTs()
  {
    return ts;
  }

  public void setTs(long ts)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    tsString = sdf.format(new Date(ts));
    this.ts = ts;
  }

  @Override
  public String toString()
  {
    return name+":"+size+":"+tsString;
  }
  
  @Override
  public int hashCode()
  {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object o)
  {
    boolean result = false;;
    if (o instanceof ArchiveResource) {
      result = name.equals(((ArchiveResource)o).name);
    }
    return result;
  }

  public String getTsString()
  {
    return tsString;
  }

}
