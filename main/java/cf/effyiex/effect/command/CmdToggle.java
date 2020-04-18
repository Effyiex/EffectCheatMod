package cf.effyiex.effect.command;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.module.Module;

public class CmdToggle extends Command {

	public CmdToggle() { super("toggle"); }

	@Override
	public void onUsage(String[] args) {
		for(Module module : Effect.getModules())
			if(module.getName().equalsIgnoreCase(args[1])) {
				module.toggle();
				String state = (module.isActive() ? "§aON" : "§cOFF");
				respond("Toggled " + module.getName() + " to " + state);
				break;
			}
	}

}
