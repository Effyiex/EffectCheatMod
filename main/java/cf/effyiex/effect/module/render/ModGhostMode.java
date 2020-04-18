package cf.effyiex.effect.module.render;

import org.lwjgl.opengl.Display;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;

public class ModGhostMode extends Module {

	private String originalTitle;
	
	public ModGhostMode() {
		super("GhostMode", ModuleType.RENDER);
		this.originalTitle = Display.getTitle();
		Display.setTitle(AD);
	}

	@Override
	public void toggle() {
		super.toggle();
		if(this.isActive()) {
			Display.setTitle(originalTitle);
		}else{
			Display.setTitle(AD);
		}
	}
	
	@Override
	public void onEvent(Object event) {}

}
