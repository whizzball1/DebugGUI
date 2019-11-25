package whizzball1.debuggui.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import whizzball1.debuggui.inventory.AbstractDebugScreen;
import whizzball1.debuggui.inventory.DebugScreen;
import whizzball1.debuggui.tile.IDebugTile;

import java.util.function.Supplier;

public class SlotMessage {
    int slot;
    BlockPos pos;

    public SlotMessage(int slot, BlockPos pos) {
        this.slot = slot;
        this.pos = pos;

    }

    public static void encode(SlotMessage msg, PacketBuffer buffer) {
        buffer.writeInt(msg.slot);
        buffer.writeLong(msg.pos.toLong());
    }

    public static SlotMessage decode(PacketBuffer buffer) {
        return new SlotMessage(buffer.readInt(), BlockPos.fromLong(buffer.readLong()));
    }

    public static void handle(SlotMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().currentScreen instanceof AbstractDebugScreen) {
                ((AbstractDebugScreen) Minecraft.getInstance().currentScreen).slotNumber = msg.slot;
            }
        });
    }
}
