package cf.effyiex.effect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.FMLClientHandler;

public interface Reference {
	
	String ID = "Effect";
	String BUILD = "Alpha 0.1";
	
	String AD = (ID + " [" + BUILD + "] by Effyiex");
	
	char COMMAND_PREFIX = '#'; 
	
	String CHAT_PREFIX = ("§8[§5" + ID + "§8]: §d");
	
	ClientCommandHandler COMMAND_HANDLER = ClientCommandHandler.instance;
	Minecraft CLIENT = FMLClientHandler.instance().getClient();
	
	FontRenderer FONT_RENDERER = CLIENT.fontRendererObj;

	String EMPTY = ( "" );
	
}
