package org.mendybot.announcer.tools;

import org.mendybot.announcer.log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class CommandTool {
	private static Logger LOG = Logger.getInstance(CommandTool.class);

	private CommandTool() {
	}

	public static void execute(String info, String command) {
		Runtime run = Runtime.getRuntime();
		try {
			LOG.logInfo("execute", "calling for " + info);
			LOG.logInfo("execute", command);
			Process proc = run.exec(command);
			LOG.logDebug("execute", "starting for " + command);
			proc.waitFor();
			LOG.logDebug("execute", "ending for " + command);
			BufferedReader is = new BufferedReader(new InputStreamReader((proc.getInputStream())));
			String line;
			while ((line = is.readLine()) != null) {
				LOG.logDebug("execute", line);
			}
			is.close();
			is = new BufferedReader(new InputStreamReader((proc.getErrorStream())));
			while ((line = is.readLine()) != null) {
				LOG.logSevere("execute", "E: " + line);
			}
			is.close();
		} catch (IOException e) {
			LOG.logSevere("execute", e);
		} catch (InterruptedException e) {
			LOG.logInfo("execute", e);
		}
	}

	public static void execute(String info, String[] command) {
		Runtime run = Runtime.getRuntime();
		try {
			LOG.logInfo("execute", "calling for " + info);
			// LOG.logInfo("execute", command);
			Process proc = run.exec(command);
			LOG.logDebug("execute", "starting for " + command);
			proc.waitFor();
			LOG.logDebug("execute", "ending for " + command);
			BufferedReader is = new BufferedReader(new InputStreamReader((proc.getInputStream())));
			String line;
			while ((line = is.readLine()) != null) {
				LOG.logDebug("execute", line);
			}
			is.close();
			is = new BufferedReader(new InputStreamReader((proc.getErrorStream())));
			while ((line = is.readLine()) != null) {
				LOG.logSevere("execute", "E: " + line);
			}
			is.close();
		} catch (IOException e) {
			LOG.logSevere("execute", e);
		} catch (InterruptedException e) {
			LOG.logInfo("execute", e);
		}
	}

}
