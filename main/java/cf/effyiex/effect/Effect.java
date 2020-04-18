package cf.effyiex.effect;

import static cf.effyiex.effect.Reference.BUILD;
import static cf.effyiex.effect.Reference.CLIENT;
import static cf.effyiex.effect.Reference.FONT_RENDERER;
import static cf.effyiex.effect.Reference.ID;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import cf.effyiex.effect.command.Command;
import cf.effyiex.effect.event.CustomEvent.PostInitializationEvent;
import cf.effyiex.effect.event.EventCatcher;
import cf.effyiex.effect.gui.GuiClickGui;
import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.utils.AccessHelper;
import cf.effyiex.effect.utils.Cryption;
import cf.effyiex.effect.utils.FileHelper;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = ID, version = BUILD, clientSideOnly = true) 
public class Effect {
    
	@Instance(ID)
	@SidedProxy(clientSide = "cf.effyiex.effect.Effect")
	public static Effect mod;
	
	private ResourceLocation logo;
	
	private ArrayList<Module> modules;
	private ArrayList<Command> commands;
	@SuppressWarnings("unused")
	private ArrayList<String> friends;
	
    @EventHandler
    public void initialize(FMLInitializationEvent event) {
    	if(event.getSide().equals(Side.SERVER)) return;
    	Effect.mod = ((Effect)this);
    	MinecraftForge.EVENT_BUS.register(new EventCatcher());
    	AccessHelper.initialize();
    	this.initializeArrays();
    	FileHelper.initializeWorkspace();
    	this.postInitialization();
    	FileHelper.loadConfig("auto");
    	try {
    		DynamicTexture texture = new DynamicTexture(
    			ImageIO.read(this.getClass().getResource("assets/logo.png"))
    		);
    		this.logo = CLIENT.getTextureManager().getDynamicTextureLocation("effectLogo", texture);
		} catch (IOException e) { e.printStackTrace(); }
    }
    
    public void initializeArrays() {
    	this.modules = new ArrayList<Module>();
    	this.commands = new ArrayList<Command>();
    	this.friends = new ArrayList<String>();
    	try {
        	InputStream stream = this.getClass().getResourceAsStream("Registry.client");
        	String text = IOUtils.toString(stream);
			IOUtils.closeQuietly(stream);
        	for(String line : text.split("\n")) {
        		String[] args = Cryption.splitTextByChar(line.replace("\n", "").replace("\r", ""), '.');
        		String path = ("cf.effyiex.effect.module.");
        		if(args[0].equalsIgnoreCase("command"))
        			path = ("cf.effyiex.effect.command." + args[1]);
        		else path += (args[0] + '.' + args[1]);
        		if(args.length < 2 || args[0].isEmpty() || args[1].isEmpty()) continue;
				Class<?> classObj = Class.forName(path);
				if(args[0].equalsIgnoreCase("command"))
					this.commands.add((Command)classObj.newInstance());
				else this.modules.add((Module)classObj.newInstance());
        	}
		} 
    	catch (ClassNotFoundException e) { e.printStackTrace(); }
		catch (InstantiationException e) { e.printStackTrace(); }
		catch (IllegalAccessException e) { e.printStackTrace(); } 
    	catch (IOException e) { e.printStackTrace(); }
    	this.modules.add(new GuiClickGui.ModClickGui());
    	this.modules.sort(new Comparator<Module>() {
			public int compare(Module o1, Module o2) {
				int l1 = FONT_RENDERER.getStringWidth(o1.getName());
				int l2 = FONT_RENDERER.getStringWidth(o2.getName());
				return l2 - l1;
			}
		});
    }
    
    public void postInitialization() {
    	for(Module module : modules) {
    		if(module.isListeningTo(PostInitializationEvent.class))
    			module.onEvent(new PostInitializationEvent());
    	}
    }
    
    public static Module getModule(String name) {
    	for(Module module : Effect.mod.modules) {
    		if(module.getName().equalsIgnoreCase(name))
    			return module;
    	}
    	return null;
    }
    
    public static ArrayList<Module> getModules() { return Effect.mod.modules; }
    public static ArrayList<Command> getCommands() { return Effect.mod.commands; }

	public ResourceLocation getLogo() { return logo; }
    
}
