package org.mendybot.announcer.widgets.display;

import java.io.File;

public interface MatrixDisplayWidget
{
  void show(DisplayText text);

  void show(ImageFile file);

  void show(Effect effect);

}
