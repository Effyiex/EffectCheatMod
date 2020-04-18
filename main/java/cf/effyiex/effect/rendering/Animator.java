package cf.effyiex.effect.rendering;

import net.minecraft.client.Minecraft;

public class Animator {
	
	public static float getFrameMultiplier(float limiter) {
		return (limiter / Minecraft.getDebugFPS());
	}
	
}
