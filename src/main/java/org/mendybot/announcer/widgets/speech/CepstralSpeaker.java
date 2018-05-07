package org.mendybot.announcer.widgets.speech;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.widgets.CommandWidget;

public class CepstralSpeaker extends CommandWidget implements SpeechWidget
{
  private final static Logger LOG = Logger.getInstance(CepstralSpeaker.class);
  private static CepstralSpeaker singleton;

  private CepstralSpeaker()
  {
  }

  @Override
  public void generate(File sound, SayText st)
  {
    LOG.logInfo("generate", "called for "+sound);        
    String command = "/opt/swift/bin/swift -n Diane -o "+sound.getAbsolutePath()+" \""+st.getText()+"\"";
    LOG.logInfo("generate", "command: "+command);        
    Runtime run = Runtime.getRuntime();
    try
    {
      LOG.logInfo("generate", "calling for "+sound);
      Process proc = run.exec(command);
      LOG.logInfo("generate", "starting for "+sound);
      proc.waitFor();
      LOG.logInfo("generate", "ending for "+sound);
      LOG.logInfo("generate", "exit code: "+proc.exitValue());
      BufferedReader is = new BufferedReader(new InputStreamReader((proc.getInputStream())));
      String line;
      while((line = is.readLine()) !=null) {
        LOG.logSevere("generate", line);
      }
      is.close();
      is = new BufferedReader(new InputStreamReader((proc.getErrorStream())));
      while((line = is.readLine()) !=null) {
        LOG.logSevere("generate", "E: "+ line);
      }
      is.close();
    }
    catch (IOException e)
    {
      LOG.logSevere("generate", e);
    }
    catch (InterruptedException e)
    {
      LOG.logSevere("generate", e);
    }
  }

  public synchronized static CepstralSpeaker getInstance()
  {
    if (singleton == null)
    {
      singleton = new CepstralSpeaker();
    }
    return singleton;
  }

}
