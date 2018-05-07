package org.mendybot.announcer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.mendybot.announcer.controller.AnnouncerHandler;
import org.mendybot.announcer.controller.DisplayHandler;
import org.mendybot.announcer.controller.PlayerHandler;
import org.mendybot.announcer.controller.RequestHandler;
import org.mendybot.announcer.controller.StatusHandler;
import org.mendybot.announcer.model.AnnouncerModel;
import org.mendybot.announcer.model.AnnouncerOs;
import org.mendybot.announcer.widgets.display.DisplayText;
import org.mendybot.announcer.widgets.display.ScrollingTextPlayer;

import com.sun.net.httpserver.HttpServer;

public class Announcer
{
  private AnnouncerModel model;

  public Announcer()
  {
    model = new AnnouncerModel();
    try
    {
      HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
      
      server.createContext("/announce", new AnnouncerHandler(model));
      server.createContext("/status", new StatusHandler(model));
      server.createContext("/play", new PlayerHandler(model));
      server.createContext("/display", new DisplayHandler(model));
      server.createContext("/request", new RequestHandler(model));
      
      server.setExecutor(null); // creates a default executor
      server.start();
      
      if (model.getOs() == AnnouncerOs.RASPBIAN) {
        displayNetworkAddress();
      }
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void displayNetworkAddress()
  {
    try
    {
      Thread.sleep(5000);
      ScrollingTextPlayer displayEngine = ScrollingTextPlayer.getInstance();
      //String host = InetAddress.getLocalHost().getHostName();
      String host = InetAddress.getLocalHost().getHostName();
      String ip = InetAddress.getLocalHost().getHostAddress();
      
      Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
      for (NetworkInterface netint : Collections.list(nets)) {
        if ("wlan0".equals(netint.getDisplayName())) {
          Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
          List<InetAddress> list =  Collections.list(inetAddresses);
          ip = list.get(1).getHostAddress();
        }
      }
      DisplayText dt = new DisplayText(ip+"  ["+host+"]   ");
      dt.setRepeat(3);
      displayEngine.show(dt);
    }
    catch (InterruptedException e)
    {
    }
    catch(Exception ex) {
      // don't care
    }
  }

  public static void main(String[] args)
  {
    new Announcer();
  }
}
