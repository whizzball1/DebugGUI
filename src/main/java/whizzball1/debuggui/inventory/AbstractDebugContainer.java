package whizzball1.debuggui.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import whizzball1.debuggui.DebugGUI;
import whizzball1.debuggui.blocks.ModBlocks;
import whizzball1.debuggui.tile.IDebugTile;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDebugContainer extends Container {
    public final Map<Integer, Slot> debugSlots = new HashMap<>();
    public TileEntity tileEntity;
    public IDebugTile debugTile;
    public IntReferenceHolder visibleSlot = IntReferenceHolder.single();
    public boolean hasItemHandler = false;


    public <T extends AbstractDebugContainer> AbstractDebugContainer(int windowId, World world, BlockPos pos, ContainerType<T> containerType) {
        super(containerType, windowId);
        tileEntity = world.getTileEntity(pos);
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> hasItemHandler = true);
        debugTile = (IDebugTile) world.getTileEntity(pos);
    }

    //Please call this when you've finished getting everything ready to run the two inits!
    public void init() {
        visibleSlotInit();
        otherInventoriesInit();
        trackInt(visibleSlot);
    }

    /*
     * Override this to change how your slot is added.
     * */
    public void visibleSlotInit() {
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            for (int handlerSlotNumber = 0; handlerSlotNumber < h.getSlots(); ++handlerSlotNumber) {
                SlotItemHandler slotHandler = new SlotItemHandler(h, handlerSlotNumber, 126, 36);
                slotHandler.slotNumber = 0;
                debugSlots.put(handlerSlotNumber, slotHandler);
                //Does the index of the slot need to be changed???
            }
            addSlot(debugSlots.get(debugTile.getVisibleSlot()));
            visibleSlot.set(debugTile.getVisibleSlot());
            detectAndSendChanges();
        });
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        return ItemStack.EMPTY;
    }

    public void otherInventoriesInit() {

    }

    public void updateSlot() {
        if(!hasItemHandler) return;
        visibleSlot.set(debugTile.getVisibleSlot());
        addDebugSlot(debugSlots.get(debugTile.getVisibleSlot()));
        DebugGUI.LOGGER.info("changedSLot");
    }

    protected void addDebugSlot(Slot slotIn) {
        inventorySlots.set(0, slotIn);
        detectAndSendChanges();
    }
}
