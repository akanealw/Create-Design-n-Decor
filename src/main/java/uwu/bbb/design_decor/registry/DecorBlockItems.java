package uwu.bbb.design_decor.registry;

import com.tterrag.registrate.util.entry.ItemEntry;
import uwu.bbb.design_decor.content.blocks.storage_container.ColoredStorageContainerItem;

import static uwu.bbb.design_decor.registry.helper.decor.ColorHelper.DefaultColorEnumProvider.*;

import static uwu.bbb.design_decor.Decor.REGISTRATE;

@SuppressWarnings("unused")
public class DecorBlockItems {

    public static final ItemEntry<ColoredStorageContainerItem> STORAGE_CONTAINERS =
            REGISTRATE.item("storage_container", p -> new ColoredStorageContainerItem(p, WHITE))
            .lang("Storage Container")
            .model((c, p) -> p.getExistingFile(p.modLoc("item/white_" + c.getName())))
            .tab(() -> DecorCreativeModeTabs.BASE_CREATIVE_TAB)
            .register();






    public static void register() {}
}