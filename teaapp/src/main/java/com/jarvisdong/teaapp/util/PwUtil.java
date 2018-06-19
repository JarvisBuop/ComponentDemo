package com.jarvisdong.teaapp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.util.ArrayList;


/**
 * Created by JarvisDong on 2017/4/11.
 * OverView: popupwindow工具类;
 */

public class PwUtil {
    /**
     * 弹框选择;
     */
    public static PopupWindow mPopupWindow;

    public static PopupWindow createPw(View view, View locationView, int x, int y) {
        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        popupWindow.showAtLocation(locationView, Gravity.RIGHT | Gravity.TOP, x, y);
        return popupWindow;
    }

    //首页搜索;
    public static PopupWindow createPw(View view, View locationView, int x, int y, boolean isAllScreen) {
        PopupWindow popupWindow = new PopupWindow(view, isAllScreen ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAsDropDown(locationView, x, y);
        return popupWindow;
    }

    /**
     * 弹出框宽度为锚宽度;
     *
     * @param view
     * @param locationView
     * @param mListener
     * @return
     */
    public static PopupWindow createSimplePw(View view, View locationView, @Nullable final PopupWindow.OnDismissListener mListener) {
        int measuredWidth = locationView.getMeasuredWidth();
        PopupWindow popupWindow = new PopupWindow(view, measuredWidth != 0 ? measuredWidth : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mListener != null) {
                    mListener.onDismiss();
                }
            }
        });
        popupWindow.showAsDropDown(locationView);
        return popupWindow;
    }



//    @SuppressWarnings("unchecked")
//    public static void doPopup(Context context, final View localView, final List mTargetList, @Nullable final ICommonListener mListener) {
//        if (mTargetList == null) return;
//        if (mPopupWindow != null && mPopupWindow.isShowing()) {
//            mPopupWindow.dismiss();
//            mPopupWindow = null;
//        } else {
//            if (isChange) {
//                ((TextView) localView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_arrow_up_black, 0);
//            }
//            View viewLayout = LayoutInflater.from(context).inflate(R.layout.component_my_popup_type4, null);
//            viewLayout.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
//                        mPopupWindow.dismiss();
//                        mPopupWindow = null;
//                    }
//                    return true;
//                }
//            });
//            mPopupWindow = PwUtil.createSimplePw(viewLayout, localView, new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    //修改图片上下;
//                    if (isChange) {
//                        ((TextView) localView).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_arrow_down_black, 0);
//                    }
//                    mPopupWindow = null;
//                }
//            });
//            RecyclerView mRecyclerView = (RecyclerView) viewLayout.findViewById(R.id.recycler_view);
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//            CommonAdapter mCommonAdapter = new CommonAdapter(context, R.layout.component_my_item_pop2, mTargetList) {
//                @Override
//                protected void convert(ViewHolder holder, final Object bean, final int position) {
//                    TextView view = holder.getView(R.id.txt_pop);
//                    Object objBean = mTargetList.get(position);
//                    configBean(view, objBean);
//                    holder.getView(R.id.ll_pop).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mListener.clickPostBack(v, position, bean);
//                            if (mPopupWindow != null && mPopupWindow.isShowing()) {
//                                mPopupWindow.dismiss();
//                                mPopupWindow = null;
//                            }
//                        }
//                    });
//                }
//            };
//            mRecyclerView.setAdapter(mCommonAdapter);
//        }
//    }

    //单个
    public static void alert(final Context mContext , ArrayList<String> list) {
        if (list == null || list.isEmpty()) return;
        String[] arr = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        new AlertDialog.Builder(mContext)
                .setTitle("联系:")
                .setItems(arr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
    }

}
