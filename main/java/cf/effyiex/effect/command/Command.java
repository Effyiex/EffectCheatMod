package cf.effyiex.effect.command;

import cf.effyiex.effect.Reference;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public abstract class Command implements Reference {
	
	private String label;
	
	public Command(String label) {
		this.label = label;
	}
	
	public String getLabel() { return label; }
	
	public static void respond(String message) {
		message = (CHAT_PREFIX + message);
		IChatComponent chat = new ChatComponentText(message);
		CLIENT.thePlayer.addChatMessage(chat);
	}
	
	public abstract void onUsage(String[] args);

}
