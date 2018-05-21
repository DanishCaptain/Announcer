package org.mendybot.announcer.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import org.mendybot.announcer.model.AnnouncerModel;
import org.mendybot.announcer.model.AnnouncerOs;
import org.mendybot.announcer.widgets.display.*;
import org.mendybot.announcer.widgets.sound.APlayer;
import org.mendybot.announcer.widgets.sound.OmxPlayer;
import org.mendybot.announcer.widgets.sound.PlayFile;
import org.mendybot.announcer.widgets.sound.SoundWidget;
import org.mendybot.announcer.widgets.speech.CepstralSpeaker;
import org.mendybot.announcer.widgets.speech.OsxSay;
import org.mendybot.announcer.widgets.speech.SayText;
import org.mendybot.announcer.widgets.speech.SpeechWidget;

import com.sun.net.httpserver.HttpExchange;

public class RequestHandler extends BaseHandler {
	public static final String P_IS_ALARM = "is-alarm";
	public static final int DEFAULT_SOUND_LEVEL = 65;
	public static final String P_SOUND_LEVEL = "sound-level";
	public static final String P_SAY_TEXT = "say";
	public static final String P_SAY_TEXT_REPEAT = "say-repeat";
	public static final String P_DISPLAY_TEXT = "display";
	public static final String P_DISPLAY_TEXT_FONT = "display-font";
	public static final String P_DISPLAY_TEXT_RGB = "display-rgb";
	public static final String P_DISPLAY_TEXT_REPEAT = "display-repeat";
	private SoundWidget soundEngine;
	private SpeechWidget speakerEngine;
	private MatrixDisplayWidget textEngine;
	private MatrixDisplayWidget imageEngine;
	private MatrixDisplayWidget effectEngine;

	public RequestHandler(AnnouncerModel model) {
		super(model);
		if (model.getOs() == AnnouncerOs.RASPBIAN) {
			soundEngine = APlayer.getInstance();
			speakerEngine = CepstralSpeaker.getInstance(model, soundEngine);
			textEngine = ScrollingTextPlayer.getInstance();
			imageEngine = ImagePlayer.getInstance();
			effectEngine = EffectPlayer.getInstance();
		} else if (model.getOs() == AnnouncerOs.UBUNTO) {
			soundEngine = OmxPlayer.getInstance();
			speakerEngine = CepstralSpeaker.getInstance(model, soundEngine);
			textEngine = ScrollingTextPlayer.getInstance();
			imageEngine = ImagePlayer.getInstance();
			effectEngine = EffectPlayer.getInstance();
		} else if (model.getOs() == AnnouncerOs.OSX) {
			soundEngine = OmxPlayer.getInstance();
			speakerEngine = OsxSay.getInstance();
			textEngine = ScrollingTextPlayer.getInstance();
			imageEngine = ImagePlayer.getInstance();
			effectEngine = EffectPlayer.getInstance();
		} else {
			throw new RuntimeException("Unknown AnnouncerOs " + model.getOs());
		}
	}

	@Override
	public void handle(HttpExchange ex) throws IOException {
		try {
			String requestMethod = ex.getRequestMethod();
			if (requestMethod.equalsIgnoreCase("POST")) {
				String json = new BufferedReader(new InputStreamReader(ex.getRequestBody())).lines()
						.collect(Collectors.joining("\n"));
				ex.getResponseHeaders().set("Content-Type", "application/json");

				JSONObject jObj = new JSONObject(json);
				boolean isAlarm = jObj.getBoolean(P_IS_ALARM);
				int soundLevel = jObj.getInt(P_SOUND_LEVEL);
				String say = jObj.getString(P_SAY_TEXT);
				int repeatSay = jObj.getInt(P_SAY_TEXT_REPEAT);
				String display = jObj.getString(P_DISPLAY_TEXT);
				String font = jObj.getString(P_DISPLAY_TEXT_FONT);
				String rgb = jObj.getString(P_DISPLAY_TEXT_RGB);
				int repeatDisplay = jObj.getInt(P_DISPLAY_TEXT_REPEAT);

				JSONObject jsonR = new JSONObject();
				jsonR.put("result", "Request submitted");
				String response = jsonR.toString();
				ex.sendResponseHeaders(200, response.length());
				OutputStream os = ex.getResponseBody();
				os.write(response.getBytes());
				os.flush();
				os.close();

				soundEngine.checkSoundLevel(15);

				if (isAlarm) {
					// Effect effect = new Effect();
					// effect.setRepeat(1);
					// effect.setTValue(3);
					// effectEngine.show(effect);
					soundEngine.submit(new PlayFile(new File(getModel().getArchiveDirectory(), "ALARM.wav")));
					// effectEngine.show(effect);

					int ms = 10;
					int t = 3;
					ImageFile igf = new ImageFile(new File(getModel().getArchiveDirectory(), "logo.ppm"));
					igf.setMs(ms);
					igf.setT(t);
					imageEngine.show(igf);
				}

				if (say != null && !"".equals(say.trim())) {
					SayText st = new SayText(say);
					st.setRepeat(repeatSay);
					st.setSoundLevel(soundLevel);
					speakerEngine.say(st);
				}
				if (display != null && !"".equals(display.trim())) {
					DisplayText dt = new DisplayText(display);
					dt.setFont(font);
					dt.setRGB(rgb);
					dt.setRepeat(repeatDisplay);
					textEngine.show(dt);
				}

			} else {
				ex.getResponseHeaders().set("Content-Type", "application/json");
				ex.sendResponseHeaders(200, 0);
				OutputStream os = ex.getResponseBody();
				JSONObject json = new JSONObject();
				json.put("error", "Post Expected");
				String response = json.toString();
				os.write(response.getBytes());
				os.close();
			}
		} catch (JSONException e) {
			throw new IOException(e);
		}
	}

}
