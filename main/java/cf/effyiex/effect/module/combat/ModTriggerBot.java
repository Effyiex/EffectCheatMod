package cf.effyiex.effect.module.combat;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.utils.Targetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModTriggerBot extends Module {

	public ModTriggerBot() {
		super("TriggerBot", ModuleType.COMBAT);
		this.listenTo(PlayerTickEvent.class);
	}

	@Override
	public void onEvent(Object event) {
		ModReach reach = ((ModReach)Effect.getModule("Reach"));
		Entity pointedEntity = Targetting.getTargetOver(reach.isActive() ? reach.getSetting("Value").toFloat() : 3.9F);
		if(this.isActive() && pointedEntity != null) {
			if(pointedEntity instanceof EntityPlayer && pointedEntity != CLIENT.thePlayer)
				if(pointedEntity.hurtResistantTime <= 0) {
					CLIENT.playerController.attackEntity(CLIENT.thePlayer, pointedEntity);
					CLIENT.thePlayer.swingItem();
				}
		}
	}

}
