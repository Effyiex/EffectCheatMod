package cf.effyiex.effect.module.world;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;
import cf.effyiex.effect.utils.AccessHelper;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModSlowmotion extends Module {

	private Setting value = new Setting("Value", 0.5F, SettingType.FLOAT);
	
	public ModSlowmotion() { 
		super("Slowmotion", ModuleType.WORLD);
		this.listenTo(PlayerTickEvent.class);
	}
	
	private float saved = 0.0F;
	
	@Override
	public void toggle() {
		super.toggle();
		if(this.isActive()) saved = ((Timer) AccessHelper.hackGet(AccessHelper.timer)).timerSpeed;
		else this.setValue(saved);
	}
	
	@Override
	public void onEvent(Object event) { if(this.isActive()) this.setValue(value.toFloat()); }

	private void setValue(float value) {
		((Timer)AccessHelper.hackGet(AccessHelper.timer)).timerSpeed = value;
	}
	
}
