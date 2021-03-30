package com.centerm.oversea.libcamera.view;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * author：huangmin
 * time：1/28/21
 * describe：
 */
public class ZoomGestureListener implements GestureDetector.OnGestureListener {
    private static final String TAG = "ZoomGestureListener";
    private ZoomListener listener;
    private float currentDistance = 0;
    private float lastDistance = 0;

    public ZoomGestureListener(ZoomListener zoomListener) {
        this.listener = zoomListener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
//        Log.i(TAG, "onDown: 按下");
        listener.click(e.getX(), e.getY());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
//        Log.i(TAG, "onShowPress: 刚碰上还没松开");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        Log.i(TAG, "onSingleTapUp: 轻轻一碰后马上松开");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Log.i(TAG, "onScroll: 按下后拖动");
        // 大于两个触摸点
        if (e2.getPointerCount() >= 2) {
            //event中封存了所有屏幕被触摸的点的信息，第一个触摸的位置可以通过event.getX(0)/getY(0)得到
            float offSetX = e2.getX(0) - e2.getX(1);
            float offSetY = e2.getY(0) - e2.getY(1);
            //运用三角函数的公式，通过计算X,Y坐标的差值，计算两点间的距离
            currentDistance = (float) Math.sqrt(offSetX * offSetX + offSetY * offSetY);
            if (lastDistance == 0) {//如果是第一次进行判断
                lastDistance = currentDistance;
            } else {
                if (currentDistance - lastDistance > 8) {
                    // 放大
                    listener.zoomIn();
                } else if (lastDistance - currentDistance > 8) {
                    // 缩小
                    listener.zoomOut();
                }
            }
            //在一次缩放操作完成后，将本次的距离赋值给lastDistance，以便下一次判断
            //但这种方法写在move动作中，意味着手指一直没有抬起，监控两手指之间的变化距离超过10
            //就执行缩放操作，不是在两次点击之间的距离变化来判断缩放操作
            //故这种将本次距离留待下一次判断的方法，不能在两次点击之间使用
            lastDistance = currentDistance;
        }
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
//        Log.i(TAG, "onLongPress: 长按屏幕");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        Log.i(TAG, "onFling: 滑动后松开");
        return true;
    }

    public interface ZoomListener {
        /**
         * 放大
         */
        void zoomIn();

        /**
         * 缩小
         */
        void zoomOut();

        void click(float x, float y);
    }
}
