package whizzball1.debuggui.tile;

import net.minecraft.tileentity.TileEntity;

public interface IDebugTile {
    int getVisibleSlot();

    void setVisibleSlot(int slot);
}
