package whizzball1.debuggui.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import whizzball1.debuggui.blocks.ModBlocks;
import whizzball1.debuggui.inventory.DebugScreen;

public class ClientProxy implements IProxy {

    public void init() {
        ScreenManager.registerFactory(ModBlocks.debugContainer, DebugScreen::new);
    }

    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
