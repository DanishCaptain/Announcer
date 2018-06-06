package org.mendybot.announcer.engine.bean;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.mendybot.announcer.engine.controller.LongHandler;
import org.mendybot.announcer.engine.controller.QuickHandler;
import org.mendybot.announcer.engine.controller.ResourceHandler;
import org.mendybot.announcer.engine.controller.StatusHandler;
import org.mendybot.announcer.engine.controller.SyncHandler;
import org.mendybot.announcer.engine.model.EngineModel;
import org.mendybot.announcer.fault.ExecuteException;
import org.springframework.beans.factory.annotation.Autowired;
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
      System.out.println("port: "+8080);
      server = HttpServer.create(new InetSocketAddress(8080), 0);
      server.createContext(SyncHandler.CONTEXT, new SyncHandler(model));
      server.createContext(StatusHandler.CONTEXT, new StatusHandler(model));
      server.createContext(ResourceHandler.CONTEXT, new ResourceHandler(model));
      server.createContext(QuickHandler.CONTEXT, new QuickHandler(model));
      server.createContext(LongHandler.CONTEXT, new LongHandler(model));
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
