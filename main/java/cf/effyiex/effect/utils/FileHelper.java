package cf.effyiex.effect.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.Setting;

public class FileHelper {
	
	private static final String WORKSPACE = (System.getProperty("user.dir") + "\\effect\\");
	
	public static void initializeWorkspace() {
		File file = new File(WORKSPACE);
		if(!file.exists() || !file.isDirectory()) file.mkdir();
		if(!(find("auto.settings.effect").exists() && find("auto.modules.effect").exists()))
			saveConfig("auto");
	}
	
	public static void saveConfig(String config) {
		try {
			File auto = find(config + ".modules.effect");
			if(!auto.exists() || !auto.isFile()) auto.createNewFile();
			auto = find(config + ".settings.effect");
			if(!auto.exists() || !auto.isFile()) auto.createNewFile();
			String activeModules = ("");
			String moduleSettings = ("");
			for(Module module : Effect.getModules()) {
				activeModules += (module.getName() + ':' + module.isActive() + ':' + module.getBind() + '\n');
				moduleSettings += ("//" + module.getName() + '\n');
				for(Setting setting : module.getSettings()) {
					moduleSettings += (setting.toString() + '\n');
				}
			}
			write(config + ".modules.effect", new Cryption(activeModules).toString());
			write(config + ".settings.effect", new Cryption(moduleSettings).toString());
			
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public static void loadConfig(String config) {
    	for(String line : FileHelper.read(config + ".modules.effect")) {
    		String[] args = new Cryption(line).toString().split(String.valueOf(':'));
    		Module module = Effect.getModule(args[0]);
    		if(module != null) {
	    		if(module.isActive() != Boolean.parseBoolean(args[1])) module.toggle();
	    		module.setBind(Integer.parseInt(args[2]));
    		}
    	}
    	Module settingsModule = null;
    	int settingsIndex = 0;
    	for(String line : FileHelper.read(config + ".settings.effect")) {
    		line = new Cryption(line).toString();
    		if(line.startsWith("//")) {
    			settingsModule = Effect.getModule(line.replace("//", ""));
    			settingsIndex = 0;
    		}else if(settingsModule != null) {
    			Setting setting = settingsModule.getSettings().get(settingsIndex);
    			setting.setValue(line);
    			settingsIndex++;
    		}
    	}
	}
	
	public static void write(File file, String text) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(text);
			writer.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public static ArrayList<String> read(File file) {
		ArrayList<String> output = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			for(String line = reader.readLine(); line != null; line = reader.readLine()) {
				output.add(line);
			}
			reader.close();
		} catch (IOException e) { e.printStackTrace(); }
		return output;
	}
	
	public static ArrayList<String> read(String workFilePath) { return read(find(workFilePath)); }
	public static void write(String workFilePath, String text) { write(find(workFilePath), text); }
	
	public static File find(String file) { return new File(WORKSPACE + file); }

}
