package com.jarvisdong.uikit.baseui.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by JarvisDong on 2017/10/30.
 * OverView:生命周期管理类-容器
 * 记录fragment状态;
 *
 * 为了统一管理fragment
 *
 */

public class LifeCycleComponentManager implements IComponentContainer {
    private HashMap<String, LifeCycleComponent> mComponentList;

    /**
     * Try to add component to container
     *
     * @param component
     * @param matrixContainer
     */
    public static void tryAddComponentToContainer(LifeCycleComponent component, Object matrixContainer) {
        tryAddComponentToContainer(component, matrixContainer, true);
    }

    public static boolean tryAddComponentToContainer(LifeCycleComponent component, Object matrixContainer, boolean throwEx) {
        if (matrixContainer instanceof IComponentContainer) {
            ((IComponentContainer) matrixContainer).addComponent(component);
            return true;
        } else {
            if (throwEx) {
                throw new IllegalArgumentException("componentContainerContext should implements IComponentContainer");
            }
            return false;
        }
    }

    @Override
    public void addComponent(LifeCycleComponent component) {
        if (component != null) {
            if (mComponentList == null) {
                mComponentList = new HashMap<String, LifeCycleComponent>();
            }
            mComponentList.put(component.toString(), component);
        }
    }

    public void onBecomesVisibleFromTotallyInvisible() {
        if (mComponentList == null) {
            return;
        }

        Iterator<Map.Entry<String, LifeCycleComponent>> it = mComponentList.entrySet().iterator();
        while (it.hasNext()) {
            LifeCycleComponent component = it.next().getValue();
            if (component != null) {
                component.onBecomesVisibleFromTotallyInvisible();
            }
        }
    }
    public void onBecomesPartiallyInvisible(){
        if (mComponentList == null) {
            return;
        }
        Iterator<Map.Entry<String, LifeCycleComponent>> it = mComponentList.entrySet().iterator();
        while (it.hasNext()) {
            LifeCycleComponent component = it.next().getValue();
            if (component != null) {
                component.onBecomesPartiallyInvisible();
            }
        }
    }
    public void onBecomesVisible(){
        if (mComponentList == null) {
            return;
        }
        Iterator<Map.Entry<String, LifeCycleComponent>> it = mComponentList.entrySet().iterator();
        while (it.hasNext()) {
            LifeCycleComponent component = it.next().getValue();
            if (component != null) {
                component.onBecomesVisible();
            }
        }
    }

    public void onBecomesTotallyInvisible() {
        if (mComponentList == null) {
            return;
        }
        Iterator<Map.Entry<String, LifeCycleComponent>> it = mComponentList.entrySet().iterator();
        while (it.hasNext()) {
            LifeCycleComponent component = it.next().getValue();
            if (component != null) {
                component.onBecomesTotallyInvisible();
            }
        }
    }

    public void onDestroy() {
        if (mComponentList == null) {
            return;
        }
        Iterator<Map.Entry<String, LifeCycleComponent>> it = mComponentList.entrySet().iterator();
        while (it.hasNext()) {
            LifeCycleComponent component = it.next().getValue();
            if (component != null) {
                component.onDestroy();
            }
        }
    }
}