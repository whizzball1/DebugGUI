package whizzball1.debuggui.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class DebugGUIPacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("debuggui", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int id = 1;

    public static void register() {
        INSTANCE.registerMessage(
                id++,
                VisibilityMessage.class,
                VisibilityMessage::encode,
                VisibilityMessage::decode,
                VisibilityMessage::handle
        );
    }
}
