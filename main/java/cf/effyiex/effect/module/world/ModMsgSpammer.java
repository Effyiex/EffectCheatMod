package cf.effyiex.effect.module.world;

import java.util.Random;

import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ModMsgSpammer extends Module {

	private Setting message = new Setting("Message", AD.replace("[", "").replace("]", ""), SettingType.STRING);
	private Setting maxDelay = new Setting("Delay", 20, SettingType.INTEGER, 0, 1000);
	private Setting obviouscate = new Setting("Obviouscate", true, SettingType.BOOLEAN);
	
	private Random random;
	
	public ModMsgSpammer() {
		super("MsgSpammer", ModuleType.WORLD);
		this.listenTo(PlayerTickEvent.class);
		this.add(message);
		this.add(maxDelay);
		this.add(obviouscate);
		this.random = new Random();
	}
	
	short delay = 0;

	@Override
	public void onEvent(Object event) {
		if(this.isActive()) {
			if(delay > 0) delay--;
			else {
				String suffix = "";
				if(obviouscate.toBoolean()) 
					suffix += ("[" + (100 + random.nextInt(899)) + ", " + (100 + random.nextInt(899)) + "]");
				CLIENT.thePlayer.sendChatMessage(message.toString() + suffix);
				delay = Short.parseShort(maxDelay.toString());
			}
		}else delay = 0;
	}

}
