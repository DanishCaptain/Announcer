package org.mendybot.announcer.widgets.speech;

import java.io.File;

public interface SpeechWidget
{

  void generate(File sound, SayText text);

}
