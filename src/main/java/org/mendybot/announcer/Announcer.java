package org.mendybot.announcer;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.mendybot.announcer.controller.AnnouncerHandler;
import org.mendybot.announcer.controller.DisplayHandler;
import org.mendybot.announcer.controller.PlayerHandler;
import org.mendybot.announcer.controller.StatusHandler;
import org.mendybot.announcer.model.AnnouncerModel;

import com.sun.net.httpserver.HttpServer;

public class Announcer
{
  private AnnouncerModel model;

  public Announcer()
  {
    model = new AnnouncerModel();
    try
    {
      HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
      
      server.createContext("/announce", new AnnouncerHandler(model));
      server.createContext("/status", new StatusHandler(model));
      server.createContext("/play", new PlayerHandler(model));
      server.createContext("/display", new DisplayHandler(model));
      
      server.setExecutor(null); // creates a default executor
      server.start();
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void main(String[] args)
  {
    new Announcer();
  }
}
