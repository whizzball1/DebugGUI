package whizzball1.debuggui.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import whizzball1.debuggui.blocks.ModBlocks;
import whizzball1.debuggui.tile.IDebugTile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugContainer extends Container {

    public final Map<Integer, Slot> hiddenSlots = new HashMap<Integer, Slot>();
    private TileEntity tileEntity;
    private IDebugTile debugTile;
    private PlayerEntity playerEntity;
    private PlayerInventory playerInventory;

    public DebugContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(ModBlocks.debugContainer, windowId);
        tileEntity = world.getTileEntity(pos);
        debugTile = (IDebugTile) world.getTileEntity(pos);
        this.playerEntity = player;
        this.playerInventory = playerInventory;
        visibleSlotInit();
        otherInventoriesInit();

    }

    /*
    * Override this to change how your slot is added.
    * */
    public void visibleSlotInit() {
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            addSlot(new SlotItemHandler(h, debugTile.getVisibleSlot(), 126, 36));
        });
    }

    public void otherInventoriesInit() {
        for(int l = 0; l < 3; ++l) {
            for(int j1 = 0; j1 < 9; ++j1) {
                this.addSlot(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 - 18));
            }
        }

        for(int i1 = 0; i1 < 9; ++i1) {
            this.addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 161 - 18));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, ModBlocks.testBlock);
    }


}
