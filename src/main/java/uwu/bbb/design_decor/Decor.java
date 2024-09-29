package uwu.bbb.design_decor;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import uwu.bbb.design_decor.data.DecorDatagen;
import uwu.bbb.design_decor.registry.DecorBlockEntities;
import uwu.bbb.design_decor.registry.DecorBlocks;
import uwu.bbb.design_decor.registry.DecorCreativeModeTabs;
import uwu.bbb.design_decor.registry.client.DecorPartialModels;
import uwu.bbb.design_decor.registry.client.DecorSpriteShifts;
import uwu.bbb.design_decor.registry.DecorBlockItems;
import uwu.bbb.design_decor.registry.helper.decor.ColorHelper;

import static uwu.bbb.design_decor.Decor.MOD_ID;

@SuppressWarnings("unused")
@Mod(MOD_ID)
public class Decor {

    public static final String NAME = "Create: Design n' Decor";
    public static final String MOD_ID = "design_decor";
    public static final String VERSION = "1.0a.Release";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static KineticStats create(Item item) {
        if (item instanceof BlockItem blockItem && blockItem.getBlock() instanceof IRotate) {
            return new KineticStats(blockItem.getBlock());
        }
        return null;
    }
    static {REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE).andThen(TooltipModifier.mapNull(Decor.create(item))));}

    public Decor() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        REGISTRATE.registerEventListeners(modEventBus);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> DecorPartialModels::init);

        ColorHelper.DefaultColorProvider.register();
        DecorCreativeModeTabs.init();
        DecorSpriteShifts.register();

        DecorBlocks.register();
        DecorBlockEntities.register();
        DecorBlockItems.register();




        modEventBus.addListener(Decor::init);
        modEventBus.addListener(EventPriority.LOWEST, DecorDatagen::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> DecorClient.onCtorClient(modEventBus, forgeEventBus));
        MinecraftForge.EVENT_BUS.register(this);
    }





    public static void init(final FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {});
    }
}
