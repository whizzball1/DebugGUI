package whizzball1.debuggui.inventory;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class DebugScreen extends AbstractDebugContainerScreen<DebugContainer> {


    //Button impl specific
    public int statistic = 0;

    public DebugScreen(DebugContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);

    }

    @Override
    public void init(Minecraft p_init_1_, int p_init_2_, int p_init_3_) {
        super.init(p_init_1_, p_init_2_, p_init_3_);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public void addTextInit() {
        addStringWithId("longString", new StringHolder(Lists.newArrayList("This is part of the string ", "and this is part of the string ", "and if you want to see more you'd better change.")));
        addStringWithId("shortString", new StringHolder(Lists.newArrayList("This is a shorter string")));
        addStringWithId("statisticString", new StringHolder(Lists.newArrayList("This is a string with a statistic: ", "0")));
        addStringWithId("excessiveString", new StringHolder(Lists.newArrayList("This is an excessively long string which has been made to test my theoretically great pagification algorithm that should hopefully handle edits. An example of such edits is as follows: ", "Dummy string.")));
    }

    @Override
    public void addButtonInit() {
        buttonList.add(new Button(guiLeft + 134 - 48/2, guiTop + 70, 48, 18, "Button 1", (i) -> {
            statistic += -1;
            getStringById("statisticString").setString(1, Integer.toString(statistic));
        }));
        buttonList.add(new Button(guiLeft + 134 - 48/2, guiTop + 70, 48, 18, "Button 2", (i) -> {
            statistic += 1;
            getStringById("statisticString").setString(1, Integer.toString(statistic));
        }));
        buttonList.add(new Button(guiLeft + 134 - 48/2, guiTop + 70, 48, 18, "Button 3", (i) -> {
            getStringById("excessiveString").setString(1, getStringById("excessiveString").getString(1) + " Dummy string.");
        }));
        buttonMap.put(buttonList.get(0), "Lower Statistic by 1");
        buttonMap.put(buttonList.get(1), "Increase Statistic by 1");
        buttonMap.put(buttonList.get(2), "Increase excessive string");
    }
}
