package cf.effyiex.effect.gui;

import static cf.effyiex.effect.rendering.ColorCode.BLUE;
import static cf.effyiex.effect.rendering.ColorCode.RED;

import java.awt.Color;
import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import cf.effyiex.effect.Reference;
import cf.effyiex.effect.rendering.FontRenderer;
import cf.effyiex.effect.utils.AccessHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.Session;

public class GuiLogin extends GuiScreen implements Reference {
	
	private static final short inputWidth = 300;
	private static final short inputHeight = 30;
	private static final ArrayList<String[]> accounts = new ArrayList<String[]>();
	
	private String status = ("Login into your Minecraft Account!");
	
	private Textbox userInput, passInput;
	
	public static String login(String username, String password) {
		if((username.length() > 3 && username.length() <= 16) || username.contains("@")) {
			boolean success = true;
			if(password.isEmpty() && !username.isEmpty() && ((username.length() < 16 || username.length() == 16) && (username.length() > 4 || username.length() == 4))) {
				try {
					AccessHelper.hackSet(AccessHelper.session, new Session(username, "-", "-", "Legacy"));
				} catch (Exception e) { e.printStackTrace(); }
			}else if(!password.isEmpty() && !username.isEmpty()) {
				YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(Proxy.NO_PROXY, "").createUserAuthentication(Agent.MINECRAFT);
				auth.setUsername(username);
				auth.setPassword(password);
				try {
					auth.logIn();
					AccessHelper.hackSet(AccessHelper.session, new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang"));
					accounts.add(new String[] {username, password});
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}
			} else success = false;
			if(!success) {
				return RED.toString() + ("Login failed!");
			}else{
				CLIENT.displayGuiScreen(new MainMenu());
			}
		}else{
			return RED.toString() + ("The length of ur username has to be between 4 and 16!");
		}
		return BLUE.toString() + ("Unknown Error");
	}
	
	@Override public void initGui() {
		userInput = new Textbox((width - inputWidth) / 2, height / 2 - inputHeight, inputWidth, inputHeight, "NEEDED: Type in a Username");
		passInput = new Textbox((width - inputWidth) / 2, height / 2 + inputHeight, inputWidth, inputHeight, "NOT NEEDED: Type in a Password", '*');
	}
	
	@Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect(0, 0, width, height, new Color(55, 55, 66).getRGB());
		super.drawScreen(mouseX, mouseY, partialTicks);
		userInput.drawTextBox();
		passInput.drawTextBox();
		FontRenderer.drawCenteredString(status, width / 2, height / 2 - inputHeight * 2, Color.WHITE.getRGB());
		int index = 0;
		FONT_RENDERER.drawString("Last Accounts:", 2, 2, Color.WHITE.getRGB());
		for(String[] account : accounts) {
			drawString(FONT_RENDERER, account[0], 3, FONT_RENDERER.FONT_HEIGHT * (index + 1) + 2, Color.WHITE.getRGB());
			index++;
		}
	}
	
	@Override protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		userInput.textboxKeyTyped(typedChar, keyCode);
		passInput.textboxKeyTyped(typedChar, keyCode);
		if(keyCode == Keyboard.KEY_RETURN) status = login(userInput.getText(), passInput.getText());
		if(keyCode == Keyboard.KEY_ESCAPE) mc.displayGuiScreen(new MainMenu());
		if(typedChar == '\t') if(userInput.isFocused()) {
			userInput.setFocused(false);
			passInput.setFocused(true);
		}else{
			userInput.setFocused(true);
			passInput.setFocused(false);
		}
	}
	
	@Override protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		userInput.mouseClicked(mouseX, mouseY, mouseButton);
		passInput.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override public void updateScreen() {
		super.updateScreen();
		userInput.updateCursorCounter();
		passInput.updateCursorCounter();
	}

}
