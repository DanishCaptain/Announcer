package org.mendybot.announcer.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class InterfaceTool
{
  public static final String PASS_KEY = "kermit-says-yes";

  private InterfaceTool()
  {
  }

  public static String readString(InputStream is) throws IOException
  {
    StringBuilder sb = new StringBuilder();
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String line;
    while((line=br.readLine()) != null)
    {
      sb.append(line+"\n");
    }
    return sb.toString();
  }
}
