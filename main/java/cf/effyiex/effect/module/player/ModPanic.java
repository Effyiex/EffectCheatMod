package cf.effyiex.effect.module.player;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.command.Command;
import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import net.minecraft.util.ChatComponentText;

public class ModPanic extends Module {

	public ModPanic() { super("Panic", ModuleType.PLAYER); }

	@Override
	public void toggle() {
		for(Module module : Effect.getModules()) {
			if(module.isActive()) module.toggle();
		}
		Command.respond("Toggled everything off for you!");
		Command.respond("Clearing the whole chat in 5s");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(5000L);
					for(short s = 0; s < 100; s++)
						CLIENT.thePlayer.addChatMessage(new ChatComponentText(EMPTY));
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
			
		}).start();
	}
	
	@Override
	public void onEvent(Object event) {}

}
