package whizzball1.debuggui.inventory;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import whizzball1.debuggui.DebugGUI;
import whizzball1.debuggui.network.DebugGUIPacketHandler;
import whizzball1.debuggui.network.VisibilityMessage;

public class DebugScreen extends ContainerScreen<DebugContainer> {

    private ResourceLocation GUI = new ResourceLocation(DebugGUI.MODID, "textures/gui/debug_gui.png");

    public DebugScreen(DebugContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.xSize = 175;
        this.ySize = 221;

    }

    @Override
    public void init(Minecraft p_init_1_, int p_init_2_, int p_init_3_) {
        super.init(p_init_1_, p_init_2_, p_init_3_);
        addButton(new Button(guiLeft + 112, guiTop + 35, 12, 18, "<", (i) -> {
            DebugGUIPacketHandler.INSTANCE.sendToServer(new VisibilityMessage(-1, container.tileEntity.getPos()));
        }));
        addButton(new Button(guiLeft + 144, guiTop + 35, 12, 18, ">", (i) -> {
            DebugGUIPacketHandler.INSTANCE.sendToServer(new VisibilityMessage(1, container.tileEntity.getPos()));
        }));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    }
}
