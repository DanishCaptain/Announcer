package org.mendybot.announcer.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Properties;

import org.json.JSONObject;
import org.mendybot.announcer.model.AnnouncerModel;
import org.mendybot.announcer.model.AnnouncerOs;
import org.mendybot.announcer.widgets.display.DisplayText;
import org.mendybot.announcer.widgets.display.MatrixDisplayWidget;
import org.mendybot.announcer.widgets.display.ScrollingTextPlayer;
import org.mendybot.announcer.widgets.sound.APlayer;
import org.mendybot.announcer.widgets.sound.OmxPlayer;
import org.mendybot.announcer.widgets.sound.PlayFile;
import org.mendybot.announcer.widgets.sound.SoundWidget;
import org.mendybot.announcer.widgets.speech.CepstralSpeaker;
import org.mendybot.announcer.widgets.speech.OsxSay;
import org.mendybot.announcer.widgets.speech.SayText;
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
      speakerEngine = CepstralSpeaker.getInstance(model, soundEngine);
      displayEngine = ScrollingTextPlayer.getInstance();

    } 
    else if (model.getOs() == AnnouncerOs.UBUNTO) {
      soundEngine = OmxPlayer.getInstance();
      speakerEngine = CepstralSpeaker.getInstance(model, soundEngine);
      displayEngine = ScrollingTextPlayer.getInstance();
    }
    else if (model.getOs() == AnnouncerOs.OSX) {
      soundEngine = OmxPlayer.getInstance();
      speakerEngine = OsxSay.getInstance();
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
    String text = p.getProperty(RequestHandler.P_SAY_TEXT);
    text = URLDecoder.decode(text, "UTF-8");
    JSONObject json = new JSONObject();
    json.put("result", "Announcement submitted");
    String response = json.toString();
    
    SayText st = new SayText(text);
    speakerEngine.say(st);

    DisplayText dt = new DisplayText(text);
    displayEngine.show(dt);

    ex.sendResponseHeaders(200, response.length());
    OutputStream os = ex.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

}
