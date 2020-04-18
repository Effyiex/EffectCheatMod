package cf.effyiex.effect.module.combat;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModAntiVelocity extends Module {

	private Setting death = new Setting("FakeDeath", false, SettingType.BOOLEAN);
	private Setting horizontal = new Setting("Horizontal", 0.0F, SettingType.FLOAT, -2.0F, 2.0F);
	private Setting vertical = new Setting("Vertical", 0.0F, SettingType.FLOAT, 0.0F, 10.0F);
	
	public ModAntiVelocity() {
		super("AntiVelocity", ModuleType.COMBAT);
		this.listenTo(PlayerTickEvent.class);
		this.add(death);
		this.add(horizontal);
		this.add(vertical);
	}
	
	@Override
	public void onEvent(Object event) {
		if(this.isActive() && CLIENT.thePlayer.hurtTime > 0.0F 
				&& CLIENT.thePlayer.hurtTime > CLIENT.thePlayer.maxHurtTime - 1) {
			if(death.toBoolean()) {
				CLIENT.thePlayer.setDead();
			}else{
				CLIENT.thePlayer.motionX *= horizontal.toFloat();
				CLIENT.thePlayer.motionY *= vertical.toFloat();
				CLIENT.thePlayer.motionZ *= horizontal.toFloat();
			}
		}
	}

}
