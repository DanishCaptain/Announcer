package org.mendybot.announcer.widgets.speech;

import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.tools.CommandTool;
import org.mendybot.announcer.widgets.CommandWidget;

public class OsxSay extends CommandWidget implements SpeechWidget {
	private final static Logger LOG = Logger.getInstance(OsxSay.class);
	private static OsxSay singleton;

	private OsxSay() {
	}

	@Override
	public void say(SayText st) {
		LOG.logInfo("say", "say " + st.getText());
		synchronized (this) {
			// say -v Karen "Hello Jim"
			// String command = "/usr/bin/osascript -e \"set volume output
			// volume '"+st.getSoundLevel()+"'\"";
			String command = "/usr/bin/osascript -e \"set\"";
			CommandTool.execute("soundlevel ", command);
			command = "say -v Karen \"" + st.getText() + "\"";
			for (int i = 0; i < st.getRepeat(); i++) {
				CommandTool.execute("for ", command);
			}
		}
	}

	public synchronized static OsxSay getInstance() {
		if (singleton == null) {
			singleton = new OsxSay();
		}
		return singleton;
	}

}
