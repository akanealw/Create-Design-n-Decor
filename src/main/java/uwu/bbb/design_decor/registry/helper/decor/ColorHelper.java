package uwu.bbb.design_decor.registry.helper.decor;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColorHelper {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ColorHelper && ((ColorHelper) obj).digitId == this.digitId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(digitId);
    }

    @SuppressWarnings("unused")
    public static class DefaultColorProvider extends ColorHelper {
        public static ColorHelper WHITE = create(12, "White", Items.WHITE_DYE, MaterialColor.SNOW, DyeColor.WHITE);
        public static ColorHelper ORANGE = create(2, "Orange", Items.ORANGE_DYE, MaterialColor.COLOR_ORANGE, DyeColor.ORANGE);
        public static ColorHelper MAGENTA = create(10, "Magenta", Items.MAGENTA_DYE, MaterialColor.COLOR_MAGENTA, DyeColor.MAGENTA);
        public static ColorHelper LIGHT_BLUE = create(7, "Light Blue", Items.LIGHT_BLUE_DYE, MaterialColor.COLOR_LIGHT_BLUE, DyeColor.LIGHT_BLUE);
        public static ColorHelper YELLOW = create(3, "Yellow", Items.YELLOW_DYE, MaterialColor.COLOR_YELLOW, DyeColor.YELLOW);
        public static ColorHelper LIME = create(4, "Lime", Items.LIME_DYE, MaterialColor.COLOR_LIGHT_GREEN, DyeColor.LIME);
        public static ColorHelper PINK = create(11, "Pink", Items.PINK_DYE, MaterialColor.COLOR_PINK, DyeColor.PINK);
        public static ColorHelper GRAY = create(14, "Gray", Items.GRAY_DYE, MaterialColor.COLOR_GRAY, DyeColor.GRAY);
        public static ColorHelper LIGHT_GRAY = create(13, "Light Gray", Items.LIGHT_GRAY_DYE, MaterialColor.COLOR_LIGHT_GRAY, DyeColor.LIGHT_GRAY);
        public static ColorHelper CYAN = create(6, "Cyan", Items.CYAN_DYE, MaterialColor.COLOR_CYAN, DyeColor.CYAN);
        public static ColorHelper PURPLE = create(9, "Purple", Items.PURPLE_DYE, MaterialColor.COLOR_PURPLE, DyeColor.PURPLE);
        public static ColorHelper BLUE = create(8, "Blue", Items.BLUE_DYE, MaterialColor.COLOR_BLUE, DyeColor.BLUE);
        public static ColorHelper BROWN = create(0, "Brown", Items.BROWN_DYE, MaterialColor.COLOR_BROWN, DyeColor.BROWN);
        public static ColorHelper GREEN = create(5, "Green", Items.GREEN_DYE, MaterialColor.COLOR_GREEN, DyeColor.GREEN);
        public static ColorHelper RED = create(1, "Red", Items.RED_DYE, MaterialColor.COLOR_RED, DyeColor.RED);
        public static ColorHelper BLACK = create(15, "Black", Items.BLACK_DYE, MaterialColor.COLOR_BLACK, DyeColor.BLACK);

        public static final ColorHelper[] COLORS = new ColorHelper[]{WHITE, ORANGE, MAGENTA, LIGHT_BLUE, YELLOW, LIME, PINK, GRAY, LIGHT_GRAY, CYAN, PURPLE, BLUE, BROWN, GREEN, RED, BLACK};

        public static void register() {}
    }

    @SuppressWarnings("unused")
    public enum DefaultColorEnumProvider implements StringRepresentable {
        WHITE(12, "White", Items.WHITE_DYE, MaterialColor.SNOW, DyeColor.WHITE),
        ORANGE(2, "Orange", Items.ORANGE_DYE, MaterialColor.COLOR_ORANGE, DyeColor.ORANGE),
        MAGENTA(10, "Magenta", Items.MAGENTA_DYE, MaterialColor.COLOR_MAGENTA, DyeColor.MAGENTA),
        LIGHT_BLUE(7, "Light Blue", Items.LIGHT_BLUE_DYE, MaterialColor.COLOR_LIGHT_BLUE, DyeColor.LIGHT_BLUE),
        YELLOW(3, "Yellow", Items.YELLOW_DYE, MaterialColor.COLOR_YELLOW, DyeColor.YELLOW),
        LIME(4, "Lime", Items.LIME_DYE, MaterialColor.COLOR_LIGHT_GREEN, DyeColor.LIME),
        PINK(11, "Pink", Items.PINK_DYE, MaterialColor.COLOR_PINK, DyeColor.PINK),
        GRAY(14, "Gray", Items.GRAY_DYE, MaterialColor.COLOR_GRAY, DyeColor.GRAY),
        LIGHT_GRAY(13, "Light Gray", Items.LIGHT_GRAY_DYE, MaterialColor.COLOR_LIGHT_GRAY, DyeColor.LIGHT_GRAY),
        CYAN(6, "Cyan", Items.CYAN_DYE, MaterialColor.COLOR_CYAN, DyeColor.CYAN),
        PURPLE(9, "Purple", Items.PURPLE_DYE, MaterialColor.COLOR_PURPLE, DyeColor.PURPLE),
        BLUE(8, "Blue", Items.BLUE_DYE, MaterialColor.COLOR_BLUE, DyeColor.BLUE),
        BROWN(0, "Brown", Items.BROWN_DYE, MaterialColor.COLOR_BROWN, DyeColor.BROWN),
        GREEN(5, "Green", Items.GREEN_DYE, MaterialColor.COLOR_GREEN, DyeColor.GREEN),
        RED(1, "Red", Items.RED_DYE, MaterialColor.COLOR_RED, DyeColor.RED),
        BLACK(15, "Black", Items.BLACK_DYE, MaterialColor.COLOR_BLACK, DyeColor.BLACK);

        private final int digitId;
        private final Item colorItem;
        private final String lang;
        private final DyeColor colorDye;
        private final MaterialColor color;

        DefaultColorEnumProvider(int digitId, String lang, Item dye, MaterialColor mapColor, DyeColor colorDye) {
            this.digitId = digitId;
            this.colorItem = dye;
            this.lang = lang;
            this.colorDye = colorDye;
            this.color = mapColor;
        }

        public ColorHelper get() {
            return create(digitId, lang, colorItem, color, colorDye);
        }

        @Override
        public @NotNull String getSerializedName() {
            return get().colorId;
        }
    }

    public int digitId;
    public Item colorItem;
    public String colorId;
    public String colorLang;
    public DyeColor colorDye;
    public MaterialColor color;

    public final List<ColorHelper> COLORS = new ArrayList<>();

    public static ColorHelper create(int digitId, String lang, Item dye, MaterialColor mapColor, DyeColor colorDye) {
        String id = lang.toLowerCase().replace(" ", "_");
        return new ColorHelper(digitId, dye, id, lang, mapColor, colorDye);
    }

    public ColorHelper() {}

    public ColorHelper(int digitId, Item colorItem, String colorId, String colorLang, MaterialColor color, DyeColor colorDye) {
        this.digitId = digitId;
        this.colorItem = colorItem;
        this.colorId = colorId;
        this.colorLang = colorLang;
        this.color = color;
        this.colorDye = colorDye;
        COLORS.add(this);
    }


    public static DefaultColorEnumProvider getSelectedColor(ColorHelper color) {
        return color == DefaultColorProvider.WHITE ? DefaultColorEnumProvider.WHITE :
               color == DefaultColorProvider.ORANGE ? DefaultColorEnumProvider.ORANGE :
               color == DefaultColorProvider.MAGENTA ? DefaultColorEnumProvider.MAGENTA :
               color == DefaultColorProvider.LIGHT_BLUE ? DefaultColorEnumProvider.LIGHT_BLUE :
               color == DefaultColorProvider.YELLOW ? DefaultColorEnumProvider.YELLOW :
               color == DefaultColorProvider.LIME ? DefaultColorEnumProvider.LIME :
               color == DefaultColorProvider.PINK ? DefaultColorEnumProvider.PINK :
               color == DefaultColorProvider.GRAY ? DefaultColorEnumProvider.GRAY :
               color == DefaultColorProvider.LIGHT_GRAY ? DefaultColorEnumProvider.LIGHT_GRAY :
               color == DefaultColorProvider.CYAN ? DefaultColorEnumProvider.CYAN :
               color == DefaultColorProvider.PURPLE ? DefaultColorEnumProvider.PURPLE :
               color == DefaultColorProvider.BLUE ? DefaultColorEnumProvider.BLUE :
               color == DefaultColorProvider.BROWN ? DefaultColorEnumProvider.BROWN :
               color == DefaultColorProvider.GREEN ? DefaultColorEnumProvider.GREEN :
               color == DefaultColorProvider.RED ? DefaultColorEnumProvider.RED :
                                                    DefaultColorEnumProvider.BLACK;
    }
}
