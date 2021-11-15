package engine.gui.control;

import com.github.mouse0w0.observable.value.MutableFloatValue;
import com.github.mouse0w0.observable.value.MutableObjectValue;
import com.github.mouse0w0.observable.value.NonNullMutableObjectValue;
import com.github.mouse0w0.observable.value.SimpleMutableFloatValue;
import engine.gui.Node;
import engine.gui.graphics.ItemViewRenderer;
import engine.gui.graphics.NodeRenderer;
import engine.item.ItemStack;

public class ItemView extends Node {
    private final MutableObjectValue<ItemStack> itemStack = new NonNullMutableObjectValue<>(ItemStack.EMPTY);
    private final MutableFloatValue size = new SimpleMutableFloatValue(80);

    public ItemView() {
        size().addChangeListener((ob, o, n) -> requestParentLayout());
    }

    public ItemView(ItemStack itemStack) {
        this();
        setItemStack(itemStack);
    }

    public final MutableObjectValue<ItemStack> itemStack() {
        return itemStack;
    }

    public final ItemStack getItemStack() {
        return itemStack.get();
    }

    public final void setItemStack(ItemStack itemStack) {
        this.itemStack.set(itemStack);
    }

    public final MutableFloatValue size() {
        return size;
    }

    public final float getSize() {
        return size.get();
    }

    public final void setSize(float size) {
        this.size.set(size);
    }

    @Override
    public float prefWidth() {
        return size.get();
    }

    @Override
    public float prefHeight() {
        return size.get();
    }

    @Override
    protected NodeRenderer createDefaultRenderer() {
        return ItemViewRenderer.INSTANCE;
    }
}
