package org.mendybot.announcer.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.mendybot.announcer.common.model.dto.Announcement;
import org.mendybot.announcer.fault.ExecuteException;
import org.mendybot.announcer.log.Logger;

public class AnnouncerModel {
	private ArrayList<Announcement> played = new ArrayList<>();
	private AnnouncerOs os;
	private File archiveDir;

	public AnnouncerModel() {
		Properties p = new Properties();
		try {
			Logger.init("/var/log/mendybot/", p);
		} catch (ExecuteException e) {
			e.printStackTrace();
		}
		String osType = System.getProperty("os.name");
		if ("linux".equalsIgnoreCase(osType)) {
			initLinux();
		} else if ("Mac OS X".equalsIgnoreCase(osType)) {
			initOSX();
		} else {
			throw new RuntimeException("Unknown OS Type: " + osType);
		}
		archiveDir = new File("/opt/MendyBot/announcer/archive");
	}

	public File getArchiveDirectory() {
		return archiveDir;
	}

	private void initLinux() {
		File osInfo = new File("/etc/os-release");
		try {
			FileReader fr = new FileReader(osInfo);
			Properties p = new Properties();
			p.load(fr);
			fr.close();
			String osName = p.getProperty("ID");
			if ("raspbian".equalsIgnoreCase(osName)) {
				initRaspbian();
			} else if ("ubuntu".equalsIgnoreCase(osName)) {
				initUbunto();
			} else {
				throw new RuntimeException("Unknown OS ID: " + osName);
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void initOSX() {
		os = AnnouncerOs.OSX;
	}

	private void initRaspbian() {
		os = AnnouncerOs.RASPBIAN;
	}

	private void initUbunto() {
		os = AnnouncerOs.UBUNTO;
	}

	public AnnouncerOs getOs() {
		return os;
	}

	public void addPlayed(Announcement a) {
		played.add(a);
	}

	public List<Announcement> getPlayed() {
		return played;
	}

	public void setPlayed(Announcement a) {
		this.played = played;
	}
}
