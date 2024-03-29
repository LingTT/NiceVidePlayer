package com.xiao.nicevideoplayer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by XiaoJianjun on 2017/7/6.
 * 切换清晰度对话框（仿腾讯视频切换清晰度的对话框）.
 */
public class ChangeClarityDialog extends Dialog {

    private LinearLayout mLinearLayout;
    private int mCurrentCheckedIndex;

    public ChangeClarityDialog(Context context) {
        super(context, R.style.dialog_change_clarity);
        init(context);
    }
    private Context mContext;
    private void init(Context context) {
        mContext = context;
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClarityNotChanged();
                }
                ChangeClarityDialog.this.dismiss();
            }
        });

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.MarginLayoutParams.MATCH_PARENT);
        setContentView(mLinearLayout, params);

        WindowManager.LayoutParams windowParams = getWindow().getAttributes();
        windowParams.width = NiceUtil.getScreenHeight(context);
        windowParams.height = NiceUtil.getScreenWidth(context);
        getWindow().setAttributes(windowParams);
    }

    /**
     * 设置清晰度等级
     *
     * @param items          清晰度等级items
     * @param defaultChecked 默认选中的清晰度索引
     */
    public void setClarityGrade(List<String> items, int defaultChecked) {
        mLinearLayout.removeAllViews();
        mCurrentCheckedIndex = defaultChecked;
        for (int i = 0; i < items.size(); i++) {
//            TextView itemView = (TextView) LayoutInflater.from(getContext())
//                    .inflate(R.layout.item_change_clarity, mLinearLayout, false);
            RelativeLayout mRlRoot = (RelativeLayout) LayoutInflater.from(getContext())
                    .inflate(R.layout.item_change_clarity, mLinearLayout, false);
            mRlRoot.setBackgroundColor(getContext().getResources().getColor(R.color.dialog_bg_2));
            TextView itemView = (TextView) mRlRoot.findViewById(R.id.tv_content);
            View viewLine = mRlRoot.findViewById(R.id.view_line);
            itemView.setTag(i);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int checkIndex = (int) v.getTag();
                        if (checkIndex != mCurrentCheckedIndex) {
                            for (int j = 0; j < mLinearLayout.getChildCount(); j++) {
                                mLinearLayout.getChildAt(j).setSelected(checkIndex == j);
                            }
                            mListener.onClarityChanged(checkIndex);
                            mCurrentCheckedIndex = checkIndex;
                        } else {
                            mListener.onClarityNotChanged();
                        }
                    }
                    ChangeClarityDialog.this.dismiss();
                }
            });
//            if(mCurrentCheckedIndex==((int)itemView.getTag())){
            if(mCurrentCheckedIndex==i){
                itemView.setSelected(true);
                viewLine.setVisibility(View.VISIBLE);
            }else{
                viewLine.setVisibility(View.GONE);
            }
            itemView.setText(items.get(i));
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)
                    mRlRoot.getLayoutParams();
//            params.topMargin = (i == 0) ? 0 : NiceUtil.dp2px(getContext(), 16f);
//            mLinearLayout.addView(itemView, params);
//            params.leftMargin = NiceUtil.dp2px(getContext(),10f);
            params.setMargins(NiceUtil.dp2px(getContext(),5f),0,
                    NiceUtil.dp2px(getContext(),5f),NiceUtil.dp2px(getContext(),20f));
            mLinearLayout.addView(mRlRoot, params);
        }
    }

    public interface OnClarityChangedListener {
        /**
         * 切换清晰度后回调
         *
         * @param clarityIndex 切换到的清晰度的索引值
         */
        void onClarityChanged(int clarityIndex);

        /**
         * 清晰度没有切换，比如点击了空白位置，或者点击的是之前的清晰度
         */
        void onClarityNotChanged();
    }

    private OnClarityChangedListener mListener;

    public void setOnClarityCheckedListener(OnClarityChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void onBackPressed() {
        // 按返回键时回调清晰度没有变化
        if (mListener != null) {
            mListener.onClarityNotChanged();
        }
        super.onBackPressed();
    }
}
