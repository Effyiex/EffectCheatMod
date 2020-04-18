package cf.effyiex.effect.module.player;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ModNofall extends Module {

	private static final C03PacketPlayer packet = new C03PacketPlayer(true);
	
	public ModNofall() {
		super("Nofall", ModuleType.PLAYER);
		this.listenTo(WorldTickEvent.class);
	}

	@Override
	public void onEvent(Object event) {
		if(this.isActive() && CLIENT.thePlayer.fallDistance > 2.5F)
			CLIENT.thePlayer.sendQueue.addToSendQueue(packet);
	}

}
