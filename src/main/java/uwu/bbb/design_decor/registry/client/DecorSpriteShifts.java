package uwu.bbb.design_decor.registry.client;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.utility.Couple;
import uwu.bbb.design_decor.Utils;
import uwu.bbb.design_decor.registry.helper.decor.ColorHelper;

import java.util.*;

@SuppressWarnings({"unused", "SameParameterValue"})
public class DecorSpriteShifts {

    private static final Map<ColorHelper, Couple<CTSpriteShiftEntry>> COLORED_STORAGE_TOP = new HashMap<>();
    private static final Map<ColorHelper, Couple<CTSpriteShiftEntry>> COLORED_STORAGE_FRONT = new HashMap<>();
    private static final Map<ColorHelper, Couple<CTSpriteShiftEntry>> COLORED_STORAGE_SIDE = new HashMap<>();
    private static final Map<ColorHelper, Couple<CTSpriteShiftEntry>> COLORED_STORAGE_BOTTOM = new HashMap<>();

    public static final CTSpriteShiftEntry
            LARGE_GIRDER = rectangle("large_girder"),
            LARGE_GIRDER_TOP = omni("large_girder_top");


    static {
        populateMaps();
    }

    private static void populateMaps() {
        for (ColorHelper c : ColorHelper.DefaultColorProvider.COLORS) {
            COLORED_STORAGE_TOP.put(c, storage("top", c));
            COLORED_STORAGE_FRONT.put(c, storage("front", c));
            COLORED_STORAGE_SIDE.put(c, storage("side", c));
            COLORED_STORAGE_BOTTOM.put(c, storage("bottom", c));
        }
    }

    public static CTSpriteShiftEntry getColoredStorageTop(ColorHelper color, boolean small) {
        return COLORED_STORAGE_TOP.get(color).get(small);
    }
    public static CTSpriteShiftEntry getColoredStorageFront(ColorHelper color, boolean small) {
        return COLORED_STORAGE_FRONT.get(color).get(small);
    }
    public static CTSpriteShiftEntry getColoredStorageSide(ColorHelper color, boolean small) {
        return COLORED_STORAGE_SIDE.get(color).get(small);
    }
    public static CTSpriteShiftEntry getColoredStorageBottom(ColorHelper color, boolean small) {
        return COLORED_STORAGE_BOTTOM.get(color).get(small);
    }

    private static Couple<CTSpriteShiftEntry> storage(String name, ColorHelper color) {
        final String prefixed = "block/storage_container/" + color.digitId + "_storage_container_" + name;
        return Couple.createWithContext(
                medium -> CTSpriteShifter.getCT(AllCTTypes.RECTANGLE, Utils.asResource(prefixed + "_small"),
                        Utils.asResource(medium ? prefixed + "_medium" : prefixed + "_large")));
    }
    
    private static CTSpriteShiftEntry omni(String name) {
        return getCT(AllCTTypes.OMNIDIRECTIONAL, name);
    }

    private static CTSpriteShiftEntry horizontal(String name) {
        return getCT(AllCTTypes.HORIZONTAL, name);
    }

    private static CTSpriteShiftEntry vertical(String name) {
        return getCT(AllCTTypes.VERTICAL, name);
    }

    private static CTSpriteShiftEntry rectangle(String name) {
        return getCT(AllCTTypes.RECTANGLE, name);
    }

    private static CTSpriteShiftEntry cross(String name) {
        return getCT(AllCTTypes.CROSS, name);
    }

    private static CTSpriteShiftEntry horizontalKryppers(String name) {
        return getCT(AllCTTypes.HORIZONTAL_KRYPPERS, name);
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName, String connectedTextureName) {
        return CTSpriteShifter.getCT(type, Utils.asResource("block/" + blockTextureName), Utils.asResource("block/" + connectedTextureName + "_connected"));
    }

    private static CTSpriteShiftEntry getCT(CTType type, String blockTextureName) {
        return getCT(type, blockTextureName, blockTextureName);
    }

    public static void register(){}
}
