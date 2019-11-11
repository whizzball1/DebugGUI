package whizzball1.debuggui.tile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import whizzball1.debuggui.DebugGUI;
import whizzball1.debuggui.blocks.ModBlocks;
import whizzball1.debuggui.inventory.DebugContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TestTile extends TileEntity implements IDebugTile, INamedContainerProvider {

    private IItemHandler handler = createHandler();
    private LazyOptional<IItemHandler> optional = LazyOptional.of(() -> handler);
    public int visibleSlot = 0;

    public TestTile() {
        super(ModBlocks.testTile);
    }

    public int getVisibleSlot() {
        return visibleSlot;
    }

    public void setVisibleSlot(int slot) {
        if (slot < handler.getSlots() & slot >= 0) this.visibleSlot = slot;
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(4);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new DebugContainer(i, world, pos, playerInventory, playerEntity);
    }

    @Override
    public void read(CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");
        ((INBTSerializable<CompoundNBT>)handler).deserializeNBT(invTag);
        //check if this visibleSlot is allowed
        visibleSlot = tag.getInt("visible_slot");
        if (!(indexInHandler(visibleSlot))) visibleSlot = 0;
        DebugGUI.LOGGER.info(visibleSlot);
        super.read(tag);
    }

    public boolean indexInHandler(int index) {
        return index < handler.getSlots();
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT compound = ((INBTSerializable<CompoundNBT>)handler).serializeNBT();
        tag.put("inv", compound);
        tag.putInt("visible_slot", visibleSlot);
        return super.write(tag);
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return optional.cast();
        }
        return super.getCapability(cap, side);
    }
}
