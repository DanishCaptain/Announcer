package org.mendybot.announcer.engine.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

import org.mendybot.announcer.common.model.dto.Announcement;
import org.mendybot.announcer.common.model.dto.QuickAnnouncementRequest;
import org.mendybot.announcer.common.model.dto.QuickAnnouncementResponse;
import org.mendybot.announcer.engine.model.EngineModel;
import org.mendybot.announcer.tools.InterfaceTool;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

public class QuickHandler extends BaseHandler
{
  public static final String CONTEXT = "/quick";

  public QuickHandler(EngineModel model)
  {
    super(model);
  }

  @Override
  public void handle(HttpExchange ex) throws IOException
  {
    Gson gson = new Gson();
    String method = ex.getRequestMethod();
    QuickAnnouncementResponse response = new QuickAnnouncementResponse();
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
        QuickAnnouncementRequest request = gson.fromJson(new InputStreamReader(ex.getRequestBody()), QuickAnnouncementRequest.class);
        if (!InterfaceTool.PASS_KEY.equals(request.getKey())) {
          response.setError("unauthorized");
          returnCode = 401;
        } else {
          try
          {
            Announcement q = new Announcement();
            q.setUuid(request.getUuid());
            q.setDisplayText(request.getText());
            q.setSayText(request.getText());
            getModel().addAnnouncement(q);
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
