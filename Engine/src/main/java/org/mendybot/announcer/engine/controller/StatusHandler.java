package org.mendybot.announcer.engine.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

import org.mendybot.announcer.common.Resource;
import org.mendybot.announcer.common.model.dto.Archive;
import org.mendybot.announcer.common.model.dto.ArchiveResource;
import org.mendybot.announcer.common.model.dto.StatusRequest;
import org.mendybot.announcer.common.model.dto.StatusResponse;
import org.mendybot.announcer.common.model.dto.SyncRequest;
import org.mendybot.announcer.engine.model.EngineModel;
import org.mendybot.announcer.tools.InterfaceTool;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

public class StatusHandler extends BaseHandler
{
  public static final String CONTEXT = "/status";
  @Value("${app.version}")
  private String version;

  public StatusHandler(EngineModel model)
  {
    super(model);
  }

  @Override
  public void handle(HttpExchange ex) throws IOException
  {
    Gson gson = new Gson();
    String method = ex.getRequestMethod();
    StatusResponse response = new StatusResponse();
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
        response.setError("json expected");
        returnCode = 400;
      } 
      else 
      {
        StatusRequest request = gson.fromJson(new InputStreamReader(ex.getRequestBody()), StatusRequest.class);
        
        if (!InterfaceTool.PASS_KEY.equals(request.getKey())) {
          response.setError("unauthorized");
          returnCode = 401;
        } else {
          try
          {
            response.setName(request.getName());
            response.setHost(ex.getRemoteAddress().getHostName());
            response.setIp(ex.getRemoteAddress().getAddress().getHostAddress());
            response.setVersion(getModel().getVersion());
            
            Archive archive = new Archive();
            response.setArchive(archive);

            List<Resource> soundFiles = getModel().getSoundFiles();
            for (Resource r : soundFiles) {
              File file = r.getFile();
              ArchiveResource ar = new ArchiveResource();
              ar.setName(file.getName());
              ar.setSize(file.length());
              ar.setTs(file.lastModified());
              archive.addSoundFile(ar);
            }

            List<Resource> imageFiles = getModel().getImageFiles();
            for (Resource r : imageFiles) {
              File file = r.getFile();
              ArchiveResource ar = new ArchiveResource();
              ar.setName(file.getName());
              ar.setSize(file.length());
              ar.setTs(file.lastModified());
              archive.addImageFile(ar);
            }
            
            List<SyncRequest> cubes = getModel().getCubes();
            for (SyncRequest sr : cubes) {
              response.addCube(sr.getCube());
            }

            returnCode = 200;
          }
        catch (Exception e)
        {
          response.setError("problem: "+e.getMessage());
          returnCode = 400;
        }
          
        }
      }
    } else {
      response.setError("post only please");
      returnCode = 400;
    }
    String json = gson.toJson(response);
    ex.sendResponseHeaders(returnCode, json.length());
    OutputStream os = ex.getResponseBody();
    os.write(json.getBytes());
    os.close();

  }

}
