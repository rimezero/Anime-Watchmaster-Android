package com.cspeitch.animewatchmaster.utils;

import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;

/**
 * Created by admin on 6/13/2016.
 */
public class EffectUtils {
    public static LayoutAnimationController getgridlayoutAnim()
    {
        LayoutAnimationController controller;
        Animation anim = new RotateAnimation(90f,0f,0,0.5f,0,0.5f);
        anim.setDuration(500);
        controller=new LayoutAnimationController(anim,0.1f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        return controller;
    }
}
