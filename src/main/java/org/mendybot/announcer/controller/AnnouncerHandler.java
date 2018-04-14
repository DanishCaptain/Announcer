package org.mendybot.announcer.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.mendybot.announcer.model.AnnouncerModel;
import org.mendybot.announcer.model.AnnouncerOs;
import org.mendybot.announcer.widgets.sound.OmxPlayer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class AnnouncerHandler implements HttpHandler
{
  private AnnouncerModel model;
  private OmxPlayer soundEngine;

  public AnnouncerHandler(AnnouncerModel model)
  {
    this.model = model;
    if (model.getOs() == AnnouncerOs.RASPBIAN) {
      soundEngine = new OmxPlayer();
    } 
    else if (model.getOs() == AnnouncerOs.UBUNTO) {
      soundEngine = new OmxPlayer();
    }
    else
    {
      throw new RuntimeException("Unknown AnnouncerOs "+model.getOs());
    }
  }

  @Override
  public void handle(HttpExchange ex) throws IOException
  {
    URI uri = ex.getRequestURI();
    String command = uri.toString().substring(uri.toString().indexOf('?') + 1);
    String response;
    if (command.startsWith("play=")) {
      String fileBase = command.substring(command.indexOf("=")+1);
      boolean result = soundEngine.enqueue(fileBase);
      if (result)
      {
        response = "Placed "+fileBase+" on play queue.";
      }
      else
      {
        response = "Could not place "+fileBase+" on play queue.";
      }
    } else {
      response = "Unrecognized command: "+command;
    }
    
    ex.sendResponseHeaders(200, response.length());
    OutputStream os = ex.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

}
