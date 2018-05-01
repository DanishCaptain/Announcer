package org.mendybot.announcer.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Properties;

import org.mendybot.announcer.model.AnnouncerModel;
import org.mendybot.announcer.model.AnnouncerOs;
import org.mendybot.announcer.widgets.display.MatrixDisplayWidget;
import org.mendybot.announcer.widgets.display.ScrollingTextPlayer;
import org.mendybot.announcer.widgets.sound.APlayer;
import org.mendybot.announcer.widgets.sound.OmxPlayer;
import org.mendybot.announcer.widgets.sound.SoundWidget;
import org.mendybot.announcer.widgets.speech.CepstralSpeaker;
import org.mendybot.announcer.widgets.speech.SpeechWidget;

import com.sun.net.httpserver.HttpExchange;

public class AnnouncerHandler extends BaseHandler
{
  private SoundWidget soundEngine;
  private SpeechWidget speakerEngine;
  private MatrixDisplayWidget displayEngine;

  public AnnouncerHandler(AnnouncerModel model)
  {
    super(model);
    if (model.getOs() == AnnouncerOs.RASPBIAN) {
      soundEngine = APlayer.getInstance();
      speakerEngine = CepstralSpeaker.getInstance();
      displayEngine = ScrollingTextPlayer.getInstance();    

    } 
    else if (model.getOs() == AnnouncerOs.UBUNTO) {
      soundEngine = OmxPlayer.getInstance();
      speakerEngine = CepstralSpeaker.getInstance();
      displayEngine = ScrollingTextPlayer.getInstance();
    }
    else
    {
      throw new RuntimeException("Unknown AnnouncerOs "+model.getOs());
    }
  }

  @Override
  public void handle(HttpExchange ex) throws IOException
  {
    Properties p = getProperties(ex);
    String text = p.getProperty("say");
    text = URLDecoder.decode(text, "UTF-8");
    String response = "Announcement submitted";
    File sound = new File(getModel().getArchiveDirectory(), "say.wav");
    speakerEngine.generate(sound, text);
    try
    {
      Thread.sleep(500);
    }
    catch (InterruptedException e){}
    displayEngine.show(text);

    soundEngine.submit(sound);
    ex.sendResponseHeaders(200, response.length());
    OutputStream os = ex.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

}
