package cf.effyiex.effect.module.player;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.utils.AccessHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModFastplace extends Module {

	private int origin = -1;
	
	public ModFastplace() {
		super("Fastplace", ModuleType.PLAYER);
		this.listenTo(PlayerTickEvent.class);
	}

	@Override
	public void onEvent(Object event) {
		if(this.isActive()) {
			if(origin < 0)
				origin = ((Integer)AccessHelper.hackGet(AccessHelper.rightClickDelayTimer));
			AccessHelper.hackSet(AccessHelper.rightClickDelayTimer, 0);
		}else if(origin > 0) {
			AccessHelper.hackSet(AccessHelper.rightClickDelayTimer, origin);
			origin = -1;
		}
	}

}
