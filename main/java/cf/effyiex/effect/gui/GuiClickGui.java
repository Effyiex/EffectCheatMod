package cf.effyiex.effect.gui;

import static cf.effyiex.effect.utils.AccessHelper.hackGet;
import static cf.effyiex.effect.utils.AccessHelper.theShaderGroup;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import cf.effyiex.effect.Effect;
import cf.effyiex.effect.Reference;
import cf.effyiex.effect.module.Module;
import cf.effyiex.effect.module.ModuleType;
import cf.effyiex.effect.module.Setting;
import cf.effyiex.effect.module.Setting.SettingType;
import cf.effyiex.effect.rendering.ColorCode;
import cf.effyiex.effect.rendering.FontRenderer;
import cf.effyiex.effect.utils.Map;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public class GuiClickGui extends GuiScreen implements Reference {
	
	private static final int BACKGROUND = new Color(0, 0, 0, 64).getRGB();
	private static final int GRADIENT = new Color(225, 55, 255, 125).getRGB();
	
	public static class ModClickGui extends Module {

		public ModClickGui() {
			super("ClickGui", ModuleType.RENDER);
			this.hideOnArray = true;
		}

		@Override
		public void toggle() { CLIENT.displayGuiScreen(new GuiClickGui()); }
		
		@Override
		public void onEvent(Object event) {}
		
	}
	
	@SuppressWarnings("serial")
	private static class TabContainer extends ArrayList<ModuleContainer> {
		
		private static final int WIDTH = 96, HEIGHT = 24;
		private static final int BACKGROUND = new Color(11, 11, 11).getRGB();
		private static final int FOREGROUND = ColorCode.WHITE.getHexCode();
		
		boolean extended = false;
		boolean holding = false;
		boolean hovered = false;
		
		String title;
		int x, y;
		
		TabContainer(ModuleType type) {
			this.title = (type.name().charAt(0) + type.name().substring(1).toLowerCase());
			this.x = HEIGHT;
			this.y = (HEIGHT * (tabs.size() + 1) + tabs.size() / 2);
			short count = 0;
			for(Module module : Effect.getModules()) {
				if(module.getType().equals(type)) {
					this.add(new ModuleContainer(module, count));
					count++;
				}
			}
		}
		
		static Map<String, int[]> dragging;
		
		void render(int mouseX, int mouseY) {
			drawRect(x, y, x + WIDTH, y + HEIGHT, BACKGROUND);
			FontRenderer.drawXYCenteredString(title, x + WIDTH / 2, y + HEIGHT / 2, FOREGROUND);
			FontRenderer.drawXYCenteredString(extended ? "v" : "o", x + WIDTH / 8 * 7, y + HEIGHT / 2, FOREGROUND);
			if(extended) for(ModuleContainer next : this)
				next.render(x + WIDTH / 2, y + HEIGHT, mouseX, mouseY);
			hovered = (mouseX > x && mouseY > y && mouseX < x + WIDTH && mouseY < y + HEIGHT);
			if(hovered && !holding && Mouse.isButtonDown(1)) {
				extended = !extended;
			}
			if(Mouse.isButtonDown(0)) {
				if(hovered && dragging == null)
					dragging = new Map<String, int[]>
					(this.title, new int[] { mouseX - x, mouseY - y });
			} else dragging = null;
			if(dragging != null && dragging.x.equalsIgnoreCase(title)) {
				x = mouseX - dragging.y[0];
				y = mouseY - dragging.y[1];
			}
			holding = Mouse.isButtonDown(1);
			//RenderHelper.drawVerticalGradient(x, y + HEIGHT, x + WIDTH, y + HEIGHT * 2, ColorCode.LIGHT_PURPLE.getHexCode(), 0);
		}
		
	}
	
	@SuppressWarnings("serial")
	private static class ModuleContainer extends ArrayList<SettingContainer> {
		
		private static final int WIDTH = 90, HEIGHT = 24;
		private static final int BACKGROUND = new Color(11, 11, 11, 155).getRGB();
		private static final int FOREGROUND = ColorCode.WHITE.getHexCode();
		private static final int ON_COLOR = new Color(55, 255, 55).getRGB();
		private static final int OFF_COLOR = new Color(255, 55, 55).getRGB();
		
		boolean hovered = false;
		boolean holding = false;
		boolean extended = false;
		
		Module module;
		short index;
		
		ModuleContainer(Module module, short index) {
			this.module = module;
			this.index = index;
			index = 0;
			for(Setting setting : module.getSettings()) {
				switch(setting.y) {
				
				case FLOAT: add(new FloatContainer(setting)); break;
				case BOOLEAN: add(new BooleanContainer(setting)); break;
				case INTEGER: add(new IntegerContainer(setting)); break;
				case MODE: add(new ModeContainer(setting)); break;
				case STRING: add(new StringContainer(setting)); break;
				case COLOR: add(new ColorContainer(setting)); break;
				default: break;
				
				}
				index++;
			}
			SettingContainer.resetByModule();
		}
		
		void render(int x, int y, int mouseX, int mouseY) {
			y += (HEIGHT * index);
			drawRect(x - WIDTH / 2, y, x + WIDTH / 2, y + HEIGHT, BACKGROUND);
			if(module.renderOnArray())
				drawRect(x - WIDTH / 2 + 1, y + 1, x - WIDTH / 2 + 2, y + HEIGHT - 1, module.isActive() ? ON_COLOR : OFF_COLOR);
			FontRenderer.drawXYCenteredString(module.getName(), x, y + HEIGHT / 2, FOREGROUND);
			if(!module.getSettings().isEmpty())
				FontRenderer.drawXYCenteredString(extended ? ">" : "<", x + WIDTH / 8 * 3, y + HEIGHT / 2, FOREGROUND);
			hovered = (mouseX > x - WIDTH / 2 && mouseY > y && mouseX < x + WIDTH / 2 && mouseY < y + HEIGHT);
			if(Mouse.isButtonDown(0) && !holding && hovered) module.toggle();
			if(Mouse.isButtonDown(1) && !holding && hovered) extended = !extended;
			holding = (Mouse.isButtonDown(0) || Mouse.isButtonDown(1));
			if(extended) for(SettingContainer container : this) {
				container.doWholeRender(x + WIDTH / 2, y, mouseX, mouseY);
			}
		}
		
	}
	
	private abstract static class SettingContainer {
		
		protected static final int WIDTH = 80, HEIGHT = 16;
		protected static final int BACKGROUND = new Color(11, 11, 11, 155).getRGB();
		protected static final int FOREGROUND = ColorCode.WHITE.getHexCode();
		
		protected static int initCounter = 0;
		
		int initY = 0;
		Setting setting;
		
		boolean hovered = false;
		boolean holding = false;
		
		SettingContainer(SettingType type, Setting setting, float init) {
			this.setting = setting;
			initY = initCounter;
			initCounter += HEIGHT * init;
		}
		
		void doWholeRender(int x, int y, int mouseX, int mouseY) {
			y += initY;
			hovered = (mouseX > x && mouseY > y && mouseX < x + WIDTH && mouseY < y + HEIGHT);
			render(x, y, mouseX, mouseY);
			holding = Mouse.isButtonDown(0);
		}
		
		abstract void render(int x, int y, int mouseX, int mouseY);
		
		public static void resetByModule() { initCounter = 0; }
		
	}
	
	private static class FloatContainer extends SettingContainer {

		private static final int BAR = new Color(255, 225, 255).getRGB();

		FloatContainer(Setting setting) { super(SettingType.FLOAT, setting, 2.25F); }
		
		@Override
		void render(int x, int y, int mouseX, int mouseY) {
			drawRect(x, y, x + WIDTH, y + HEIGHT / 4 * 9, BACKGROUND);
			FontRenderer.drawXYCenteredString(setting.getID() + ':', x + WIDTH / 2, y + HEIGHT / 2, FOREGROUND);
			FONT_RENDERER.drawString(String.valueOf(setting.getBoundings().x), x + 4, y + HEIGHT / 2 * 3, FOREGROUND);
			String max = String.valueOf(setting.getBoundings().y);
			FONT_RENDERER.drawString(max, x + WIDTH - FONT_RENDERER.getStringWidth(max) - 4, y + HEIGHT / 2 * 3, FOREGROUND);
			FontRenderer.drawCenteredString(setting.toString(), x + WIDTH / 2, y + HEIGHT / 2 * 3, FOREGROUND);
			drawRect(x + 3, y + HEIGHT, x + WIDTH - 3, y + HEIGHT + 1, BAR);
			int indiX = Math.round((WIDTH - 8) / (setting.getBoundings().y - setting.getBoundings().x) * setting.toFloat());
			drawRect(x + 3 + indiX, y + HEIGHT - 3, x + 4 + indiX, y + HEIGHT + 4, BAR);
			if(holding && mouseX > x + 3 && mouseX < x + WIDTH - 3 && mouseY > y + HEIGHT / 2 && mouseY < y + HEIGHT / 2 * 3) {
				int mx = (mouseX - x - 3);
				int width = (WIDTH - 6);
				setting.setValue(-setting.getBoundings().x + setting.getBoundings().y / width * mx);
			}
		}
		
	}
	
	private static class BooleanContainer extends SettingContainer {
		
		BooleanContainer(Setting setting) { super(SettingType.BOOLEAN, setting, 1); }
		
		@Override
		void render(int x, int y, int mouseX, int mouseY) {
			drawRect(x, y, x + WIDTH, y + HEIGHT, BACKGROUND);
			FontRenderer.drawXYCenteredString(setting.getID() + ": " + (setting.toBoolean() ? "§aON" : "§cOFF"), x + WIDTH / 2, y + HEIGHT / 2, FOREGROUND);
			if(!holding && hovered && Mouse.isButtonDown(0)) { setting.setValue(!setting.toBoolean()); }
		}
		
	}
	
	private static class IntegerContainer extends SettingContainer {
		
		private static final int BAR = new Color(255, 225, 255).getRGB();

		IntegerContainer(Setting setting) { super(SettingType.INTEGER, setting, 2.25F); }
		
		@Override
		void render(int x, int y, int mouseX, int mouseY) {
			drawRect(x, y, x + WIDTH, y + HEIGHT / 4 * 9, BACKGROUND);
			FontRenderer.drawXYCenteredString(setting.getID() + ':', x + WIDTH / 2, y + HEIGHT / 2, FOREGROUND);
			FONT_RENDERER.drawString(String.valueOf(Math.round(setting.getBoundings().x)), x + 4, y + HEIGHT / 2 * 3, FOREGROUND);
			String max = String.valueOf(Math.round(setting.getBoundings().y));
			FONT_RENDERER.drawString(max, x + WIDTH - FONT_RENDERER.getStringWidth(max) - 4, y + HEIGHT / 2 * 3, FOREGROUND);
			FontRenderer.drawCenteredString(setting.toString(), x + WIDTH / 2, y + HEIGHT / 2 * 3, FOREGROUND);
			drawRect(x + 3, y + HEIGHT, x + WIDTH - 3, y + HEIGHT + 1, BAR);
			int indiX = Math.round((WIDTH - 8) / (setting.getBoundings().y - setting.getBoundings().x) * setting.toFloat());
			drawRect(x + 3 + indiX, y + HEIGHT - 3, x + 4 + indiX, y + HEIGHT + 4, BAR);
			if(holding && mouseX > x + 3 && mouseX < x + WIDTH - 3 && mouseY > y + HEIGHT / 2 && mouseY < y + HEIGHT / 2 * 3) {
				int mx = (mouseX - x - 3);
				int width = (WIDTH - 6);
				setting.setValue(Math.round(-setting.getBoundings().x + setting.getBoundings().y / width * mx));
			}
		}
		
	}
	
	private static class ModeContainer extends SettingContainer {

		private static final int SELECTED = new Color(255, 55, 255).getRGB();
		
		ModeContainer(Setting setting) { super(SettingType.MODE, setting, setting.getModes().length + 1); }

		@Override
		void render(int x, int y, int mouseX, int mouseY) {
			drawRect(x, y, x + WIDTH, y + HEIGHT * (setting.getModes().length + 1), BACKGROUND);
			FontRenderer.drawXYCenteredString(setting.getID() + ':', x + WIDTH / 2, y + HEIGHT / 2, FOREGROUND);
			int index = 1;
			for(String option : setting.getModes()) {
				String display = (String.valueOf(index) + ". " + option);
				FontRenderer.drawXYCenteredString(display, x + WIDTH / 2, y + HEIGHT * index + HEIGHT / 2, setting.toString().equals(option) ? SELECTED : FOREGROUND);
				if(Mouse.isButtonDown(0) && !holding && mouseX > x && mouseX < x + WIDTH && mouseY > y + HEIGHT * index && mouseY < y + HEIGHT * (index + 1))
					setting.x = (index - 1);
				index++;
			}
		}
		
	}
	
	private static class StringContainer extends SettingContainer {

		StringContainer(Setting setting) { super(SettingType.STRING, setting, 1); }

		boolean extended = false;
		
		@Override
		void render(int x, int y, int mouseX, int mouseY) {
			drawRect(x, y, x + WIDTH, y + HEIGHT, BACKGROUND);
			FontRenderer.drawXYCenteredString(setting.getID() + (extended ? " >" : " <"), x + WIDTH / 2, y + HEIGHT / 2, FOREGROUND);
			if(extended) FONT_RENDERER.drawString('\"' + setting.toString() + '\"', x + WIDTH + 3, y + HEIGHT / 2 - FONT_RENDERER.FONT_HEIGHT / 2, FOREGROUND);
			if(!holding && hovered && Mouse.isButtonDown(0)) { extended = !extended; }
		}
		
	}
	
	private static class ColorContainer extends SettingContainer {

		ColorContainer(Setting setting) { super(SettingType.COLOR, setting, 1); }

		@Override
		void render(int x, int y, int mouseX, int mouseY) {
			drawRect(x, y, x + WIDTH, y + HEIGHT, BACKGROUND);
			FONT_RENDERER.drawString(setting.getID() + ": ", x + 4, y + HEIGHT / 2 - FONT_RENDERER.FONT_HEIGHT / 2, FOREGROUND);
			drawRect(x + 5 + FONT_RENDERER.getStringWidth(setting.getID() + ": "), y + 1, x + WIDTH - 1, y + HEIGHT - 1, setting.toColor().getRGB());
			if(!holding && hovered && Mouse.isButtonDown(0)) CLIENT.displayGuiScreen(new GuiColorchoose(CLIENT.currentScreen) {
				
				@Override
				public void replace() { ColorContainer.this.setting.setAWTColor(current); }
				
			});
		}
		
	}
	
	private boolean shaderState = false;
	
	private void checkForShader() {
		if(!shaderState && CLIENT.entityRenderer != null) {
			if (OpenGlHelper.shadersSupported) {
				if (hackGet(theShaderGroup) != null)
					((ShaderGroup)hackGet(theShaderGroup)).deleteShaderGroup();
				CLIENT.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
			}
			shaderState = true;
		}
	}
	
	private static ArrayList<TabContainer> tabs = new ArrayList<GuiClickGui.TabContainer>();
	
	public GuiClickGui() {
		if(tabs.isEmpty()) {
			for(ModuleType type : ModuleType.values())
				tabs.add(new TabContainer(type));
		}
	}
	
	@Override
	public void initGui() {
		this.buttonList.clear();
		CLIENT.setIngameNotInFocus();
	}
	
	@Override
	public void onGuiClosed() {
		CLIENT.entityRenderer.stopUseShader();
		super.onGuiClosed();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect(0, 0, width, height, BACKGROUND);
		drawGradientRect(0, 0, width, height, 0, GRADIENT);
		this.checkForShader();
		for(TabContainer tab : tabs)
			if(tab != null) tab.render(mouseX, mouseY);
		drawRect(mouseX - 2, mouseY - 2, mouseX + 2, mouseY + 2, Color.BLACK.getRGB());
		drawRect(mouseX - 1, mouseY - 1, mouseX + 1, mouseY + 1, Color.WHITE.getRGB());
	}
	
}
