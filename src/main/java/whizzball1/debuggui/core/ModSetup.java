package whizzball1.debuggui.core;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import whizzball1.debuggui.blocks.ModBlocks;
import whizzball1.debuggui.network.DebugGUIPacketHandler;

public class ModSetup {

    public void init() {
        DebugGUIPacketHandler.register();
    }

    public ItemGroup itemGroup = new ItemGroup("DebugGUI") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.testBlock);
        }
    };
}
