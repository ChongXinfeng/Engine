package engine.gui;

import com.github.mouse0w0.observable.value.MutableObjectValue;
import com.github.mouse0w0.observable.value.NonNullMutableObjectValue;
import engine.gui.graphics.NodeRenderer;
import engine.gui.graphics.RegionRenderer;
import engine.gui.misc.*;
import engine.math.Math2;

public class Region extends Parent {

    public static final float USE_COMPUTED_VALUE = Size.USE_COMPUTED_VALUE;
    public static final float USE_PERF_VALUE = Size.USE_PERF_VALUE;

    private MutableObjectValue<Background> background;
    private MutableObjectValue<Border> border;
    private MutableObjectValue<Insets> padding;

    private final Size size = new Size();

    public final MutableObjectValue<Background> background() {
        if (background == null) {
            background = new NonNullMutableObjectValue<>(Background.NOTHING);
        }
        return background;
    }

    public final Background getBackground() {
        return background == null ? Background.NOTHING : background.get();
    }

    public final void setBackground(Background background) {
        background().set(background);
    }

    public final MutableObjectValue<Border> border() {
        if (border == null) {
            border = new NonNullMutableObjectValue<>(Border.NO_BORDER);
        }
        return border;
    }

    public final Border getBorder() {
        return border == null ? Border.NO_BORDER : border.get();
    }

    public final void setBorder(Border border) {
        border().set(border);
    }

    public final MutableObjectValue<Insets> padding() {
        if (padding == null) {
            padding = new NonNullMutableObjectValue<>(Insets.EMPTY);
        }
        return padding;
    }

    public final Insets getPadding() {
        return padding == null ? Insets.EMPTY : padding.get();
    }

    public final void setPadding(Insets padding) {
        padding().set(padding);
    }

    public final Size getSize() {
        return size;
    }

    public float getMinWidth() {
        return size.getMinWidth();
    }

    public void setMinWidth(float minWidth) {
        size.setMinWidth(minWidth);
    }

    public float getMinHeight() {
        return size.getMinHeight();
    }

    public void setMinHeight(float minHeight) {
        size.setMinHeight(minHeight);
    }

    public void setMinSize(float width, float height) {
        size.setMinSize(width, height);
    }

    public float getPrefWidth() {
        return size.getPrefWidth();
    }

    public void setPrefWidth(float prefWidth) {
        size.setPrefWidth(prefWidth);
    }

    public float getPrefHeight() {
        return size.getPrefHeight();
    }

    public void setPrefHeight(float prefHeight) {
        size.setPrefHeight(prefHeight);
    }

    public void setPrefSize(float width, float height) {
        size.setPrefSize(width, height);
    }

    public float getMaxWidth() {
        return size.getMaxWidth();
    }

    public void setMaxWidth(float maxWidth) {
        size.setMaxWidth(maxWidth);
    }

    public float getMaxHeight() {
        return size.getMaxHeight();
    }

    public void setMaxHeight(float maxHeight) {
        size.setMaxHeight(maxHeight);
    }

    public void setMaxSize(float width, float height) {
        size.setMaxSize(width, height);
    }

    public static void positionInArea(Node child, float areaX, float areaY, float areaWidth, float areaHeight,
                                      float areaBaselineOffset, Insets margin, HPos halignment, VPos valignment, boolean isSnapToPixel) {
        Insets childMargin = margin != null ? margin : Insets.EMPTY;

        position(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset,
                childMargin.getTop(),
                childMargin.getRight(),
                childMargin.getBottom(),
                childMargin.getLeft(),
                halignment, valignment, isSnapToPixel);
    }

    protected static float snap(float value, boolean snapToPixel) {
        return snapToPixel ? Math.round(value) : value;
    }

    private static void position(Node child, float areaX, float areaY, float areaWidth, float areaHeight,
                                 float areaBaselineOffset,
                                 float topMargin, float rightMargin, float bottomMargin, float leftMargin,
                                 HPos hpos, VPos vpos, boolean isSnapToPixel) {
        final float xoffset = leftMargin + computeXOffset(areaWidth - leftMargin - rightMargin,
                child.getWidth(), hpos);
        final float yoffset;
        /*if (vpos == Pos.VPos.BASELINE) {
            double bo = child.getBaselineOffset();
            if (bo == BASELINE_OFFSET_SAME_AS_HEIGHT) {
                // We already know the layout bounds at this stage, so we can use them
                yoffset = areaBaselineOffset - child.getLayoutBounds().getHeight();
            } else {
                yoffset = areaBaselineOffset - bo;
            }
        } else */
        {
            yoffset = topMargin + computeYOffset(areaHeight - topMargin - bottomMargin,
                    child.getHeight(), vpos);
        }
        final float x = snap(areaX + xoffset, isSnapToPixel);
        final float y = snap(areaY + yoffset, isSnapToPixel);

        child.relocate(x, y);
    }

    static float computeXOffset(float width, float contentWidth, HPos hpos) {
        switch (hpos) {
            case LEFT:
                return 0;
            case CENTER:
                return (width - contentWidth) / 2;
            case RIGHT:
                return width - contentWidth;
            default:
                throw new AssertionError("Unhandled hPos");
        }
    }

    static float computeYOffset(float height, float contentHeight, VPos vpos) {
        switch (vpos) {
            case BASELINE:
            case TOP:
                return 0;
            case CENTER:
                return (height - contentHeight) / 2;
            case BOTTOM:
                return height - contentHeight;
            default:
                throw new AssertionError("Unhandled vPos");
        }
    }

    @Override
    public float minWidth() {
        float minWidth = getMinWidth();
        if (minWidth == USE_COMPUTED_VALUE) {
            minWidth = super.minWidth();
        } else if (minWidth == USE_PERF_VALUE) {
            minWidth = prefWidth();
        }
        return minWidth < 0 ? 0 : minWidth;
    }

    @Override
    public float minHeight() {
        float minHeight = getMinHeight();
        if (minHeight == USE_COMPUTED_VALUE) {
            minHeight = super.minHeight();
        } else if (minHeight == USE_PERF_VALUE) {
            minHeight = prefHeight();
        }
        return minHeight < 0 ? 0 : minHeight;
    }

    @Override
    public final float prefWidth() {
        float width = getPrefWidth();
        if (width == USE_COMPUTED_VALUE) {
            return computeWidth();
        }
        return width;
    }

    public float computeWidth() {
        return super.prefWidth();
    }

    @Override
    public final float prefHeight() {
        float height = getPrefHeight();
        if (height == USE_COMPUTED_VALUE) {
            return computeHeight();
        }
        return height;
    }

    public float computeHeight() {
        return super.prefHeight();
    }

    @Override
    public float maxWidth() {
        float maxWidth = getMaxWidth();
        if (maxWidth == USE_COMPUTED_VALUE) {
            maxWidth = super.maxWidth();
        } else if (maxWidth == USE_PERF_VALUE) {
            maxWidth = prefWidth();
        }
        return maxWidth < 0 ? 0 : maxWidth;
    }

    @Override
    public float maxHeight() {
        float maxHeight = getMaxHeight();
        if (maxHeight == USE_COMPUTED_VALUE) {
            maxHeight = super.maxHeight();
        } else if (maxHeight == USE_PERF_VALUE) {
            maxHeight = prefHeight();
        }
        return maxHeight < 0 ? 0 : maxHeight;
    }

    @Override
    protected void layoutChildren() {
        Insets padding = getPadding();
        float x = padding.getLeft();
        float y = padding.getTop();
        float w = getWidth() - x - padding.getRight();
        float h = getHeight() - y - padding.getBottom();
        layoutChildren(x, y, w, h);
    }

    protected void layoutChildren(float contentX, float contentY, float contentWidth, float contentHeight) {
        for (Node node : getChildren()) {
            layoutInArea(node, contentX + node.getLayoutX(), contentY + node.getLayoutY(), prefWidth(node), prefHeight(node));
        }
    }

    @Override
    protected NodeRenderer createDefaultRenderer() {
        return RegionRenderer.INSTANCE;
    }

    protected void layoutInArea(Node c, float areaX, float areaY, float areaWidth, float areaHeight, int areaBaselineOffset, Insets margin, HPos hAlign, VPos vAlign) {
        Insets childMargin = margin != null ? margin : Insets.EMPTY;

        float top = childMargin.getTop();
        float bottom = childMargin.getBottom();
        float left = childMargin.getLeft();
        float right = childMargin.getRight();

//        if (valignment == VPos.BASELINE) {
//            double bo = child.getBaselineOffset();
//            if (bo == BASELINE_OFFSET_SAME_AS_HEIGHT) {
//                if (child.isResizable()) {
//                    // Everything below the baseline is like an "inset". The Node with BASELINE_OFFSET_SAME_AS_HEIGHT cannot
//                    // be resized to this area
//                    bottom += snapSpace(areaHeight - areaBaselineOffset, isSnapToPixel);
//                } else {
//                    top = snapSpace(areaBaselineOffset - child.getLayoutBounds().getHeight(), isSnapToPixel);
//                }
//            } else {
//                top = snapSpace(areaBaselineOffset - bo, isSnapToPixel);
//            }
//        }


//        if (child.isResizable()) {
//            Vec2d size = boundedNodeSizeWithBias(child, areaWidth - left - right, areaHeight - top - bottom,
//                    fillWidth, fillHeight, TEMP_VEC2D);
//        }
        c.resize(areaWidth - left - right, areaHeight - top - bottom);
        position(c, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset,
                top, right, bottom, left, hAlign, vAlign, true);
    }

    protected float computeChildPrefAreaWidth(Node child, float baselineComplement, Insets margin, float height, boolean fillHeight) {
        float left = margin != null ? margin.getLeft() : 0;
        float right = margin != null ? margin.getRight() : 0;
        float alt = -1;
        if (height != -1) {
            //TODO width depends on height
        }
        return left + Math2.clamp(child.minWidth(), child.prefWidth(), child.maxWidth()) + right;
    }

    protected float computeChildPrefAreaHeight(Node child, float baselineComplement, Insets margin, float width) {
        float top = margin != null ? margin.getTop() : 0;
        float bottom = margin != null ? margin.getBottom() : 0;
        float alt = -1;
        if (false) {
            //TODO height depends on width
        }

        if (baselineComplement != -1) {
            return 0; //TODO
        } else {
            return top + Math2.clamp(child.minHeight(), child.prefHeight(), child.maxHeight()) + bottom;
        }
    }
}
