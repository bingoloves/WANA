package cn.cqs.common.form;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseViewHolder;
import java.util.ArrayList;
import java.util.List;
import cn.cqs.base.log.LogUtils;
import cn.cqs.common.R;
import cn.cqs.common.widget.MaxRecyclerView;

/**
 * Created by bingo on 2021/4/29.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 选择组件
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/29
 */

public class SelectView extends FrameLayout{
    private GridLayoutManager layoutManager;
    private MaxRecyclerView recyclerView;
    private SelectAdapter adapter;
    private int spanCount = 4;
    /**
     * 是否单选
     */
    private boolean isRadio = true;
    public SelectView(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public SelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        recyclerView = new MaxRecyclerView(context);
        recyclerView.setLayoutParams(new LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
        addView(recyclerView);
        initRecyclerView(context);
    }

    private void initRecyclerView(Context context) {
        layoutManager = new GridLayoutManager(context,spanCount);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SelectAdapter(new ArrayList<>()){
            @Override
            protected void convertTypeCheckbox(BaseViewHolder helper, FormValue item) {
                super.convertTypeCheckbox(helper, item);
                CheckBox checkBox = helper.getView(R.id.checkbox);
                checkBox.setText(item.getName());
                checkBox.setOnCheckedChangeListener(null);
                checkBox.setChecked(item.isSelected());
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isRadio){
                        setRadioSelect(helper.getAdapterPosition(),isChecked);
                    } else {
                        item.setSelected(isChecked);
                    }
                });
            }

            @Override
            protected void convertTypeRadio(BaseViewHolder helper, FormValue item) {
                super.convertTypeRadio(helper, item);
//                RadioButton radioButton = helper.getView(R.id.radio);
//                radioButton.setText(item.getName());
//                radioButton.setOnClickListener(null);
//                radioButton.setSelected(item.isSelected());
//                radioButton.setOnClickListener(v -> setRadioSelect(helper.getAdapterPosition()));
            }

            @Override
            public int getItemViewType(int position) {
                return FormValue.CHECKBOX;
//                return isRadio? FormValue.RAIDO:FormValue.CHECKBOX;
            }
        };
        recyclerView.setAdapter(adapter);
    }
    /**
     * 对TextView设置不同状态时其文字颜色
     * @param normal
     * @param pressed
     * @param focused
     * @param unable
     * @return
     */
    public static ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };
        int[][] states = new int[6][];
        states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
        states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
        states[2] = new int[] { android.R.attr.state_enabled };
        states[3] = new int[] { android.R.attr.state_focused };
        states[4] = new int[] { android.R.attr.state_window_focused };
        states[5] = new int[] {};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }
    /**
     * 设置当选
     */
    private void setRadioSelect(int position,boolean isChecked){
        List<FormValue> data = adapter.getData();
        for (int i = 0; i < data.size(); i++) {
            FormValue formValue = data.get(i);
            formValue.setSelected(position == i && isChecked);
        }
        recyclerView.post(() -> adapter.notifyDataSetChanged());
    }
    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        layoutManager.setSpanCount(spanCount);
    }

    public boolean isRadio() {
        return isRadio;
    }

    public void setRadio(boolean radio) {
        isRadio = radio;
    }

    /**
     * 设置数据
     * @param list
     */
    public void setData(List<FormValue> list){
        if (list != null){
            adapter.setNewData(list);
        }
    }

    private List<FormValue> getFormValues(){
        return adapter.getData();
    }

    /**
     * 获取单选值
     * @return
     */
    private String getRadioValue(){
        if (isRadio){
            for (FormValue formValue : adapter.getData()) {
                if (formValue.isSelected()){
                    return formValue.getValue();
                }
            }
        } else {
            throw new IllegalArgumentException("it is not radio type");
        }
        return null;
    }

    /**
     * 获取多选值
     * @return
     */
    private List<String> getCheckboxValue(){
        if (!isRadio){
            List<String> result = new ArrayList<>();
            for (FormValue formValue : adapter.getData()) {
                if (formValue.isSelected()){
                    result.add(formValue.getValue());
                }
            }
            return result;
        } else {
            throw new IllegalArgumentException("it is not checkbox type");
        }
    }
}
