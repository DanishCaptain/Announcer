package org.mendybot.announcer.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

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
import org.mendybot.announcer.widgets.speech.SayText;
import org.mendybot.announcer.widgets.speech.SpeechWidget;

import com.sun.net.httpserver.HttpExchange;

public class RequestHandler extends BaseHandler
{
  public static final String P_SAY_TEXT = "say";
  public static final String P_SAY_TEXT_REPEAT = "say-repeat";
  public static final String P_DISPLAY_TEXT = "display";
  public static final String P_DISPLAY_TEXT_FONT = "display-font";
  public static final String P_DISPLAY_TEXT_RGB = "display-rgb";
  public static final String P_DISPLAY_TEXT_REPEAT = "display-repeat";
  private SoundWidget soundEngine;
  private SpeechWidget speakerEngine;
  private MatrixDisplayWidget displayEngine;

  public RequestHandler(AnnouncerModel model)
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
    String requestMethod = ex.getRequestMethod();
    if (requestMethod.equalsIgnoreCase("POST")) {
      String json = new BufferedReader(
              new InputStreamReader(
                      ex.getRequestBody()
              )
      ).lines().collect(Collectors.joining("\n"));
      ex.getResponseHeaders().set("Content-Type", "application/json");
      
     JSONObject jObj = new JSONObject(json);
     String say = jObj.getString(P_SAY_TEXT);
     int repeatSay = jObj.getInt(P_SAY_TEXT_REPEAT);
     String display = jObj.getString(P_DISPLAY_TEXT);
     String font = jObj.getString(P_DISPLAY_TEXT_FONT);
     String rgb = jObj.getString(P_DISPLAY_TEXT_RGB);
     int repeatDisplay = jObj.getInt(P_DISPLAY_TEXT_REPEAT);
     
     JSONObject jsonR = new JSONObject();
     jsonR.put("result", "Request submitted");
     String response = jsonR.toString();
     ex.sendResponseHeaders(200, response.length());
     OutputStream os = ex.getResponseBody();
     os.write(response.getBytes());
     os.flush();
     os.close();
     
     if (say != null && !"".equals(say.trim())) {
       File sound = new File(getModel().getArchiveDirectory(), "say.wav");
       SayText st = new SayText(say);
       st.setRepeat(repeatSay);
       speakerEngine.generate(sound, st);
       PlayFile pf = new PlayFile(sound);
       pf.setRepeat(st.getRepeat());
       soundEngine.submit(pf);
     }
     if (display != null && !"".equals(display.trim())) {
       DisplayText dt = new DisplayText(display);
       dt.setFont(font);
       dt.setRGB(rgb);
       dt.setRepeat(repeatDisplay);
       displayEngine.show(dt);
     }

      
    } else {
      ex.getResponseHeaders().set("Content-Type", "application/json");
      ex.sendResponseHeaders(200, 0);
      OutputStream os = ex.getResponseBody();
      JSONObject json = new JSONObject();
      json.put("error", "Post Expected");
      String response = json.toString();
      os.write(response.getBytes());
      os.close();
    }
  }

}
