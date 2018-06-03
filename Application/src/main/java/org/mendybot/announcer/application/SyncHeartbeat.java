package org.mendybot.announcer.application;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.mendybot.announcer.common.engine.EngineClient;
import org.mendybot.announcer.common.model.dto.Announcement;
import org.mendybot.announcer.common.model.dto.Archive;
import org.mendybot.announcer.common.model.dto.ArchiveResource;
import org.mendybot.announcer.common.model.dto.Cube;
import org.mendybot.announcer.common.model.dto.SyncResponse;
import org.mendybot.announcer.controller.AnnouncerHandler;
import org.mendybot.announcer.fault.ExecuteException;
import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.model.AnnouncerModel;

public class SyncHeartbeat extends TimerTask {
	private static final Logger LOG = Logger.getInstance(SyncHeartbeat.class);
	private HashMap<String, ArchiveResource> arMap = new HashMap<>();
	private AnnouncerModel model;
	private EngineClient c;
	private String host;
	private UUID uuid;
	private AnnouncerHandler announcer;

	public SyncHeartbeat(AnnouncerModel model, AnnouncerHandler announcer) {
		this.model = model;
		this.announcer = announcer;
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			host = "Unknown";
		}
		uuid = UUID.fromString("789faf7f-5821-4010-b9a5-b855d139fb65");
		c = new EngineClient();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(this, 0, 10 * 1000);
	}

	@Override
	public void run() {
		Cube cube = new Cube();
		cube.setUuid(uuid);
		cube.setName(host);
		File archiveDir = model.getArchiveDirectory();
		File[] files = archiveDir.listFiles();
		for (File f : files) {
			if (f.getName().endsWith(".wav")) {
				if (!f.getName().equals("say.wav")) {
					cube.getArchive().addSoundFile(this.lookupResorce(f));
				}
			} else if (f.getName().endsWith(".ppm")) {
				cube.getArchive().addImageFile(lookupResorce(f));
			}
		}

		try {
			SyncResponse response = c.sendSync(cube);
			Archive a = response.getArchive();
			List<ArchiveResource> images = a.getImageFiles();
			checkImages(images);
			List<ArchiveResource> sounds = a.getSoundFiles();
			checkSounds(sounds);
			List<Announcement> announcements = response.getAnnouncements();
			checkAnnouncements(announcements);

		} catch (ExecuteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void checkImages(List<ArchiveResource> images) throws ExecuteException {
		File archiveDir = model.getArchiveDirectory();
		for (ArchiveResource ar : images) {
			File f = new File(archiveDir, ar.getName());
			if (f.exists()) {
				ArchiveResource existing = lookupResorce(f);
				if (existing.getSize() != ar.getSize()) {
					LOG.logDebug("checkImages", "*-->" + ar + "::" + existing + "::" + ar.getTsString() + "::" + existing.getTsString());
					c.grabResource(ar, f);
					clearResorce(f);
				} else {
					if (existing.getTs() != ar.getTs()) {
						LOG.logDebug("checkImages", "#-->" + ar + "::" + existing + "::" + ar.getTsString() + "::" + existing.getTsString());
						c.grabResource(ar, f);
						clearResorce(f);
					}
				}
			} else {
				LOG.logDebug("checkImages", "-->" + ar + "::" + ar.getTsString());
				c.grabResource(ar, f);
			}
		}
	}

	private void checkSounds(List<ArchiveResource> sounds) throws ExecuteException {
		File archiveDir = model.getArchiveDirectory();
		for (ArchiveResource ar : sounds) {
			File f = new File(archiveDir, ar.getName());
			if (f.exists()) {
				ArchiveResource existing = lookupResorce(f);
				if (existing.getSize() != ar.getSize()) {
					LOG.logDebug("checkSounds", "*-->" + ar + "::" + existing + "::" + ar.getTsString() + "::" + existing.getTsString());
					c.grabResource(ar, f);
					clearResorce(f);
				} else {
					if (existing.getTs() != ar.getTs()) {
						LOG.logDebug("checkSounds", "#-->" + ar + "::" + existing + "::" + ar.getTsString() + "::" + existing.getTsString());
						c.grabResource(ar, f);
						clearResorce(f);
					}
				}
			} else {
				LOG.logDebug("checkSounds", "-->" + ar + "::" + ar.getTsString());
				c.grabResource(ar, f);
			}
		}
	}

	private void checkAnnouncements(List<Announcement> announcements) throws ExecuteException {
		LOG.logInfo("checkAnnouncements", "*-->" + announcements);
		announcer.play(announcements);
		
	}

	private synchronized ArchiveResource lookupResorce(File file) {
		String name = file.getName();
		ArchiveResource ar = arMap.get(name);
		if (ar == null) {
			ar = new ArchiveResource();
			ar.setName(name);
			ar.setSize(file.length());
			ar.setTs(file.lastModified());
			arMap.put(name, ar);
		}
		return ar;
	}

	private synchronized void clearResorce(File file) {
		String name = file.getName();
		ArchiveResource ar = arMap.get(name);
		if (ar != null) {
			arMap.remove(name);
		}
	}

}
