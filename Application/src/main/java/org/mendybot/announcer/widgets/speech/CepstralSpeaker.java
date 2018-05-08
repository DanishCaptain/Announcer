package org.mendybot.announcer.widgets.speech;

import java.io.File;

import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.model.AnnouncerModel;
import org.mendybot.announcer.tools.CommandTool;
import org.mendybot.announcer.widgets.CommandWidget;
import org.mendybot.announcer.widgets.sound.PlayFile;
import org.mendybot.announcer.widgets.sound.SoundWidget;

public class CepstralSpeaker extends CommandWidget implements SpeechWidget
{
  private final static Logger LOG = Logger.getInstance(CepstralSpeaker.class);
  private static CepstralSpeaker singleton;
  private final File sound;
  private final SoundWidget soundEngine;

  private CepstralSpeaker(AnnouncerModel model, SoundWidget soundEngine)
  {
    sound = new File(model.getArchiveDirectory(), "say.wav");
    this.soundEngine = soundEngine;
  }

  @Override
  public void say(SayText st)
  {
    LOG.logInfo("generate", "called for "+sound);
    synchronized (this)
    {
      String command = "/opt/swift/bin/swift -n Diane -o "+sound.getAbsolutePath()+" \""+st.getText()+"\"";
      CommandTool.execute("for "+sound, command);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
        }
        PlayFile pf = new PlayFile(sound);
        soundEngine.checkSoundLevel(st.getSoundLevel());
        for (int i=0; i<st.getRepeat(); i++) {
            soundEngine.submit(pf);
        }
    }
  }

  public synchronized static CepstralSpeaker getInstance(AnnouncerModel model, SoundWidget soundEngine)
  {
    if (singleton == null)
    {
      singleton = new CepstralSpeaker(model, soundEngine);
    }
    return singleton;
  }

}
