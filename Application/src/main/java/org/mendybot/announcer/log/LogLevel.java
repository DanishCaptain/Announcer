package org.mendybot.announcer.log;

/**
 * The discriminator for a log message 
 * <pre>
 * <B>History
 * Date        Author         Description/Justification</B>
 * 11 Mar 2018 Brian Sorensen ENG-100 - Initial code.
 * </pre>
 */
public enum LogLevel
{
  /** No logs will be processed */
  NONE, 
  /** Only severe logs will be processed */
  SEVERE, 
  /** Only Severe and Warning logs will be processed */
  WARNING, 
  /** Only Severe, Warning and Info logs will be processed */
  INFO, 
  /** All logs will be processed */
  DEBUG;

  /**
   * Used to determine if the give LogLevel is in the DEBUG scope
   * @param level level to test
   */
  public static boolean isDebugRange(LogLevel level)
  {
    if (level == DEBUG)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Used to determine if the give LogLevel is in the INFO scope
   * @param level level to test
   */
  public static boolean isInfoRange(LogLevel level)
  {
    if (level == INFO || level == DEBUG)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Used to determine if the give LogLevel is in the WARNING scope
   * @param level level to test
   */
  public static boolean isWarningRange(LogLevel level)
  {
    if (level == WARNING || level == INFO || level == DEBUG)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Used to determine if the give LogLevel is in the SEVERE scope
   * @param level level to test
   */
  public static boolean isSevereRange(LogLevel level)
  {
    if (level == SEVERE || level == WARNING || level == INFO || level == DEBUG)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  /**
   * Used to determine if the give LogLevel is in the NONE scope
   * @param level level to test
   */
  public static boolean isNoneRange(LogLevel level)
  {
    if (level == NONE || level == SEVERE || level == WARNING || level == INFO
        || level == DEBUG)
    {
      return true;
    }
    else
    {
      return false;
    }
  }

}
