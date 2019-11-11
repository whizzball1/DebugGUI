package whizzball1.debuggui.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import whizzball1.debuggui.inventory.DebugContainer;
import whizzball1.debuggui.tile.IDebugTile;

import java.util.function.Supplier;

public class VisibilityMessage {
    int change;
    BlockPos pos;

    public VisibilityMessage(int change, BlockPos pos) {
        this.change = change;
        this.pos = pos;

    }

    public static void encode(VisibilityMessage msg, PacketBuffer buffer) {
        buffer.writeInt(msg.change);
        buffer.writeLong(msg.pos.toLong());
    }

    public static VisibilityMessage decode(PacketBuffer buffer) {
        return new VisibilityMessage(buffer.readInt(), BlockPos.fromLong(buffer.readLong()));
    }

    public static void handle(VisibilityMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            TileEntity te = ctx.get().getSender().getServerWorld().getTileEntity(msg.pos);
            IDebugTile ite = (IDebugTile) te;
            ite.setVisibleSlot(ite.getVisibleSlot() + msg.change);
            ((DebugContainer) ctx.get().getSender().openContainer).updateSlot();
        });
    }
}
