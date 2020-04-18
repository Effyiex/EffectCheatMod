package cf.effyiex.effect.module.render;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import net.minecraftforge.client.event.RenderWorldEvent;

public class ModBlockESP extends Module {

	public ModBlockESP() {
		super("BlockESP", ModuleType.RENDER);
		this.listenTo(RenderWorldEvent.Post.class);
	}

	@Override
	public void onEvent(Object obj) {
		if(this.isActive()) {
			//RenderWorldEvent.Post event = (RenderWorldEvent.Post) obj;
			
		}
	}

}
