package org.mendybot.announcer.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.json.JSONObject;
import org.mendybot.announcer.model.AnnouncerModel;
import org.mendybot.announcer.model.AnnouncerOs;
import org.mendybot.announcer.widgets.sound.APlayer;
import org.mendybot.announcer.widgets.sound.OmxPlayer;
import org.mendybot.announcer.widgets.sound.PlayFile;
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
    else if (model.getOs() == AnnouncerOs.OSX) {
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
    JSONObject json = new JSONObject();
    json.put("result", "Play request submitted");
    String response = json.toString();
    if (id!= null) {
      File file = new File(getModel().getArchiveDirectory(), id + ".wav");
      PlayFile pf = new PlayFile(file);
      soundEngine.submit(pf);
    } else {
      response = "Unrecognized player id: "+id;
    }
    
    ex.sendResponseHeaders(200, response.length());
    OutputStream os = ex.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

}
