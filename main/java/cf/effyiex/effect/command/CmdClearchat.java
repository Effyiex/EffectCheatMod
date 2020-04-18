package cf.effyiex.effect.command;

import net.minecraft.util.ChatComponentText;

public class CmdClearchat extends Command {

	public CmdClearchat() { super("clearchat"); }

	@Override
	public void onUsage(String[] args) { for(short s = 0; s < 128; s++) CLIENT.thePlayer.addChatMessage(new ChatComponentText(" ")); }

}
