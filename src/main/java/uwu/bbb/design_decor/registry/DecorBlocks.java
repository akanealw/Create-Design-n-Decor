package uwu.bbb.design_decor.registry;

import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.vault.ItemVaultBlock;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import uwu.bbb.design_decor.Utils;
import uwu.bbb.design_decor.content.blocks.storage_container.ColoredStorageContainerBlock;
import uwu.bbb.design_decor.content.blocks.storage_container.ColoredStorageContainerCTBehaviour;
import uwu.bbb.design_decor.registry.helper.decor.ColorHelper;


import static com.simibubi.create.foundation.data.CreateRegistrate.connectedTextures;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static uwu.bbb.design_decor.Decor.REGISTRATE;

public class DecorBlocks {

    public static final BlockEntry<ColoredStorageContainerBlock> COLORED_STORAGE_CONTAINER = regColoredStorageContainer();

    private static BlockEntry<ColoredStorageContainerBlock> regColoredStorageContainer() {
        return REGISTRATE.block("colored_storage_container", ColoredStorageContainerBlock::new)
                .lang("Storage Container")
                .initialProperties(SharedProperties::softMetal)
                .properties(p -> p.color(MaterialColor.COLOR_GRAY)
                        .sound(SoundType.NETHERITE_BLOCK)
                        .explosionResistance(1200))
                .transform(pickaxeOnly())
                .blockstate((c, p) -> p.getVariantBuilder(c.get()).forAllStates(s -> {
                    ResourceLocation refModel = Create.asResource("block/item_vault");
                    ResourceLocation refModelItem = Create.asResource("item/item_vault");

                    ColorHelper.DefaultColorEnumProvider color = s.getValue(ColoredStorageContainerBlock.COLOR);
                    ResourceLocation path0 = Utils.asResource("block/storage_container/" + color.get().digitId + "_storage_container_bottom_small");
                    ResourceLocation path1 = Utils.asResource("block/storage_container/" + color.get().digitId + "_storage_container_front_small");
                    ResourceLocation path2 = Utils.asResource("block/storage_container/" + color.get().digitId + "_storage_container_side_small");
                    ResourceLocation path3 = Utils.asResource("block/storage_container/" + color.get().digitId + "_storage_container_top_small"); //particle


                    ModelFile model = p.models().withExistingParent("block/storage_containers/" + color.get().colorId, refModel)
                            .texture("0", path0)
                            .texture("1", path1)
                            .texture("2", path2)
                            .texture("3", path3)
                            .texture("particle", path3);

                    p.models().withExistingParent("item/" + color.get().colorId + "_storage_container", refModelItem)
                            .parent(model);

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(s.getValue(ItemVaultBlock.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0)
                            .build();
                }))
                .onRegister(connectedTextures(ColoredStorageContainerCTBehaviour::new))
                .register();
    }

    public static void register() {
    }
}
