package whizzball1.debuggui.core;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import whizzball1.debuggui.blocks.ModBlocks;

public class ModSetup {

    public ItemGroup itemGroup = new ItemGroup("DebugGUI") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.testBlock);
        }
    };
}
