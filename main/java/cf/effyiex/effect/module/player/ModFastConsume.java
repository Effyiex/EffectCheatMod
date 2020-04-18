package cf.effyiex.effect.module.player;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;

import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class ModFastConsume extends Module {

	private static final C03PacketPlayer packet = new C03PacketPlayer(true);
	
	public ModFastConsume() {
		super("FastConsume", ModuleType.PLAYER);
		this.listenTo(WorldTickEvent.class);
	}

	@Override
	public void onEvent(Object event) {
		if(this.isActive() && CLIENT.gameSettings.keyBindUseItem.isKeyDown()
				&& CLIENT.thePlayer.getHeldItem() != null
				&& CLIENT.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
			CLIENT.thePlayer.sendQueue.addToSendQueue(packet);
		}
	}

}
