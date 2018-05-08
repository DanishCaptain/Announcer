package org.mendybot.announcer.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.json.JSONObject;
import org.mendybot.announcer.model.AnnouncerModel;
import org.mendybot.announcer.model.AnnouncerOs;
import org.mendybot.announcer.widgets.display.ImageFile;
import org.mendybot.announcer.widgets.display.ImagePlayer;
import org.mendybot.announcer.widgets.display.MatrixDisplayWidget;

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
    else if (model.getOs() == AnnouncerOs.OSX) {
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
    JSONObject json = new JSONObject();
    json.put("result", "Display submitted");
    String response = json.toString();
    File image = new File(getModel().getArchiveDirectory(), id+".ppm");
    ImageFile imgF = new ImageFile(image);
    displayEngine.show(imgF);

    ex.sendResponseHeaders(200, response.length());
    OutputStream os = ex.getResponseBody();
    os.write(response.getBytes());
    os.close();
  }

}
