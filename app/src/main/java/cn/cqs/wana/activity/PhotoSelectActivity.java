package cn.cqs.wana.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.gyf.immersionbar.ImmersionBar;
import com.permissionx.guolindev.callback.RequestCallback;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.cqs.common.adapter.QuickAdapter;
import cn.cqs.common.base.BaseActivity;
import cn.cqs.common.upload.NineImageAdapter;
import cn.cqs.common.upload.NineImageView;
import cn.cqs.common.upload.NineViewCallback;
import cn.cqs.common.utils.PermissionUtils;
import cn.cqs.common.utils.PhotoUtils;
import cn.cqs.wana.R;

/**
 * Created by bingo on 2021/4/28.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 图片选择框架
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/28
 */
public class PhotoSelectActivity extends BaseActivity {
    @BindView(R.id.nineImageView)
    NineImageView nineImageView;
    @OnClick(R.id.btn_select)
    public void clickEvent(){
        selectPhotos();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_select);
        initNineView();
    }

    @Override
    protected void initImmersionbar() {
        ImmersionBar.with(this).titleBar(R.id.title_bar).statusBarDarkFont(true).init();
    }
    private final int RC_CHOOSE_PHOTO = 1;
    private void initNineView() {
        nineImageView.setMax(4);
//        nineImageView.setEditable(false);
        nineImageView.setNineViewCallback(new NineViewCallback() {
            @Override
            public void addImage() {
                int addableNum = nineImageView.getAddableNum();
                if (addableNum > 0){
                    PhotoUtils.choicePhotoWrapper((FragmentActivity) getActivity(),false, RC_CHOOSE_PHOTO,addableNum);
                } else {
                    toast("最多可以选择" + nineImageView.getMax() + "个");
                }
            }

            @Override
            public void onPreview(int position, List<String> images) {
                if (!images.isEmpty()){
                    PhotoUtils.preview(getActivity(), (ArrayList<String>) images,position);
                }
            }

            @Override
            public void deleteImage(int position) {
                nineImageView.remove(position);
            }
        });
    }

    private void selectPhotos() {
        PermissionUtils.request(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (allGranted){
                    PhotoUtils.choicePhotoWrapper((FragmentActivity) getActivity(),false, RC_CHOOSE_PHOTO,nineImageView.getAddableNum());
                } else {
                    toast("缺少必要权限");
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            ArrayList<String> selectedPhotos = BGAPhotoPickerActivity.getSelectedPhotos(data);
            nineImageView.addImages(selectedPhotos);
        }
    }
}
