package org.mendybot.announcer.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Properties;

import org.mendybot.announcer.model.AnnouncerModel;
import org.mendybot.announcer.model.AnnouncerOs;
import org.mendybot.announcer.widgets.display.ImagePlayer;
import org.mendybot.announcer.widgets.display.MatrixDisplayWidget;
import org.mendybot.announcer.widgets.sound.APlayer;
import org.mendybot.announcer.widgets.sound.OmxPlayer;
import org.mendybot.announcer.widgets.sound.SoundWidget;
import org.mendybot.announcer.widgets.speech.CepstralSpeaker;
import org.mendybot.announcer.widgets.speech.SpeechWidget;

import com.sun.net.httpserver.HttpExchange;

public class DisplayHandler extends BaseHandler
{
  private MatrixDisplayWidget displayEngine;

  public DisplayHandler(AnnouncerModel model)
  {
    super(model);
    if (model.getOs() == AnnouncerOs.RASPBIAN) {
      displayEngine = ImagePlayer.getInstance();    

    } 
    else if (model.getOs() == AnnouncerOs.UBUNTO) {
      displayEngine = ImagePlayer.getInstance();
    }
    else
    {
      throw new RuntimeException("Unknown DisplayrOs "+model.getOs());
    }
  }

  @Override
  public void handle(HttpExchange ex) throws IOException
  {
    Properties p = getProperties(ex);
    String id = p.getProperty("id");
    String response = "Display submitted";
    File sound = new File(getModel().getArchiveDirectory(), id+".ppm");
    displayEngine.show(sound);

    ex.sendResponseHeaders(200, response.length());
    OutputStream os = ex.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

}
