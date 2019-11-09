package whizzball1.debuggui;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import whizzball1.debuggui.blocks.ModBlocks;
import whizzball1.debuggui.blocks.TestBlock;
import whizzball1.debuggui.core.ClientProxy;
import whizzball1.debuggui.core.IProxy;
import whizzball1.debuggui.core.ModSetup;
import whizzball1.debuggui.core.ServerProxy;
import whizzball1.debuggui.inventory.DebugContainer;
import whizzball1.debuggui.tile.TestTile;

@Mod("debuggui")
public class DebugGUI {

    public static final String MODID = "debuggui";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static ModSetup setup = new ModSetup();

    private static final Logger LOGGER = LogManager.getLogger();

    public DebugGUI() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent e) {
        proxy.init();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> e) {
            e.getRegistry().register(new TestBlock());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> e) {
            Item.Properties properties = new Item.Properties()
                    .group(setup.itemGroup);
            e.getRegistry().register(new BlockItem(ModBlocks.testBlock, properties).setRegistryName("testblock"));
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(TileEntityType.Builder.create(TestTile::new, ModBlocks.testBlock).build(null).setRegistryName("testblock"));
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> e) {
            e.getRegistry().register(IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                return new DebugContainer(windowId, DebugGUI.proxy.getClientWorld(), pos, inv, DebugGUI.proxy.getClientPlayer());
            }).setRegistryName("testblock"));
        }
    }

}
