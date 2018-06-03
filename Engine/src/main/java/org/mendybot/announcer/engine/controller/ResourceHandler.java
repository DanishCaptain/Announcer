package org.mendybot.announcer.engine.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

import org.mendybot.announcer.common.model.dto.ResourceRequest;
import org.mendybot.announcer.common.model.dto.ResourceResponse;
import org.mendybot.announcer.engine.model.EngineModel;
import org.mendybot.announcer.tools.InterfaceTool;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

public class ResourceHandler extends BaseHandler
{
  public static final String CONTEXT = "/resource";
  @Value("${app.version}")
  private String version;

  public ResourceHandler(EngineModel model)
  {
    super(model);
  }

  @Override
  public void handle(HttpExchange ex) throws IOException
  {
    Gson gson = new Gson();
    String method = ex.getRequestMethod();
    ResourceResponse response = new ResourceResponse();
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
        ResourceRequest request = gson.fromJson(new InputStreamReader(ex.getRequestBody()), ResourceRequest.class);
        
        if (!InterfaceTool.PASS_KEY.equals(request.getKey())) {
          response.setError("unauthorized");
          returnCode = 401;
        } else {
          try
          {
            response.setResource(request.getResource());
            File f = new File(getModel().getArchiveDirectory(), request.getResource().getName());
            byte[] data = new byte[(int) f.length()];
            response.setData(data);
            FileInputStream fis = new FileInputStream(f);
            fis.read(data);
            fis.close();
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
