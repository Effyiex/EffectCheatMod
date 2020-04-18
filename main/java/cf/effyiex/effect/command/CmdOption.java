package cf.effyiex.effect.command;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.Setting;

public class CmdOption extends Command {

	public CmdOption() { super("option"); }

	@Override
	public void onUsage(String[] args) {
		if(args.length > 2) {
			Module mod = Effect.getModule(args[1]);
			if(mod != null) {
				for(Setting setting : mod.getSettings()) {
					if(setting.getID().equalsIgnoreCase(args[2])) {
						if(args.length > 3) {
							String value = EMPTY;
							for(int i = 3; i < args.length; i++)
								value += args[i];
							setting.setValue(value);
							Command.respond("Set the value of '" + setting.getID() + "' (" + mod.getName() + ") to: " + setting.toString());
						} else Command.respond("Current Value of '" + setting.getID() + "': " + setting.toString());
						break;
					}
				}
			}
		}else Command.respond("Syntax error.");
	}

}
