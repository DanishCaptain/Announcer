package org.mendybot.announcer.widgets.display;

import java.io.File;

public interface MatrixDisplayWidget
{

  void show(String text);

  void show(File file);

}