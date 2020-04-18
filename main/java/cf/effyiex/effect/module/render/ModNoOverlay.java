package cf.effyiex.effect.module.render;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;

public class ModNoOverlay extends Module {

	public ModNoOverlay() {
		super("NoBlockOverlay", ModuleType.RENDER);
		this.listenTo(RenderBlockOverlayEvent.class);
	}

	@Override
	public void onEvent(Object event) {
		if(this.isActive()) {
			((RenderBlockOverlayEvent)event).setCanceled(true);
		}
	}

}
