package org.mendybot.announcer.engine.bean;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.mendybot.announcer.engine.controller.StatusHandler;
import org.mendybot.announcer.engine.model.EngineModel;
import org.springframework.stereotype.Component;

import com.sun.net.httpserver.HttpServer;

@Component
public class Engine
{
  private HttpServer server;
  private EngineModel model;

  public Engine()
  {
    model = new EngineModel();
  }

  public void init()
  {

    try
    {
      server = HttpServer.create(new InetSocketAddress(8000), 0);

      server.createContext("/status", new StatusHandler(model));
      server.setExecutor(null); // creates a default executor
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public void start()
  {
    server.start();
  }
}
