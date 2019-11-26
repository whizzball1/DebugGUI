package whizzball1.debuggui.inventory;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import whizzball1.debuggui.DebugGUI;
import whizzball1.debuggui.network.DebugGUIPacketHandler;
import whizzball1.debuggui.network.VisibilityMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDebugContainerScreen<T extends AbstractDebugContainer> extends ContainerScreen<T> {
    private ResourceLocation GUI = new ResourceLocation(DebugGUI.MODID, "textures/gui/debug_gui.png");

    private final int textPageWidth = 75;

    public List<Widget> buttonList = new ArrayList<>();
    public Map<Widget, String> buttonMap = new HashMap<>();
    public int buttonNumber = 0;

    public List<StringHolder> stringList = new ArrayList<>();
    public List<StringHolder> splitStringList = new ArrayList<>();
    public Map<String, Integer> idToIndex = new HashMap<>();
    public List<StringHolder> stringsOnPage;
    public List<Integer> pagePartitions = new ArrayList<>();
    public int currentStringPage = 0;

    public AbstractDebugContainerScreen(T container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.xSize = 175;
        this.ySize = 221;

    }

    @Override
    public void init(Minecraft p_init_1_, int p_init_2_, int p_init_3_) {
        super.init(p_init_1_, p_init_2_, p_init_3_);
        addButton(new Button(guiLeft + 112, guiTop + 35, 12, 18, "<", (i) -> {
            DebugGUIPacketHandler.INSTANCE.sendToServer(new VisibilityMessage(-1, container.tileEntity.getPos()));
        }));
        addButton(new Button(guiLeft + 144, guiTop + 35, 12, 18, ">", (i) -> {
            DebugGUIPacketHandler.INSTANCE.sendToServer(new VisibilityMessage(1, container.tileEntity.getPos()));
        }));
        addButton(new Button(guiLeft + 112, guiTop + 70, 12, 18, "<", (i) -> {
            if (!(buttonNumber == 0)) switchButton(buttonNumber - 1, false);
        }));
        addButton(new Button(guiLeft + 144, guiTop + 70, 12, 18, ">", (i) -> {
            if (!(buttonNumber + 1 == buttonList.size())) switchButton(buttonNumber + 1, false);
        }));
        addButton(new Button(guiLeft + 8, guiTop + 104, 12, 18, "<", (i) -> {
            if (!(currentStringPage == 0)) switchTextPage(currentStringPage - 1);
        }));
        addButton(new Button(guiLeft + 75, guiTop + 104, 12, 18, ">", (i) -> {
            if (!(currentStringPage + 1 >= pagePartitions.size())) switchTextPage(currentStringPage + 1);
        }));
        addButtonInit();
        switchButton(0, true);
        addTextInit();
        pagify(false);
        switchTextPage(0);
    }

    public void switchButton(int buttonListIndex, boolean force) {
        if (buttonList.size() == 0) return;
        Widget widget = buttonList.get(buttonListIndex);
        if (widget == null) return;
        this.buttonNumber = buttonListIndex;
        if (force) {
            addButton(widget);
        } else {
            buttons.set(buttons.size()-1, widget);
            children.set(buttons.size()-1, widget);
        }
        buttons.get(2).x = guiLeft + 122 - widget.getWidth() / 2;
        buttons.get(3).x = guiLeft + 134 + widget.getWidth() / 2;
    }

    //Override this to change the initial text added!
    public void addTextInit() {
    }

    public void addTextDynamic(StringHolder str, String id) {
        addStringWithId(id, str);
        pagify(true);
    }

    //Override this to change the initial buttons added!
    public void addButtonInit() {

    }

    public void addStringWithId(String id, StringHolder str) {
        idToIndex.put(id, stringList.size());
        stringList.add(str);
    }

    public StringHolder getStringById(String id) {
        return stringList.get(idToIndex.get(id));
    }

    public void removeStringById(String id) {
        stringList.remove(idToIndex.get(id));
        idToIndex.remove(id);
        pagify(true);
    }

    //Make this more efficient by not getting the word wrapped height if the string is split.
    protected void pagify(boolean replace) {
        if (stringList.isEmpty()) return;
        if (replace) {
            pagePartitions.clear();
            splitStringList.clear();
        }
        pagePartitions.add(0);
        int pageHeight = 0;
        for (StringHolder str : stringList) {
            int toAdd = font.getWordWrappedHeight(str.getStringToRender(false), textPageWidth);
            if (toAdd > 85) {
                List<StringHolder> tempSplitStrings = str.splitStringToRender(font, false, textPageWidth);
                for (StringHolder splitStr : tempSplitStrings) {
                    pagePartitions.add(splitStringList.size());
                    splitStringList.add(splitStr);
                }
                pageHeight = toAdd % 81;
            } else if (pageHeight + toAdd > 85 ) {
                pagePartitions.add(splitStringList.size());
                pageHeight = toAdd;
                splitStringList.add(str);
            } else {
                pageHeight += toAdd;
                splitStringList.add(str);
            }

        }
    }

    //Figure out what happens if the height > 85?
    protected void drawDebugText() {
        if (stringsOnPage.isEmpty()) return;
        boolean splitNeeded = false;
        List<String> tempList = new ArrayList<>();
        for (StringHolder str : stringsOnPage) {
            if (str.reSplit(font, textPageWidth)) {
                splitNeeded = true;
            }
            tempList.add(str.getStringToRender(false));
        }
        if (splitNeeded) {
            pagify(true);
            switchTextPage(currentStringPage >= pagePartitions.size() ? 0 : currentStringPage);
            drawDebugText();
            return;
        }
        font.drawSplitString(String.join("\n", tempList), 10, 20, textPageWidth, 16777215);
    }

    public void switchTextPage(int page) {
        if (stringList.isEmpty()) return;
        currentStringPage = page;
        stringsOnPage = splitStringList.subList(pagePartitions.get(page), pagePartitions.size() > page + 1 ? pagePartitions.get(page + 1) : splitStringList.size());
    }


    //Please use super() when you override this!
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawCenteredString(font, "Slot " + Integer.toString(container.visibleSlot.get()), guiLeft, guiTop + 16, 16777215);
        drawButtonText();
        drawDebugText();
    }

    protected void drawButtonText() {
        if (buttonList.size() == 0) return;
        String toDraw = buttonMap.get(buttonList.get(buttonNumber));
        font.drawSplitString(toDraw, 98, 90, 72, 16777215);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
    }
}
