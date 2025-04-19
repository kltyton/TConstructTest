package com.kltyton.test;

import com.kltyton.test.fluid.*;
import com.kltyton.test.modifiers.TestModifier;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Test.MODID)
public class Test {
    public static final String MODID = "test";
    private static final Logger LOGGER = LogUtils.getLogger();

    //注册器
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Item> FROST_STAR_ITEM = ITEMS.register("frost_star", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ANTIHEMOSTATIC_TABLET_ITEM = ITEMS.register("antihemostatic_tablet", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIRST_LIFE_ALLOY_ITEM = ITEMS.register("first_life_alloy", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GHOST_ALLOY_ITEM = ITEMS.register("ghost_alloy", () -> new Item(new Item.Properties()));




    // 流体类型注册
    public static final RegistryObject<FluidType> FROST_STAR_FLUID_TYPE = FLUID_TYPES.register(
            "frost_star_fluid",
            () -> new FrostStarFluidType(FluidType.Properties.create()
                    .density(1500)
                    .viscosity(2000)
                    .temperature(250)
            )
    );
    public static final RegistryObject<FluidType> ANTIHEMOSTATIC_TABLET_FLUID_TYPE = FLUID_TYPES.register(
            "antihemostatic_tablet_fluid",
            () -> new AntihemostaticTabletFluidType(FluidType.Properties.create()
                    .density(1500)
                    .viscosity(2000)
                    .temperature(250)
            )
    );
    public static final RegistryObject<FluidType> FIRST_LIFE_ALLOY_FLUID_TYPE = FLUID_TYPES.register(
            "first_life_alloy_fluid",
            () -> new FirstLifeAlloyFluidType(FluidType.Properties.create()
                    .density(1500)
                    .viscosity(2000)
                    .temperature(250)
            )
    );
    public static final RegistryObject<FluidType> GHOST_ALLOY_FLUID_TYPE = FLUID_TYPES.register(
            "ghost_alloy_fluid",
            () -> new GhostAlloyFluidType(FluidType.Properties.create()
                    .density(1500)
                    .viscosity(2000)
                    .temperature(250)
            )
    );

    // 流体注册
    public static final RegistryObject<FlowingFluid> FROST_STAR_FLUID = FLUIDS.register(
            "frost_star_fluid",
            () -> new FrostStarFluid.Source(Test.FROST_STAR_FLUID_PROPERTIES)
    );

    public static final RegistryObject<FlowingFluid> ANTIHEMOSTATIC_TABLET_FLUID = FLUIDS.register(
            "antihemostatic_tablet_fluid",
            () -> new AntihemostaticTabletFluid.Source(Test.ANTIHEMOSTATIC_TABLET_FLUID_PROPERTIES)
    );
    public static final RegistryObject<FlowingFluid> FIRST_LIFE_ALLOY_FLUID = FLUIDS.register(
            "first_life_alloy_fluid",
            () -> new FirstLifeAlloyFluid.Source(Test.FIRST_LIFE_ALLOY_FLUID_PROPERTIES)
    );
    public static final RegistryObject<FlowingFluid> GHOST_ALLOY_FLUID = FLUIDS.register(
            "ghost_alloy_fluid",
            () -> new GhostAlloyFluid.Source(Test.GHOST_ALLOY_FLUID_PROPERTIES)
    );

    //(流动中)
    public static final RegistryObject<FlowingFluid> FLOWING_FROST_STAR_FLUID = FLUIDS.register(
            "flowing_frost_star_fluid",
            () -> new FrostStarFluid.Flowing(Test.FROST_STAR_FLUID_PROPERTIES)
    );
    public static final RegistryObject<FlowingFluid> FLOWING_ANTIHEMOSTATIC_TABLET_FLUID = FLUIDS.register(
            "flowing_antihemostatic_tablet_fluid",
            () -> new AntihemostaticTabletFluid.Flowing(Test.ANTIHEMOSTATIC_TABLET_FLUID_PROPERTIES)
    );
    public static final RegistryObject<FlowingFluid> FLOWING_FIRST_LIFE_ALLOY_FLUID = FLUIDS.register(
            "flowing_first_life_alloy_fluid",
            () -> new FirstLifeAlloyFluid.Flowing(Test.FIRST_LIFE_ALLOY_FLUID_PROPERTIES)
    );
    public static final RegistryObject<FlowingFluid> FLOWING_GHOST_ALLOY_FLUID = FLUIDS.register(
            "flowing_ghost_alloy_fluid",
            () -> new GhostAlloyFluid.Flowing(Test.GHOST_ALLOY_FLUID_PROPERTIES)
    );

    // 流体方块注册
    public static final RegistryObject<LiquidBlock> FROST_STAR_FLUID_BLOCK = BLOCKS.register(
            "frost_star_fluid_block",
            () -> new LiquidBlock(Test.FROST_STAR_FLUID, Block.Properties.copy(Blocks.WATER))
    );
    public static final RegistryObject<LiquidBlock> ANTIHEMOSTATIC_TABLET_FLUID_BLOCK = BLOCKS.register(
            "antihemostatic_tablet_fluid_block",
            () -> new LiquidBlock(Test.ANTIHEMOSTATIC_TABLET_FLUID, Block.Properties.copy(Blocks.WATER))
    );
    public static final RegistryObject<LiquidBlock> FIRST_LIFE_ALLOY_FLUID_BLOCK = BLOCKS.register(
            "first_life_alloy_fluid_block",
            () -> new LiquidBlock(Test.FIRST_LIFE_ALLOY_FLUID, Block.Properties.copy(Blocks.WATER))
    );
    public static final RegistryObject<LiquidBlock> GHOST_ALLOY_FLUID_BLOCK = BLOCKS.register(
            "ghost_alloy_fluid_block",
            () -> new LiquidBlock(Test.GHOST_ALLOY_FLUID, Block.Properties.copy(Blocks.WATER))
    );

    // 桶物品注册
    public static final RegistryObject<Item> FROST_STAR_BUCKET = ITEMS.register(
            "frost_star_bucket",
            () -> new BucketItem(Test.FROST_STAR_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1))
    );
    public static final RegistryObject<Item> ANTIHEMOSTATIC_TABLET_BUCKET = ITEMS.register(
            "antihemostatic_tablet_bucket",
            () -> new BucketItem(Test.ANTIHEMOSTATIC_TABLET_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1))
    );
    public static final RegistryObject<Item> FIRST_LIFE_ALLOY_BUCKET = ITEMS.register(
            "first_life_alloy_bucket",
            () -> new BucketItem(Test.FIRST_LIFE_ALLOY_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1))
    );
    public static final RegistryObject<Item> GHOST_ALLOY_BUCKET = ITEMS.register(
            "ghost_alloy_bucket",
            () -> new BucketItem(Test.GHOST_ALLOY_FLUID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1))
    );

    // 流体属性定义（需静态初始化）
    public static final ForgeFlowingFluid.Properties FROST_STAR_FLUID_PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    FROST_STAR_FLUID_TYPE,
                    FROST_STAR_FLUID,
                    FLOWING_FROST_STAR_FLUID
            ).bucket(Test.FROST_STAR_BUCKET)
                    .block(Test.FROST_STAR_FLUID_BLOCK);
    // 流体属性定义（需静态初始化）
    public static final ForgeFlowingFluid.Properties ANTIHEMOSTATIC_TABLET_FLUID_PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    ANTIHEMOSTATIC_TABLET_FLUID_TYPE,
                    ANTIHEMOSTATIC_TABLET_FLUID,
                    FLOWING_ANTIHEMOSTATIC_TABLET_FLUID
            ).bucket(Test.ANTIHEMOSTATIC_TABLET_BUCKET)
                    .block(Test.ANTIHEMOSTATIC_TABLET_FLUID_BLOCK);
    public static final ForgeFlowingFluid.Properties FIRST_LIFE_ALLOY_FLUID_PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    FIRST_LIFE_ALLOY_FLUID_TYPE,
                    FIRST_LIFE_ALLOY_FLUID,
                    FLOWING_FIRST_LIFE_ALLOY_FLUID
            ).bucket(Test.FIRST_LIFE_ALLOY_BUCKET)
                    .block(Test.FIRST_LIFE_ALLOY_FLUID_BLOCK);
    public static final ForgeFlowingFluid.Properties GHOST_ALLOY_FLUID_PROPERTIES =
            new ForgeFlowingFluid.Properties(
                    GHOST_ALLOY_FLUID_TYPE,
                    GHOST_ALLOY_FLUID,
                    FLOWING_GHOST_ALLOY_FLUID
            ).bucket(Test.GHOST_ALLOY_BUCKET)
                    .block(Test.GHOST_ALLOY_FLUID_BLOCK);

    // 为示例项目创建一个 ID 为“test：example_tab”的创意选项卡，该选项卡位于 battle 选项卡之后
    public static final RegistryObject<CreativeModeTab> TEST_TAB = CREATIVE_MODE_TABS.register("test_tab", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("item_group.test_tab")) // 使用翻译键
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> FROST_STAR_ITEM.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(FROST_STAR_ITEM.get());
                        output.accept(ANTIHEMOSTATIC_TABLET_ITEM.get());
                        output.accept(FIRST_LIFE_ALLOY_ITEM.get());
                        output.accept(GHOST_ALLOY_ITEM.get());
                    })
                    .build()
    );





    public Test() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus); // 新增
        FLUIDS.register(modEventBus); // 新增
        FLUID_TYPES.register(modEventBus); // 新增
        ITEMS.register(modEventBus);
        // 将 Deferred Register 注册到 mod 事件总线，以便注册选项卡
        CREATIVE_MODE_TABS.register(modEventBus);
        TestModifier.MODIFIERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

    }






    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void gatherData(final GatherDataEvent event) {
            DataGenerator gen = event.getGenerator();
            ExistingFileHelper fileHelper = event.getExistingFileHelper();
            if (event.includeClient()) {
            }
            if (event.includeServer()) {
            }
        }
        @SubscribeEvent
        public static void onSetup(FMLClientSetupEvent event) {
        }
    }
    // 您可以使用 EventBusSubscriber 自动注册带有 @SubscribeEvent 注释的类中的所有静态方法
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
/*            event.enqueueWork(() -> {
                // 注册流体渲染
                IClientFluidTypeExtensions clientProperties = IClientFluidTypeExtensions.of(FROST_STAR_FLUID_TYPE.get());
                Minecraft.getInstance().getBlockColors().register((state, world, pos, tintIndex) ->
                        clientProperties.getTintColor(), FROST_STAR_FLUID_BLOCK.get());
            });*/
        }
    }
}
