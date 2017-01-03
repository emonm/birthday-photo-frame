package com.creativeitem.newyear.photoframe.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.creativeitem.newyear.photoframe.R;

import java.util.ArrayList;
import java.util.List;


public class RatioRelativeLayout extends RelativeLayout {
    protected View dragHandle;
    protected List<View> dropTargets = new ArrayList<View>();
    protected boolean dragging = false;
    protected int pointerId;
    protected int selectedDropTargetIndex = -1;
    protected int lastSelectedDropTargetIndex = -1;
    protected int lastDroppedIndex = -1;
    protected boolean allowHorizontalDrag = true;
    protected boolean allowVerticalDrag = true;
    protected DropListener dropListener;
    private float heightRatio;
    private int maxHeight;

    public RatioRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttrs(context, attrs);
        heightRatio = attrs.getAttributeFloatValue(null, "heightRatio", 1.0f);
        maxHeight = -1;
    }


    public RatioRelativeLayout(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    public RatioRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyAttrs(context, attrs);
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // We have to manually resize the child views to match the parent.
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final LayoutParams params = (LayoutParams) child.getLayoutParams();

            if (params.height == LayoutParams.MATCH_PARENT) {
                params.height = bottom - top;
            }
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) Math.ceil(this.heightRatio * (float) width);
        if (maxHeight != -1 && height > maxHeight) height = maxHeight;
        setMeasuredDimension(width, height);
    }

    public DropListener getDropListener() {
        return dropListener;
    }

    public void setDropListener(DropListener dropListener) {
        this.dropListener = dropListener;
    }

    public View getDragHandle() {
        return dragHandle;
    }

    public void setDragHandle(View dragHandle) {
        this.dragHandle = dragHandle;
        setupDragHandle();
    }

    public boolean isAllowHorizontalDrag() {
        return allowHorizontalDrag;
    }

    public void setAllowHorizontalDrag(boolean allowHorizontalDrag) {
        this.allowHorizontalDrag = allowHorizontalDrag;
    }

    public boolean isAllowVerticalDrag() {
        return allowVerticalDrag;
    }

    public void setAllowVerticalDrag(boolean allowVerticalDrag) {
        this.allowVerticalDrag = allowVerticalDrag;
    }

    protected void applyAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ConstrainedDragAndDropView, 0, 0);


        try {
            /*
            layoutId = a.getResourceId(R.styleable.ConstrainedDragAndDropView_layoutId, 0);
            if (layoutId > 0) {
                LayoutInflater.from(context).inflate(layoutId, this, true);
            }
            */
        } finally {
            a.recycle();
        }
    }

    protected void setupDragHandle() {
        this.setOnTouchListener(new DragAreaTouchListener());
    }

    protected void onActionDown(View view, MotionEvent motionEvent) {
        // if we're not already dragging, and the touch position is on the drag handle,
        // then start dragging
        if (!dragging && isDragHandleTouch(motionEvent)) {
            pointerId = motionEvent.getPointerId(0);
            updateDragPosition(motionEvent);
            dragging = true;
            Log.d("drag", "drag start");
        }
    }

    protected void onActionUp(View view, MotionEvent motionEvent) {

        // if we're dragging, then stop dragging
        if (dragging && motionEvent.getPointerId(0) == pointerId) {
            updateDragPosition(motionEvent);
            dragging = false;
            Log.d("drag", "drag end");

            // find out what drop target, if any, the drag handle was dropped on
            int dropTargetIndex = findDropTargetIndexUnderDragHandle();

            if (dropTargetIndex >= 0) { // if drop was on a target, select the target
                Log.d("drag", "drop on target " + dropTargetIndex);
                selectDropTarget(dropTargetIndex);
                snapDragHandleToDropTarget(dropTargetIndex);
                lastDroppedIndex = dropTargetIndex;
                if (dropListener != null) {
                    dropListener.onDrop(dropTargetIndex, dropTargets.get(dropTargetIndex));
                }
            } else { // if drop was not on a target, re-select the last selected target
                deselectDropTarget();
                snapDragHandleToDropTarget(lastDroppedIndex);
            }
        }
    }

    protected void onActionMove(View view, MotionEvent motionEvent) {
        if (dragging && motionEvent.getPointerId(0) == pointerId) {
            updateDragPosition(motionEvent);
            int dropTargetIndex = findDropTargetIndexUnderDragHandle();
            if (dropTargetIndex >= 0) {
                Log.d("drag", "hover on target " + dropTargetIndex);
                selectDropTarget(dropTargetIndex);
            } else {
                deselectDropTarget();
            }
        }
    }

    @SuppressLint("NewApi")
    protected void updateDragPosition(MotionEvent motionEvent) {

        // this is where we constrain the movement of the dragHandle

        if (allowHorizontalDrag) {
            float candidateX = motionEvent.getX() - dragHandle.getWidth() / 2;
            if (candidateX > 0 && candidateX + dragHandle.getWidth() < this.getWidth()) {
                dragHandle.setX(candidateX);
            }
        }

        if (allowVerticalDrag) {
            float candidateY = motionEvent.getY() - dragHandle.getHeight() / 2;
            if (candidateY > 0 && candidateY + dragHandle.getHeight() < this.getHeight()) {
                dragHandle.setY(candidateY);
            }
        }
    }

    @SuppressLint("NewApi")
    protected void snapDragHandleToDropTarget(int dropTargetIndex) {
        if (dropTargetIndex > -1) {
            View dropTarget = dropTargets.get(dropTargetIndex);
            float xCenter = dropTarget.getX() + dropTarget.getWidth() / 2;
            float yCenter = dropTarget.getY() + dropTarget.getHeight() / 2;

            float xOffset = dragHandle.getWidth() / 2;
            float yOffset = dragHandle.getHeight() / 2;

            float x = xCenter - xOffset;
            float y = yCenter - yOffset;

            dragHandle.setX(x);
            dragHandle.setY(y);
        }
    }

    protected boolean isDragHandleTouch(MotionEvent motionEvent) {
        Point point = new Point(
                new Float(motionEvent.getRawX()).intValue(),
                new Float(motionEvent.getRawY()).intValue()
        );

        return isPointInView(point, dragHandle);
    }

    protected int findDropTargetIndexUnderDragHandle() {
        int dropTargetIndex = -1;
        for (int i = 0; i < dropTargets.size(); i++) {
            if (isCollision(dragHandle, dropTargets.get(i))) {
                dropTargetIndex = i;
                break;
            }
        }

        return dropTargetIndex;
    }

    /**
     * Determines whether a raw screen coordinate is within the bounds of the specified view
     *
     * @param point - Point containing screen coordinates
     * @param view  - View to test
     * @return true if the point is in the view, else false
     */
    protected boolean isPointInView(Point point, View view) {

        int[] viewPosition = new int[2];
        view.getLocationOnScreen(viewPosition);

        int left = viewPosition[0];
        int right = left + view.getWidth();
        int top = viewPosition[1];
        int bottom = top + view.getHeight();

        return point.x >= left && point.x <= right && point.y >= top && point.y <= bottom;
    }

    @SuppressLint("NewApi")
    protected boolean isCollision(View view1, View view2) {
        boolean collision = false;


        do {
            if (view1.getY() + view1.getHeight() < view2.getY()) {
                break;
            }

            if (view1.getY() > view2.getY() + view2.getHeight()) {
                break;
            }

            if (view1.getX() > view2.getX() + view2.getWidth()) {
                break;
            }

            if (view1.getX() + view1.getWidth() < view2.getX()) {
                break;
            }

            collision = true;

        } while (false);

        return collision;
    }

    protected void selectDropTarget(int index) {
        if (index > -1) {
            deselectDropTarget();
            selectedDropTargetIndex = index;
            dropTargets.get(selectedDropTargetIndex).setSelected(true);
        }
    }

    protected void deselectDropTarget() {
        if (selectedDropTargetIndex > -1) {
            dropTargets.get(selectedDropTargetIndex).setSelected(false);
            lastSelectedDropTargetIndex = selectedDropTargetIndex;
            selectedDropTargetIndex = -1;
        }
    }

    public interface DropListener {
        public void onDrop(final int dropIndex, final View dropTarget);
    }

    protected class DragAreaTouchListener implements OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onActionDown(view, motionEvent);
                    break;

                case MotionEvent.ACTION_UP:
                    onActionUp(view, motionEvent);
                    break;

                case MotionEvent.ACTION_MOVE:
                    onActionMove(view, motionEvent);
                    break;

                default:
                    break;
            }

            return true;
        }
    }
}

