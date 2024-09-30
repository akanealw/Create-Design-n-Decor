package uwu.bbb.design_decor.registry;

import com.tterrag.registrate.util.entry.ItemEntry;
import uwu.bbb.design_decor.Utils;
import uwu.bbb.design_decor.content.blocks.storage_container.ColoredStorageContainerItem;

import static uwu.bbb.design_decor.registry.helper.decor.ColorHelper.DefaultColorEnumProvider.*;

import static uwu.bbb.design_decor.Decor.REGISTRATE;

@SuppressWarnings("unused")
public class DecorBlockItems {

    public static final ItemEntry<ColoredStorageContainerItem> STORAGE_CONTAINERS =
            REGISTRATE.item("storage_container", p -> new ColoredStorageContainerItem(p, WHITE))
            .lang("Storage Container")
            .model((c, p) -> p.withExistingParent("item/" + c.getName(), Utils.asResource("item/white_storage_container")))
            .tab(() -> DecorCreativeModeTabs.BASE_CREATIVE_TAB)
            .register();

    public static void register() {}
}