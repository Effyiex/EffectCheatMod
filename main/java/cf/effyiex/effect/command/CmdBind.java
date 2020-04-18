package cf.effyiex.effect.command;

import org.lwjgl.input.Keyboard;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.module.Module;

public class CmdBind extends Command {

	public CmdBind() { super("bind"); }
	
	@Override
	public void onUsage(String[] args) {
		if(args.length > 1) {
			Module mod = Effect.getModule(args[1]);
			if(mod == null) {
				respond("Couldn't find the given Module!");
				return;
			}
			if(args.length > 2) {
				mod.setBind(Keyboard.getKeyIndex(args[2].toUpperCase()));
				respond("Bind of '" + mod.getName() + "' is now: " + Keyboard.getKeyName(mod.getBind()));
			} else respond("Bind of '" + mod.getName() + "' us currently: " + Keyboard.getKeyName(mod.getBind()));
		}else respond("Syntax Error.");
	}

}
