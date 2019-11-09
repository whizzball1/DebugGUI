package whizzball1.debuggui.blocks;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import whizzball1.debuggui.inventory.DebugContainer;
import whizzball1.debuggui.tile.TestTile;

public class ModBlocks {

    @ObjectHolder("debuggui:testblock")
    public static TestBlock testBlock;

    @ObjectHolder("debuggui:testblock")
    public static TileEntityType<TestTile> testTile;

    @ObjectHolder("debuggui:testblock")
    public static ContainerType<DebugContainer> debugContainer;
}
