package cf.effyiex.effect.event;

import cf.effyiex.effect.Reference;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CustomEvent extends Event implements Reference {
	
	public static class GameRenderEvent extends CustomEvent {
		
		public float partialTicks;
		public int width, height;
		
		public GameRenderEvent(float partialTicks) {
			ScaledResolution resolution = new ScaledResolution(CLIENT);
			this.width = resolution.getScaledWidth();
			this.height = resolution.getScaledHeight();
			this.partialTicks = partialTicks;
		}
		
	}
	
	@Cancelable
	public static class CameraHurtEffectEvent extends CustomEvent {}
	
	public static class WorldChangeEvent extends CustomEvent {
		
		public WorldClient pre, post;
		
		public WorldChangeEvent(WorldClient pre, WorldClient post) {
			this.pre = pre;
			this.post = post;
		}
		
	}
	
	public static class LivingRenderEvent extends CustomEvent {
		
		public Entity entity;
		public double x, y, z;
		
		public LivingRenderEvent(Entity entity, double x, double y, double z) {
			this.entity = entity;
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
	}
	
	public static class ScreenRenderEvent extends CustomEvent {
		
		public float partialTicks;
		public int mouseX, mouseY;
		
		public ScreenRenderEvent(float partialTicks, int mouseX, int mouseY) {
			this.partialTicks = partialTicks;
			this.mouseX = mouseX;
			this.mouseY = mouseY;
		}
		
	}
	
	public static class PostInitializationEvent extends CustomEvent {}

}
