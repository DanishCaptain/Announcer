package org.mendybot.announcer.engine.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mendybot.announcer.common.Resource;
import org.mendybot.announcer.engine.model.EngineModel;
import org.mendybot.announcer.tools.InterfaceTool;
import org.springframework.beans.factory.annotation.Value;

import com.sun.net.httpserver.HttpExchange;

public class SyncHandler extends BaseHandler
{
  public static final String CONTEXT = "/sync";
  @Value("${app.version}")
  private String version;

  public SyncHandler(EngineModel model)
  {
    super(model);
  }

  @Override
  public void handle(HttpExchange ex) throws IOException
  {
    String method = ex.getRequestMethod();
    String json;
    int returnCode;
    if ("post".equalsIgnoreCase(method)) {
      String contentType = null;
      for (Entry<String, List<String>> es : ex.getRequestHeaders().entrySet())
      {
        if ("content-type".equalsIgnoreCase(es.getKey())) {
          contentType = es.getValue().get(0);
        }
      }
      if (!"application/json".equalsIgnoreCase(contentType))
      {
        json = "{\"error\": \"json expected"+"\"}";
        returnCode = 400;
      } 
      else 
      {
        JSONObject objIn = new JSONObject(InterfaceTool.readString(ex.getRequestBody()));
        
        String name = objIn.getString("Name");
        String key = objIn.getString("Key");

        if (!InterfaceTool.PASS_KEY.equals(key)) {
          json = "{\"error\": \"unauthorized"+"\"}";
          returnCode = 401;
        } else {
          try
          {
            JSONObject objOut = new JSONObject();
            objOut.put("Name", name);
            objOut.put("Host", ex.getRemoteAddress().getHostName());
            objOut.put("IP", ex.getRemoteAddress().getAddress());
            objOut.put("version", getModel().getVersion());
            JSONObject archive = new JSONObject();
            objOut.put("archive", archive);

            JSONArray sFiles = new JSONArray();
            archive.put("sound-files", sFiles);
            List<Resource> soundFiles = getModel().getSoundFiles();
            for (Resource r : soundFiles) {
              File file = r.getFile();
              JSONObject rO = new JSONObject();
              rO.put("uuid", r.getUuid());
              rO.put("name", file.getName());
              rO.put("size", file.length());
              rO.put("ts", file.lastModified());
              sFiles.put(rO);
            }

            JSONArray iFiles = new JSONArray();
            archive.put("image-files", iFiles);
            List<Resource> imageFiles = getModel().getImageFiles();
            for (Resource r : imageFiles) {
              File file = r.getFile();
              JSONObject rO = new JSONObject();
              rO.put("uuid", r.getUuid());
              rO.put("name", file.getName());
              rO.put("size", file.length());
              rO.put("ts", file.lastModified());
              iFiles.put(rO);
            }

            json = objOut.toString();
            returnCode = 200;
          }
        catch (JSONException e)
        {
          json = "{\"error\": \"problem: "+e.getMessage()+"\"}";
          returnCode = 400;
        }
          
        }
      }
    } else {
      json = "{\"error\": \"post only please\"}";
      returnCode = 400;
    }
    ex.sendResponseHeaders(returnCode, json.length());
    OutputStream os = ex.getResponseBody();
    os.write(json.getBytes());
    os.close();

  }

}
