package cf.effyiex.effect.module.combat;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModBowspammer extends Module {

	private static final C03PacketPlayer packet = new C03PacketPlayer(false);
	
	public ModBowspammer() { 
		super("Bowspammer", ModuleType.COMBAT);
		this.listenTo(PlayerTickEvent.class);
	}

	@Override
	public void onEvent(Object event) {
		if(this.isActive() && CLIENT.thePlayer.isUsingItem() && CLIENT.thePlayer.getHeldItem() != null) {
			if(CLIENT.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
				for(short s = 0; s < 20; s++) CLIENT.thePlayer.sendQueue.addToSendQueue(packet);
				CLIENT.playerController.onStoppedUsingItem(CLIENT.thePlayer);
			}
		}
	}

}
