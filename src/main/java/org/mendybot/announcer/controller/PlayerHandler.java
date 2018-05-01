package org.mendybot.announcer.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.mendybot.announcer.model.AnnouncerModel;
import org.mendybot.announcer.model.AnnouncerOs;
import org.mendybot.announcer.widgets.sound.APlayer;
import org.mendybot.announcer.widgets.sound.OmxPlayer;
import org.mendybot.announcer.widgets.sound.SoundWidget;

import com.sun.net.httpserver.HttpExchange;

public class PlayerHandler extends BaseHandler
{
  private SoundWidget soundEngine;

  public PlayerHandler(AnnouncerModel model)
  {
    super(model);
    if (model.getOs() == AnnouncerOs.RASPBIAN) {
      soundEngine = APlayer.getInstance();
    } 
    else if (model.getOs() == AnnouncerOs.UBUNTO) {
      soundEngine = OmxPlayer.getInstance();
    }
    else
    {
      throw new RuntimeException("Unknown AnnouncerOs "+model.getOs());
    }
  }

  @Override
  public void handle(HttpExchange ex) throws IOException
  {
    System.out.println("play:");
    Properties p = this.getProperties(ex);
    String id = p.getProperty("id");
    System.out.println("play: "+id);
    String response = "Play request submitted";
    if (id!= null) {
      File file = new File(getModel().getArchiveDirectory(), id + ".wav");
      System.out.println(file+":"+file.exists());
      soundEngine.submit(file);
    } else {
      response = "Unrecognized player id: "+id;
    }
    
    ex.sendResponseHeaders(200, response.length());
    OutputStream os = ex.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

}
