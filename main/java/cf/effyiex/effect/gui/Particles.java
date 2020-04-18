package cf.effyiex.effect.gui;

import static cf.effyiex.effect.rendering.Animator.getFrameMultiplier;
import static net.minecraft.client.gui.Gui.drawRect;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.opengl.Display;

@SuppressWarnings("serial")
public class Particles extends ArrayList<Particles.Particle> {
	
	private static final short BOUNDARY = 1000;
	private static final short MAX_SPEED = 3;
	private static final Random RANDOMIZER = new Random();
	
	static class Particle {
		
		short x, y, motion;
		
		public Particle spawn() {
			this.x = (short) (BOUNDARY + RANDOMIZER.nextInt(BOUNDARY));
			this.y = (short) (BOUNDARY + RANDOMIZER.nextInt(BOUNDARY));
			this.motion = (short)-RANDOMIZER.nextInt(MAX_SPEED);
			return this;
		}
		
		public void update(float multiplier) {
			this.y += motion * multiplier;
			this.x += motion * multiplier;
			if(this.y <= 0 || this.x <= 0) spawn();
		}
		
	}
	
	private short frameRate;
	
	public Particles(short frameRate, short times) {
		this.frameRate = frameRate;
		for(short s = 0; s < times; s++) {
			Particle p = (new Particle().spawn());
			if(p != null) this.add(p);
		}
	}
	
	public void render(Rectangle screen) {
		int size = Math.round(Display.getHeight() / 256.0F);
		for(Particle particle : Particles.this) {
			int x = screen.x + Math.round(screen.width / (float) BOUNDARY * particle.x);
			int y = screen.y + Math.round(screen.height / (float) BOUNDARY * particle.y);
			particle.update(getFrameMultiplier(frameRate));
			drawRect(x - size / 2, y - size / 2, x + size / 2, y + size / 2, new Color(255, 255, 255).getRGB());
		}
	}

}
