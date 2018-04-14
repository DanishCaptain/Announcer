package org.mendybot.announcer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.mendybot.announcer.model.AnnouncerModel;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StatusHandler implements HttpHandler
{
  private String version;
  private AnnouncerModel model;

  public StatusHandler(AnnouncerModel model)
  {
    this.model = model;
    InputStream is = this.getClass().getClassLoader().getResourceAsStream("version.txt");
    if (is != null) {
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      try
      {
        version = br.readLine();
        System.out.println(version);
      }
      catch (IOException e)
      {
        version = e.getMessage();
      }
    } else {
      version = "Unknown";
    }
  }

  @Override
  public void handle(HttpExchange ex) throws IOException
  {
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<head>");
    sb.append("</head>");
    sb.append("<body>");
    sb.append("<div><div>version: </div><div>"+version+"</div></div>");
    sb.append("</body>");
    sb.append("</html>");
    ex.sendResponseHeaders(200, sb.length());
    OutputStream os = ex.getResponseBody();
    os.write(sb.toString().getBytes());
    os.close();
  }

}
