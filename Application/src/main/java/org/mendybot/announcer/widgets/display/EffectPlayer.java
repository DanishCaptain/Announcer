package org.mendybot.announcer.widgets.display;

import org.mendybot.announcer.log.Logger;
import org.mendybot.announcer.tools.CommandTool;
import org.mendybot.announcer.widgets.CommandWidget;

public class EffectPlayer extends CommandWidget implements MatrixDisplayWidget, Runnable {
	private static Logger LOG = Logger.getInstance(EffectPlayer.class);
	private static EffectPlayer singleton;
	private Thread t = new Thread(this);
	private boolean running;
	private String commandBase = "sudo /opt/rpi-rgb-led-matrix/examples-api-use/demo";
	private Effect effect;

	private EffectPlayer() {
		t.setName(getClass().getSimpleName());
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e1) {
			}
			if (effect != null) {
				play(effect);
			}
		}
	}

	@Override
	public void show(Effect effect) {
		this.effect = effect;
		synchronized (this) {
			notifyAll();
		}
	}

	private void play(Effect effect) {
		synchronized (this) {
			LOG.logInfo("play", "calling for " + effect);
			String command = createCommand(effect);
			CommandTool.execute("show", command);
		}
	}

	private String createCommand(Effect effect) {
		/*
		 * Options: -D <demo-nr> : Always needs to be set -t <seconds> : Run for
		 * these number of seconds, then exit. --led-gpio-mapping=<name> : Name
		 * of GPIO mapping used. Default "regular" --led-rows=<rows> : Panel
		 * rows. Typically 8, 16, 32 or 64. (Default: 32). --led-cols=<cols> :
		 * Panel columns. Typically 32 or 64. (Default: 32).
		 * --led-chain=<chained> : Number of daisy-chained panels. (Default: 1).
		 * --led-parallel=<parallel> : Parallel chains. range=1..3 (Default: 1).
		 * --led-multiplexing=<0..6> : Mux type: 0=direct; 1=Stripe;
		 * 2=Checkered; 3=Spiral; 4=ZStripe; 5=ZnMirrorZStripe; 6=coreman
		 * (Default: 0) --led-pixel-mapper : Semicolon-separated list of
		 * pixel-mappers to arrange pixels. Optional params after a colon e.g.
		 * "U-mapper;Rotate:90" Available: "Rotate", "U-mapper". Default: ""
		 * --led-pwm-bits=<1..11> : PWM bits (Default: 11).
		 * --led-brightness=<percent>: Brightness in percent (Default: 100).
		 * --led-scan-mode=<0..1> : 0 = progressive; 1 = interlaced (Default:
		 * 0). --led-row-addr-type=<0..2>: 0 = default; 1 = AB-addressed panels;
		 * 2 = direct row select(Default: 0). --led-show-refresh : Show refresh
		 * rate. --led-inverse : Switch if your matrix has inverse colors on.
		 * --led-rgb-sequence : Switch if your matrix has led colors swapped
		 * (Default: "RGB") --led-pwm-lsb-nanoseconds : PWM Nanoseconds for LSB
		 * (Default: 130) --led-pwm-dither-bits=<0..2> : Time dithering of lower
		 * bits (Default: 0) --led-no-hardware-pulse : Don't use hardware
		 * pin-pulse generation. --led-slowdown-gpio=<0..2>: Slowdown GPIO.
		 * Needed for faster Pis/slower panels (Default: 1). --led-daemon : Make
		 * the process run in the background as daemon. --led-no-drop-privs :
		 * Don't drop privileges from 'root' after initializing the hardware.
		 * Demos, choosen with -D 0 - some rotating square 1 - forward scrolling
		 * an image (-m <scroll-ms>) 2 - backward scrolling an image (-m
		 * <scroll-ms>) 3 - test image: a square 4 - Pulsing color 5 - Grayscale
		 * Block 6 - Abelian sandpile model (-m <time-step-ms>) 7 - Conway's
		 * game of life (-m <time-step-ms>) 8 - Langton's ant (-m
		 * <time-step-ms>) 9 - Volume bars (-m <time-step-ms>) 10 - Evolution of
		 * color (-m <time-step-ms>) 11 - Brightness pulse generator Example:
		 * ./demo -t 10 -D 1 runtext.ppm Scrolls the runtext for 10 seconds
		 */
		StringBuilder command = new StringBuilder(commandBase);
		// -t 10 -D 1 runtext.ppm";
		// command.append(" --led-no-hardware-pulse");
		command.append(" --led-rows=32");
		command.append(" --led-cols=32");
		command.append(" --led-chain=1");
		command.append(" -t " + effect.getTValue());
		command.append(" -D " + effect.getDValue());
		return command.toString();
	}

	public synchronized static EffectPlayer getInstance() {
		if (singleton == null) {
			singleton = new EffectPlayer();
		}
		return singleton;
	}

	@Override
	public void show(DisplayText text) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public void show(ImageFile file) {
		throw new RuntimeException("not implemented");
	}
}
