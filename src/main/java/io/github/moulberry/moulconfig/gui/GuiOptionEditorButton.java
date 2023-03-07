/*
 * Copyright (C) 2022 NotEnoughUpdates contributors
 *
 * This file is part of NotEnoughUpdates.
 *
 * NotEnoughUpdates is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * NotEnoughUpdates is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NotEnoughUpdates. If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.moulberry.moulconfig.gui;

import io.github.moulberry.moulconfig.Config;
import io.github.moulberry.moulconfig.GuiTextures;
import io.github.moulberry.moulconfig.RenderUtils;
import io.github.moulberry.moulconfig.TextRenderUtils;
import io.github.moulberry.moulconfig.struct.ProcessedOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class GuiOptionEditorButton extends GuiOptionEditor {
	private final int runnableId;
	private String buttonText;
	private final Config config;
    private boolean isUsingRunnable;

	public GuiOptionEditorButton(
        ProcessedOption option,
        int runnableId,
        String buttonText,
        Config config
    ) {
        super(option);
        this.runnableId = runnableId;
        this.config = config;

        this.buttonText = buttonText;
        this.isUsingRunnable = option.getType() == Runnable.class;
        if (this.buttonText != null && this.buttonText.isEmpty()) this.buttonText = null;
    }

	@Override
	public void render(int x, int y, int width) {
		super.render(x, y, width);

		int height = getHeight();

		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().getTextureManager().bindTexture(GuiTextures.BUTTON);
		RenderUtils.drawTexturedRect(x + width / 6 - 24, y + height - 7 - 14, 48, 16);

		if (buttonText != null) {
			TextRenderUtils.drawStringCenteredScaledMaxWidth(buttonText, Minecraft.getMinecraft().fontRendererObj,
				x + width / 6, y + height - 7 - 6,
				false, 44, 0xFF303030
			);
		}
	}

	@Override
	public boolean mouseInput(int x, int y, int width, int mouseX, int mouseY) {
		if (Mouse.getEventButtonState()) {
			int height = getHeight();
			if (mouseX > x + width / 6 - 24 && mouseX < x + width / 6 + 24 &&
				mouseY > y + height - 7 - 14 && mouseY < y + height - 7 + 2) {
                if (isUsingRunnable) {
                    ((Runnable) option.get()).run();
                } else {
                    config.executeRunnable(runnableId);
                }
                return true;
            }
		}

		return false;
	}

	@Override
	public boolean keyboardInput() {
		return false;
	}
}
