package org.mendybot.announcer.engine.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mendybot.announcer.engine.model.EngineModel;
import org.springframework.beans.factory.annotation.Value;

import com.sun.net.httpserver.HttpExchange;

public class StatusHandler extends BaseHandler
{
  @Value("${app.version}")
  private String version;

  public StatusHandler(EngineModel model)
  {
    super(model);
  }

  @Override
  public void handle(HttpExchange ex) throws IOException
  {
    
    try
    {
      JSONObject obj = new JSONObject();
      obj.put("version", getModel().getVersion());
      JSONObject archive = new JSONObject();
      obj.put("archive", archive);
      
      JSONArray sFiles = new JSONArray();
      archive.put("sound-files", sFiles);
      List<File> soundFiles = getModel().getSoundFiles();
      for (File file : soundFiles) {
        sFiles.put(file.getName());
      }
      
      JSONArray iFiles = new JSONArray();
      archive.put("image-files", iFiles);
      List<File> imageFiles = getModel().getImageFiles();
      for (File file : imageFiles) {
        iFiles.put(file.getName());
      }
      
      String json = obj.toString();
      ex.sendResponseHeaders(200, json.length());
      OutputStream os = ex.getResponseBody();
      os.write(json.getBytes());
      os.close();
    }
    catch (JSONException e)
    {
      String result = "problem: "+e.getMessage();
      ex.sendResponseHeaders(400, result.length());
      OutputStream os = ex.getResponseBody();
      os.write(result.getBytes());
      os.close();
    }
  }

}
