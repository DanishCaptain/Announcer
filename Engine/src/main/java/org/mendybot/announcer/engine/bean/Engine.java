package org.mendybot.announcer.engine.bean;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.mendybot.announcer.engine.controller.StatusHandler;
import org.mendybot.announcer.engine.fault.ExecuteException;
import org.mendybot.announcer.engine.model.EngineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sun.net.httpserver.HttpServer;

@Component
public class Engine
{
  @Autowired
  private EngineModel model;
  private HttpServer server;

  public Engine()
  {
  }

  public void init() throws ExecuteException
  {

    try
    {
      server = HttpServer.create(new InetSocketAddress(8000), 0);

      server.createContext("/status", new StatusHandler(model));
      server.setExecutor(null); // creates a default executor
    }
    catch (IOException e)
    {
      throw new ExecuteException(e);
    }

  }

  public void start() throws ExecuteException
  {
    server.start();
  }
}
