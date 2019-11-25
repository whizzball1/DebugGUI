package whizzball1.debuggui.inventory;

import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringHolder {

    public List<String> stringArray;
    private String joinedString;
    public boolean isDirty;
    public String delimiter = "";
    //add delimiter here that would be useful

    public boolean isInSplit = false;
    public StringHolder splitParent;
    public List<StringHolder> splitString;

    public StringHolder(List<String> stringArray) {
        this.stringArray = stringArray;
        isDirty = true;
    }

    public StringHolder(String delimiter, List<String> stringArray) {
        this(stringArray);
        this.delimiter = delimiter;
    }

    public void setString(int index, String string) {
        if (validateIndex(index)) {
            stringArray.set(index, string);
            isDirty = true;
        }
        return;
    }

    /*
    * included is the inclusive first index
    * exclusive is the exclusive last index*/
    public void setStrings(int included, int excluded, String... inputArray) {
        if (inputArray.length < excluded - included || !(validateRange(included, excluded))) {
            return;
        }
        for (int i = 0; i < excluded - included; i++) {
            stringArray.set(i + included, inputArray[i]);
        }
        isDirty = true;
    }

    public String getString(int index) {
        return validateIndex(index) ? stringArray.get(index) : null;
    }

    public List<String> getStrings(int included, int excluded) {
        if (!validateRange(included, excluded)) return null;
        return stringArray.subList(included, excluded);
    }

    public List<String> getStringArray() {
        return stringArray;
    }

    public String getStringToRender(boolean force) {
        if (isDirty || force) {
            updateString();
            isDirty = false;
        }
        return joinedString;
    }

    private void updateString() {
        joinedString = String.join(delimiter, stringArray);
    }

    private boolean validateIndex(int index) {
        return index < stringArray.size() && index >= 0;
    }

    private boolean validateRange(int i1, int i2) {
        return validateIndex(i1) && i2 > i1 && i2 <= stringArray.size();
    }

    public boolean reSplit(FontRenderer font, int width) {
        if (isInSplit) if (splitParent.isDirty) {
            splitParent.splitStringToRender(font, true, width);
            return true;
        }
        return false;
    }

    //This should only get called if you actually need to split.
    public List<StringHolder> splitStringToRender(FontRenderer font, boolean force, int width) {
        if (force || splitString == null) {
            splitString = null;
            splitString = new ArrayList<>();
            String stringToSplit = getStringToRender(false);
            List<String> splitStrings = font.listFormattedStringToWidth(stringToSplit, width);
            for (int i = 0; i < splitStrings.size(); i += 9) {
                splitString.add(
                        new StringHolder(" ", splitStrings.subList(i, i + 9 <= splitStrings.size() ? i + 9 : splitStrings.size()))
                );
            }
            for (StringHolder str : splitString) {
                str.isInSplit = true;
                str.splitParent = this;
            }
        }
        return splitString;
    }
}
