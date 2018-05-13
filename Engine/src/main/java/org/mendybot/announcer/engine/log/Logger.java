package org.mendybot.announcer.engine.log;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

import org.mendybot.announcer.engine.fault.ExecuteException;

/**
 * This is used to initialize the Log_Level and Log_Mode via the provided
 * properties.
 * 
 * <pre>
 * <B>History
 * Date        Author         Description/Justification</B>
 * 11 Mar 2018 Brian Sorensen ENG-100 - Initial code.
 * </pre>
 */
public class Logger
{
  public static final String LOG_BASE = "org.mendybot";
  private static HashMap<String, Logger> singleton = new HashMap<String, Logger>();
  private static FileHandler fh;
  private static HashMap<java.util.logging.Logger, LogMode> modeMap = new HashMap<java.util.logging.Logger, LogMode>();
  private static ConsoleHandler ch;
  private static String fileNameBase = "Logger";
  private java.util.logging.Logger logger;
  private Class<?> c;

  /**
   * Constructor
   * 
   * @param c
   *          The class this logger is associated to
   */
  protected Logger(Class<?> c)
  {
    this.c = c;
    logger = java.util.logging.Logger.getLogger(c.getPackage().getName());
  }

  /**
   * This method is used to initialize the logger using the given property set
   * 
   * @param ps
   *          The property set to use for initialization values
   * @param logFileBase
   *          The directory path for the log file storage
   * @param p
   * @throws ExecuteException
   */
  public static void init(String logFileBase, Properties p) throws ExecuteException
  {
    Logger.fileNameBase = logFileBase;
    java.util.logging.Logger log = java.util.logging.Logger.getLogger(LOG_BASE);
    Handler[] hList = log.getHandlers();
    for (int i = 0; i < hList.length; i++)
    {
      log.removeHandler(hList[i]);
    }

    LogMode mode = LogMode.valueOf(p.getProperty("log-mode", "FILE"));
    setMode(log, mode);
    LogLevel level = LogLevel.valueOf(p.getProperty("log-level", "INFO"));
    setLevel(log, level);
  }

  /**
   * This method is used to log a debug message if the appropriate level is let
   * for the Logger
   * 
   * @param methodName
   *          The method the log entry was sent from
   * @param msg
   *          The message to be logged
   */
  public void logDebug(String methodName, String msg)
  {
    logger.logp(Level.FINE, c.getSimpleName(), methodName, msg);
  }

  /**
   * This method is used to log a debug message if the appropriate level is let
   * for the Logger
   * 
   * @param methodName
   *          The method the log entry was sent from
   */
  public void logDebug(String methodName, Throwable t)
  {
    logger.logp(Level.FINE, c.getSimpleName(), methodName, getAsMessage(t));
  }

  /**
   * This method gets the string representation of the Throwable's stack trace
   * 
   * @param t
   *          The fault entry to process
   * @return String
   */
  private String getAsMessage(Throwable t)
  {
    CharArrayWriter caw = new CharArrayWriter();
    PrintWriter pr = new PrintWriter(caw);
    t.printStackTrace(pr);
    pr.flush();
    pr.close();
    String value = caw.toString();
    return value;
  }

  /**
   * This method is used to log a info message if the appropriate level is let
   * for the Logger
   * 
   * @param methodName
   *          The method the log entry was sent from
   * @param msg
   *          The message to be logged
   */
  public void logInfo(String methodName, String msg)
  {
    logger.logp(Level.INFO, c.getSimpleName(), methodName, msg);
  }

  /**
   * This method is used to log a info message if the appropriate level is let
   * for the Logger
   * 
   * @param methodName
   *          The method the log entry was sent from
   * @param t
   *          The fault entry to be logged
   */
  public void logInfo(String methodName, Throwable t)
  {
    logger.logp(Level.INFO, c.getSimpleName(), methodName, getAsMessage(t));
  }

  /**
   * This method is used to log a warning message if the appropriate level is
   * let for the Logger
   * 
   * @param methodName
   *          The method the log entry was sent from
   * @param msg
   *          The message to be logged
   */
  public void logWarning(String methodName, String msg)
  {
    logger.logp(Level.WARNING, c.getSimpleName(), methodName, msg);
  }

  /**
   * This method is used to log a warning message if the appropriate level is
   * let for the Logger
   * 
   * @param methodName
   *          The method the log entry was sent from
   * @param t
   *          The fault entry to be logged
   */
  public void logWarning(String methodName, Throwable t)
  {
    logger.logp(Level.WARNING, c.getSimpleName(), methodName, getAsMessage(t));

  }

  /**
   * This method is used to log a severe message if the appropriate level is let
   * for the Logger
   * 
   * @param methodName
   *          The method the log entry was sent from
   * @param msg
   *          The message to be logged
   */
  public void logSevere(String methodName, String msg)
  {
    logger.logp(Level.SEVERE, c.getSimpleName(), methodName, msg);
  }

  /**
   * This method is used to log a severe message if the appropriate level is let
   * for the Logger
   * 
   * @param methodName
   *          The method the log entry was sent from
   * @param t
   *          The fault entry to be logged
   */
  public void logSevere(String methodName, Throwable t)
  {
    logger.logp(Level.SEVERE, c.getSimpleName(), methodName, getAsMessage(t));
  }

  /**
   * This method is used to get the available logging levels.
   * 
   * @return Array of LogLevels
   */
  public LogLevel[] getLevels()
  {
    return LogLevel.values();
  }

  /**
   * This method returns the list of available modes
   * 
   * @return Array of LogMode
   */
  public LogMode[] getModes()
  {
    return LogMode.values();
  }

  /**
   * This method returns the current logging level for this Logger. The logging
   * level is used to filter out log messages.
   * 
   * @return LogLevel
   */
  public LogLevel getCurrentLevel()
  {
    return getLevel(logger);
  }

  /**
   * This method returns the current loggin mode for this Logger. The logging
   * mode determines where the data is stored.
   * 
   * @return The current mode
   */
  public LogMode getCurrentMode()
  {
    return getMode(logger);
  }

  /**
   * This method allows the current logging level to be changed for subsequent
   * log messages.
   * 
   * @param level
   *          the new log level to use
   */
  public void setLevel(LogLevel level)
  {
    setLevel(logger, level);
  }

  /**
   * This method allows the current logging level for the system.
   * 
   * @param level
   *          the new log level to use
   */
  public void setMasterLevel(LogLevel level)
  {
    java.util.logging.Logger log = java.util.logging.Logger.getLogger(LOG_BASE);
    setLevel(log, level);
  }

  /**
   * This method sets the log level to the given level (translated)
   * 
   * @param log
   *          the base logger to set
   * @param level
   *          the new log level to use
   */
  private static void setLevel(java.util.logging.Logger log, LogLevel level)
  {
    Level newLevel;
    if (level == LogLevel.SEVERE)
    {
      newLevel = Level.SEVERE;
    }
    else if (level == LogLevel.WARNING)
    {
      newLevel = Level.WARNING;
    }
    else if (level == LogLevel.INFO)
    {
      newLevel = Level.INFO;
    }
    else if (level == LogLevel.DEBUG)
    {
      newLevel = Level.FINE;
    }
    else if (level == LogLevel.NONE)
    {
      newLevel = Level.OFF;
    }
    else
    {
      newLevel = Level.INFO;
    }
    log.setLevel(newLevel);
    Handler[] handler = log.getHandlers();
    for (int i = 0; i < handler.length; i++)
    {
      handler[i].setLevel(newLevel);
    }
  }

  /**
   * This method returns the translated log level
   * 
   * @param log
   *          the base logger to use
   * @return LogLevel
   */
  private static LogLevel getLevel(java.util.logging.Logger log)
  {
    Level testLevel = log.getLevel();
    if (testLevel == null)
    {
      java.util.logging.Logger parent = log.getParent();
      while (parent != null)
      {
        if (LOG_BASE.equals(parent.getName()))
        {
          testLevel = parent.getLevel();
          break;
        }
        else
        {
          parent = parent.getParent();
        }
      }
    }
    if (testLevel == Level.SEVERE)
    {
      return LogLevel.SEVERE;
    }
    else if (testLevel == Level.WARNING)
    {
      return LogLevel.WARNING;
    }
    else if (testLevel == Level.INFO)
    {
      return LogLevel.INFO;
    }
    else if (testLevel == Level.FINE)
    {
      return LogLevel.DEBUG;
    }
    else if (testLevel == Level.OFF)
    {
      return LogLevel.NONE;
    }
    else
    {
      return LogLevel.NONE;
    }
  }

  /**
   * This method sets the current mode (translated)
   * 
   * @param log
   *          the base logger to set
   * @param mode
   *          the log mode to use
   * @throws ExecuteException
   */
  private static void setMode(java.util.logging.Logger log, LogMode mode) throws ExecuteException
  {
    if (fh == null)
    {
      try
      {
        fh = new FileHandler(fileNameBase + "%g.log", 20000000, 10, true);
      }
      catch (Exception e)
      {
        throw new ExecuteException(e);
      }
    }
    if (ch == null)
    {
      ch = new ConsoleHandler();
      ch.setFormatter(new SimpleFormatter());

    }
    modeMap.put(log, mode);
    if (mode == LogMode.BOTH)
    {
      log.addHandler(fh);
      log.addHandler(ch);
    }
    else if (mode == LogMode.NORMAL)
    {
      log.removeHandler(fh);
      log.addHandler(ch);
    }
    else if (mode == LogMode.FILE)
    {
      log.addHandler(fh);
      log.removeHandler(ch);
    }
    else
    {
      log.addHandler(ch);
    }
  }

  /**
   * This method returns the translated log level
   * 
   * @param log
   *          the base logger to set
   * @return LogMode
   */
  private static LogMode getMode(java.util.logging.Logger log)
  {
    LogMode mode = modeMap.get(log);
    if (mode == null)
    {
      java.util.logging.Logger parent = log.getParent();
      while (parent != null)
      {
        if (LOG_BASE.equals(parent.getName()))
        {
          mode = modeMap.get(parent);
          break;
        }
        else
        {
          parent = parent.getParent();
        }
      }
    }
    return mode;
  }

  /**
   * This method is used to return the singleton instance of the Logger
   * 
   * @param c
   *          The class derive logger
   * @return typed singleton Logger
   */
  public synchronized static Logger getInstance(Class<?> c)
  {
    String name = c.getName();
    Logger result = singleton.get(name);
    if (result == null)
    {
      result = new Logger(c);
      singleton.put(name, result);
    }
    return result;
  }

  /**
   * This method is used to add a handler to the logger
   * 
   * @param handler
   *          new interested handler
   */
  public static void add(Handler handler)
  {
    java.util.logging.Logger log = java.util.logging.Logger.getLogger(LOG_BASE);
    log.addHandler(handler);
  }

}
