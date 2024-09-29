package uwu.bbb.design_decor.content.blocks.storage_container;

import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.blockEntity.IMultiBlockEntityContainer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryWrapper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import uwu.bbb.design_decor.registry.DecorBlockEntities;
import uwu.bbb.design_decor.registry.helper.decor.ColorHelper;

import java.util.*;

import static uwu.bbb.design_decor.content.blocks.storage_container.ColoredStorageContainerBlock.COLOR;

@SuppressWarnings("unchecked")
public class ColoredStorageContainerBlockEntity extends SmartBlockEntity implements IMultiBlockEntityContainer.Inventory {

    protected LazyOptional<IItemHandler> itemCapability;

    protected ItemStackHandler inventory;
    protected BlockPos controller;
    protected BlockPos lastKnownPos;
    protected boolean updateConnectivity;
    protected int radius;
    protected int length;
    protected Direction.Axis axis;

    public ColoredStorageContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        inventory = new ItemStackHandler(AllConfigs.server().logistics.vaultCapacity.get()) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                updateComparators();
            }
        };

        itemCapability = LazyOptional.empty();
        radius = 1;
        length = 1;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    protected void updateConnectivity() {
        updateConnectivity = false;
        if (Objects.requireNonNull(getLevel()).isClientSide())
            return;
        if (!isController())
            return;
        ConnectivityHandler.formMulti(this);
        updateColors();
    }

    protected void updateComparators() {
        ColoredStorageContainerBlockEntity controllerBE = getControllerBE();
        if (controllerBE == null)
            return;

        Objects.requireNonNull(getLevel()).blockEntityChanged(controllerBE.worldPosition);

        BlockPos pos = controllerBE.getBlockPos();
        for (int y = 0; y < controllerBE.radius; y++) {
            for (int z = 0; z < (controllerBE.axis == Direction.Axis.X ? controllerBE.radius : controllerBE.length); z++) {
                for (int x = 0; x < (controllerBE.axis == Direction.Axis.Z ? controllerBE.radius : controllerBE.length); x++) {
                    getLevel().updateNeighbourForOutputSignal(pos.offset(x, y, z), getBlockState().getBlock());
                }
            }
        }
    }

    @SuppressWarnings("all")
    @Override
    public void tick() {
        super.tick();

        if (getController() == worldPosition)
            updateColors();

        if (lastKnownPos == null)
            lastKnownPos = getBlockPos();
        else if (!lastKnownPos.equals(worldPosition) && worldPosition != null) {
            onPositionChanged();
            return;
        }

        if (updateConnectivity)
            updateConnectivity();
    }

    @Override
    public BlockPos getLastKnownPos() {
        return lastKnownPos;
    }

    @Override
    public boolean isController() {
        return controller == null || worldPosition.getX() == controller.getX()
                && worldPosition.getY() == controller.getY() && worldPosition.getZ() == controller.getZ();
    }

    private void onPositionChanged() {
        removeController(true);
        lastKnownPos = worldPosition;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ColoredStorageContainerBlockEntity getControllerBE() {
        if (isController())
            return this;
        BlockEntity blockEntity = Objects.requireNonNull(getLevel()).getBlockEntity(controller);
        if (blockEntity instanceof ColoredStorageContainerBlockEntity)
            return (ColoredStorageContainerBlockEntity) blockEntity;
        return null;
    }

    public void removeController(boolean keepContents) {
        if (Objects.requireNonNull(getLevel()).isClientSide())
            return;
        updateConnectivity = true;
        controller = null;
        radius = 1;
        length = 1;

        BlockState state = getBlockState();
        if (ColoredStorageContainerBlock.isVault(state)) {
            state = state.setValue(ColoredStorageContainerBlock.LARGE, false);
            getLevel().setBlock(worldPosition, state, 22);
        }

        itemCapability.invalidate();
        setChanged();
        sendData();
    }

    @Override
    public void setController(BlockPos controller) {
        if (Objects.requireNonNull(getLevel()).isClientSide && !isVirtual())
            return;
        if (controller.equals(this.controller))
            return;
        this.controller = controller;
        itemCapability.invalidate();
        setChanged();
        sendData();
    }

    @Override
    public BlockPos getController() {
        return isController() ? worldPosition : controller;
    }

    public void updateColors() {
        for (int y = 0; y < getControllerBE().radius; y++) {
            for (int z = 0; z < (getControllerBE().axis == Direction.Axis.X ? getControllerBE().radius : getControllerBE().length); z++) {
                for (int x = 0; x < (getControllerBE().axis == Direction.Axis.Z ? getControllerBE().radius : getControllerBE().length); x++) {
                    BlockPos pos = getControllerBE().getBlockPos().offset(x, y, z);
                    BlockState stateAtPos = Objects.requireNonNull(getLevel()).getBlockState(pos);

                    if (stateAtPos.hasProperty(COLOR) && getMostColor() != null) {
                        getLevel().setBlockAndUpdate(pos, stateAtPos.setValue(COLOR, getMostColor()));
                    }
                }
            }
        }
    }

    public ColorHelper.DefaultColorEnumProvider getMostColor() {
        Map<ColorHelper.DefaultColorEnumProvider, Integer> colorCountMap = new HashMap<>();

        for (int y = 0; y < getControllerBE().radius; y++) {
            for (int z = 0; z < (getControllerBE().axis == Direction.Axis.X ? getControllerBE().radius : getControllerBE().length); z++) {
                for (int x = 0; x < (getControllerBE().axis == Direction.Axis.Z ? getControllerBE().radius : getControllerBE().length); x++) {
                    BlockPos pos = getControllerBE().getBlockPos().offset(x, y, z);
                    BlockState stateAtPos = Objects.requireNonNull(getLevel()).getBlockState(pos);

                    if (stateAtPos.hasProperty(COLOR)) {
                        ColorHelper.DefaultColorEnumProvider color = stateAtPos.getValue(COLOR);

                        colorCountMap.put(color, colorCountMap.getOrDefault(color, 0) + 1);
                    }
                }
            }
        }

        return colorCountMap.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }


    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);

        BlockPos controllerBefore = controller;
        int prevSize = radius;
        int prevLength = length;

        updateConnectivity = compound.contains("Uninitialized");
        controller = null;
        lastKnownPos = null;

        if (compound.contains("LastKnownPos"))
            lastKnownPos = NbtUtils.readBlockPos(compound.getCompound("LastKnownPos"));
        if (compound.contains("Controller"))
            controller = NbtUtils.readBlockPos(compound.getCompound("Controller"));

        if (isController()) {
            radius = compound.getInt("Size");
            length = compound.getInt("Length");
        }

        if (!clientPacket) {
            inventory.deserializeNBT(compound.getCompound("Inventory"));
            return;
        }

        @SuppressWarnings("all")
        boolean changeOfController = controllerBefore == null ? controller != null : !controllerBefore.equals(controller);
        if (hasLevel() && (changeOfController || prevSize != radius || prevLength != length))
            Objects.requireNonNull(getLevel()).setBlocksDirty(getBlockPos(), Blocks.AIR.defaultBlockState(), getBlockState());
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        if (updateConnectivity)
            compound.putBoolean("Uninitialized", true);
        if (lastKnownPos != null)
            compound.put("LastKnownPos", NbtUtils.writeBlockPos(lastKnownPos));
        if (!isController())
            compound.put("Controller", NbtUtils.writeBlockPos(controller));
        if (isController()) {
            compound.putInt("Size", radius);
            compound.putInt("Length", length);
        }

        super.write(compound, clientPacket);

        if (!clientPacket) {
            compound.putString("StorageType", "CombinedInv");
            compound.put("Inventory", inventory.serializeNBT());
        }
    }

    public ItemStackHandler getInventoryOfBlock() {
        return inventory;
    }

    public void applyInventoryToBlock(ItemStackHandler handler) {
        for (int i = 0; i < inventory.getSlots(); i++)
            inventory.setStackInSlot(i, i < handler.getSlots() ? handler.getStackInSlot(i) : ItemStack.EMPTY);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (isItemHandlerCap(cap)) {
            initCapability();
            return itemCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    private void initCapability() {
        if (itemCapability.isPresent())
            return;
        if (!isController()) {
            ColoredStorageContainerBlockEntity controllerBE = getControllerBE();
            if (controllerBE == null)
                return;
            controllerBE.initCapability();
            itemCapability = controllerBE.itemCapability;
            return;
        }

        boolean alongZ = ColoredStorageContainerBlock.getVaultBlockAxis(getBlockState()) == Direction.Axis.Z;
        IItemHandlerModifiable[] invs = new IItemHandlerModifiable[length * radius * radius];
        for (int yOffset = 0; yOffset < length; yOffset++) {
            for (int xOffset = 0; xOffset < radius; xOffset++) {
                for (int zOffset = 0; zOffset < radius; zOffset++) {
                    BlockPos vaultPos = alongZ ? worldPosition.offset(xOffset, zOffset, yOffset)
                            : worldPosition.offset(yOffset, xOffset, zOffset);
                    ColoredStorageContainerBlockEntity vaultAt =
                            ConnectivityHandler.partAt(DecorBlockEntities.COLORED_STORAGE_CONTAINER.get(), Objects.requireNonNull(getLevel()), vaultPos);
                    invs[yOffset * radius * radius + xOffset * radius + zOffset] =
                            vaultAt != null ? vaultAt.inventory : new ItemStackHandler();
                }
            }
        }

        IItemHandler itemHandler = new VersionedInventoryWrapper(new CombinedInvWrapper(invs));
        itemCapability = LazyOptional.of(() -> itemHandler);
    }

    public static int getMaxLength(int radius) {
        return radius * 3;
    }

    @Override
    public void preventConnectivityUpdate() { updateConnectivity = false; }

    @Override
    public void notifyMultiUpdated() {
        BlockState state = this.getBlockState();
        if (ColoredStorageContainerBlock.isVault(state)) { // safety
            Objects.requireNonNull(getLevel()).setBlock(getBlockPos(), state.setValue(ColoredStorageContainerBlock.LARGE, radius > 2), 6);
        }
        itemCapability.invalidate();
        setChanged();
    }

    @Override
    public Direction.Axis getMainConnectionAxis() { return getMainAxisOf(this); }

    @Override
    public int getMaxLength(Direction.Axis longAxis, int width) {
        if (longAxis == Direction.Axis.Y) return getMaxWidth();
        return getMaxLength(width);
    }

    @Override
    public int getMaxWidth() {
        return 3;
    }

    @Override
    public int getHeight() { return length; }

    @Override
    public int getWidth() { return radius; }

    @Override
    public void setHeight(int height) { this.length = height; }

    @Override
    public void setWidth(int width) { this.radius = width; }

    @Override
    public boolean hasInventory() { return true; }
}
