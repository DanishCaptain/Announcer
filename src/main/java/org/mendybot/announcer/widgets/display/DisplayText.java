package org.mendybot.announcer.widgets.display;

public class DisplayText
{
  private String text;
  private String font="10x20";
  private String rgb="255,255,0";
  private int repeat=1;

  public DisplayText(String text)
  {
    this.text = text;
  }

  public String getText()
  {
    return text;
  }

  public String getFont()
  {
    return font;
  }
  
  public void setFont(String font)
  {
    this.font = font;
  }
  
  public int getRepeat()
  {
    return repeat;
  }

  public void setRepeat(int repeat)
  {
    this.repeat = repeat;
  }

  public String getRGB()
  {
    return rgb;
  }

  public void setRGB(String rgb)
  {
    this.rgb = rgb;
  }

}
