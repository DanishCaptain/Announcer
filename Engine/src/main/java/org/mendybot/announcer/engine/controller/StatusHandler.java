package org.mendybot.announcer.engine.controller;

import java.io.IOException;
import java.io.OutputStream;

import org.mendybot.announcer.engine.bean.Engine;
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
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<head>");
    sb.append("</head>");
    sb.append("<body>");
    sb.append("<div><div>version: </div><div>" + version + "</div></div>");
    sb.append("<div><div>version: </div><div>" + Engine.version + "</div></div>");
    sb.append("</body>");
    sb.append("</html>");
    ex.sendResponseHeaders(200, sb.length());
    OutputStream os = ex.getResponseBody();
    os.write(sb.toString().getBytes());
    os.close();
  }

}
