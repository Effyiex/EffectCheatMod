package cf.effyiex.effect.command;

import cf.effyiex.effect.utils.FileHelper;

public class CmdConfig extends Command {

	public CmdConfig() { super("config"); }

	@Override
	public void onUsage(String[] args) {
		if(args[1].equalsIgnoreCase("load")) {
			FileHelper.loadConfig(args[2]);
			respond("Loaded the config '" + args[2] + "'");
		}else if(args[1].equalsIgnoreCase("save")) {
			FileHelper.saveConfig(args[2]);
			respond("Saved current config as '" + args[2] + "'");
		}else respond("Syntax Error. Arg1 -> save/load");
	}

}
