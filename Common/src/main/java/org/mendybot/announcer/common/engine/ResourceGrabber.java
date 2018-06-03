package org.mendybot.announcer.common.engine;

import java.io.File;
import java.util.ArrayList;

import org.mendybot.announcer.common.model.dto.ArchiveResource;
import org.mendybot.announcer.fault.ExecuteException;
import org.mendybot.announcer.log.Logger;

public class ResourceGrabber implements Runnable {
  private static final Logger LOG = Logger.getInstance(ResourceGrabber.class);
  private ArrayList<ArchiveResource> arList = new ArrayList<>();
  private ArrayList<File> fList = new ArrayList<>();
  private Thread t = new Thread(this);
	private EngineClient engine;
  private boolean running;
	
	public ResourceGrabber(EngineClient engine)
	{
	  this.engine = engine;
	  t.setName(getClass().getName());
	  t.setDaemon(true);
	  t.start();
	}
	
    public void grab(ArchiveResource ar, File f) throws ExecuteException
    {
      synchronized (arList)
      {
        if (!arList.contains(ar)) {
          arList.add(ar);
          fList.add(f);
          //LOG.logDebug("grab", ""+arList);
        } else {
          LOG.logDebug("grab", "skipping: "+ar);
        }
      }
    }
    
    @Override
    public void run() 
    {
      running = true;
      while(running) {
          if (arList.size() > 0) {
            ArchiveResource ar;
            File f;
            synchronized (arList) {
              ar = arList.get(0);
              f = fList.get(0);
            }
            try
            {
              engine.callResource(ar, f);
            }
            catch (ExecuteException e)
            {
              e.printStackTrace();
            }
            synchronized (arList) {
              arList.remove(0);
              fList.remove(0);
            }
            Thread.yield();
          } else {
            try
            {
              Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
              running = false;
            }
          }
      }
    }

}
