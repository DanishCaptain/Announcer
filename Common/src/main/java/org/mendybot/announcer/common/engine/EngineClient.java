package org.mendybot.announcer.common.engine;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import org.mendybot.announcer.common.model.dto.ArchiveResource;
import org.mendybot.announcer.common.model.dto.Cube;
import org.mendybot.announcer.common.model.dto.LongAnnouncementRequest;
import org.mendybot.announcer.common.model.dto.LongAnnouncementResponse;
import org.mendybot.announcer.common.model.dto.QuickAnnouncementRequest;
import org.mendybot.announcer.common.model.dto.QuickAnnouncementResponse;
import org.mendybot.announcer.common.model.dto.ResourceRequest;
import org.mendybot.announcer.common.model.dto.ResourceResponse;
import org.mendybot.announcer.common.model.dto.StatusRequest;
import org.mendybot.announcer.common.model.dto.StatusResponse;
import org.mendybot.announcer.common.model.dto.SyncRequest;
import org.mendybot.announcer.common.model.dto.SyncResponse;
import org.mendybot.announcer.fault.ExecuteException;
import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.tools.InterfaceTool;

import com.google.gson.Gson;

public class EngineClient
{
  private static final Logger LOG = Logger.getInstance(EngineClient.class);
  private final String USER_AGENT = "Mozilla/5.0";
  private ResourceGrabber grabber;

  public EngineClient()
  {
    grabber = new ResourceGrabber(this);
  }

  public SyncResponse sendSync(Cube cube) throws ExecuteException
  {
    String url = "http://innovannounce.bks.net:8080/sync";
    try
    {
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      con.setDoOutput(true);
      con.setDoInput(true);

      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      con.setRequestMethod("POST");

      // String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
      Gson gson = new Gson();
      SyncRequest request = new SyncRequest();
      request.setCube(cube);
      request.setKey(InterfaceTool.PASS_KEY);

      String json = gson.toJson(request);
      // InterfaceTool.PASS_KEY

      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      // wr.writeBytes(urlParameters);
      wr.writeBytes(json);
      wr.flush();
      wr.close();

      int responseCode = con.getResponseCode();
      LOG.logDebug("sendSync", "Sending 'POST' request to URL : " + url);
      LOG.logDebug("sendSync", "Response Code : " + responseCode);

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      SyncResponse response = gson.fromJson(in, SyncResponse.class);
      in.close();
      return response;
    }
    catch (MalformedURLException e)
    {
      throw new ExecuteException(e);
    }
    catch (IOException e)
    {
      throw new ExecuteException(e);
    }

  }

  public StatusResponse sendStatus() throws ExecuteException
  {
    /*
     * { "success":true, "name":"Folkvangr", "host":"innovannounce.bks.net",
     * "ip":"10.0.0.223", "version":"0.0.1", "archive":{ "sList":[], "iList":[ {
     * "uuid":"d9f95660-ef99-460e-9f94-21f0985122a0", "name":"logo.ppm",
     * "size":3124, "ts":1526098940000 } ], "cList":[ {
     * "uuid":"789faf7f-5821-4010-b9a5-b855d139fb65", "name":"Folkvangr" } ] },
     * "message":"" }
     * 
     */
    String url = "http://innovannounce.bks.net:8080/status";
    try
    {
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      con.setDoOutput(true);
      con.setDoInput(true);

      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      con.setRequestMethod("POST");

      // String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
      Gson gson = new Gson();
      StatusRequest request = new StatusRequest();
      // request.setUuid(uuid);
      request.setName(InetAddress.getLocalHost().getCanonicalHostName());
      request.setKey(InterfaceTool.PASS_KEY);

      String json = gson.toJson(request);
      // InterfaceTool.PASS_KEY

      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      // wr.writeBytes(urlParameters);
      wr.writeBytes(json);
      wr.flush();
      wr.close();

      int responseCode = con.getResponseCode();
      LOG.logDebug("sendStatus", "Sending 'POST' request to URL : " + url);
      LOG.logDebug("sendStatus", "Response Code : " + responseCode);

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      StatusResponse response = gson.fromJson(in, StatusResponse.class);
      in.close();
      return response;
    }
    catch (MalformedURLException e)
    {
      throw new ExecuteException(e);
    }
    catch (IOException e)
    {
      throw new ExecuteException(e);
    }

  }

  public void grabResource(ArchiveResource ar, File f) throws ExecuteException
  {
    grabber.grab(ar, f);
  }

  void callResource(ArchiveResource ar, File f) throws ExecuteException
  {
    String url = "http://innovannounce.bks.net:8080/resource";
    try
    {
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      con.setDoOutput(true);
      con.setDoInput(true);

      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      con.setRequestMethod("POST");

      Gson gson = new Gson();
      ResourceRequest request = new ResourceRequest();
      request.setResource(ar);
      request.setKey(InterfaceTool.PASS_KEY);

      String json = gson.toJson(request);
      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      // wr.writeBytes(urlParameters);
      wr.writeBytes(json);
      wr.flush();
      wr.close();

      int responseCode = con.getResponseCode();
      LOG.logDebug("callResource", "Sending 'POST' request to URL : " + url);
      LOG.logDebug("callResource", "Response Code : " + responseCode);

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      ResourceResponse response = gson.fromJson(in, ResourceResponse.class);
      FileOutputStream fos = new FileOutputStream(f);
      fos.write(response.getData());
      fos.close();
      in.close();
      f.setLastModified(ar.getTs());
    }
    catch (MalformedURLException e)
    {
      throw new ExecuteException(e);
    }
    catch (IOException e)
    {
      throw new ExecuteException(e);
    }
  }

  public QuickAnnouncementResponse send(QuickAnnouncementRequest request) throws ExecuteException
  {
    String url = "http://innovannounce.bks.net:8080/quick";
    try
    {
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      con.setDoOutput(true);
      con.setDoInput(true);

      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      con.setRequestMethod("POST");

      Gson gson = new Gson();
      request.setKey(InterfaceTool.PASS_KEY);

      String json = gson.toJson(request);
      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      // wr.writeBytes(urlParameters);
      wr.writeBytes(json);
      wr.flush();
      wr.close();

      int responseCode = con.getResponseCode();
      LOG.logDebug("callResource", "Sending 'POST' request to URL : " + url);
      LOG.logDebug("callResource", "Response Code : " + responseCode);

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      QuickAnnouncementResponse response = gson.fromJson(in, QuickAnnouncementResponse.class);
      return response;
    }
    catch (MalformedURLException e)
    {
      throw new ExecuteException(e);
    }
    catch (IOException e)
    {
      throw new ExecuteException(e);
    }
  }

  public LongAnnouncementResponse send(LongAnnouncementRequest request) throws ExecuteException
  {
    String url = "http://innovannounce.bks.net:8080/announcement-request";
    try
    {
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      con.setDoOutput(true);
      con.setDoInput(true);

      con.setRequestProperty("Content-Type", "application/json");
      con.setRequestProperty("Accept", "application/json");
      con.setRequestProperty("User-Agent", USER_AGENT);
      con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
      con.setRequestMethod("POST");

      Gson gson = new Gson();
      request.setKey(InterfaceTool.PASS_KEY);

      String json = gson.toJson(request);
      DataOutputStream wr = new DataOutputStream(con.getOutputStream());
      // wr.writeBytes(urlParameters);
      wr.writeBytes(json);
      wr.flush();
      wr.close();

      int responseCode = con.getResponseCode();
      LOG.logDebug("callResource", "Sending 'POST' request to URL : " + url);
      LOG.logDebug("callResource", "Response Code : " + responseCode);

      BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
      LongAnnouncementResponse response = gson.fromJson(in, LongAnnouncementResponse.class);
      return response;
    }
    catch (MalformedURLException e)
    {
      throw new ExecuteException(e);
    }
    catch (IOException e)
    {
      throw new ExecuteException(e);
    }
  }

}
