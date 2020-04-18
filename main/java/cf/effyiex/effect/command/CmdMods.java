package cf.effyiex.effect.command;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.module.Module;

public class CmdMods extends Command {

	public CmdMods() { super("mods"); }

	@Override
	public void onUsage(String[] args) {
		String output = ("§fMods: ");
		for(Module mod : Effect.getModules())
			output += ((mod.isActive() ? "§a" : "§c") + mod.getName() + "§f, ");
		output = output.substring(0, output.length() - 4);
		respond(output);
	}

}
