package cn.cqs.common.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.permissionx.guolindev.callback.RequestCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.cqs.base.log.LogUtils;
import cn.cqs.common.R;
import cn.cqs.toast.ToastUtils;

/**
 * Created by bingo on 2021/3/26.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/3/26
 */
public class PhotoUtils {
    public static final int ERROR = R.drawable.image_error;
    /**
     *
     * @param activity
     * @param needCamera   是否需要拍照
     * @param requestCode  请求码
     * @param max          选择的照片最大数量
     */
    public static void choicePhotoWrapper(FragmentActivity activity, boolean needCamera, int requestCode, int max) {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        PermissionUtils.request(activity, permissions, new RequestCallback() {
            @Override
            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                if (allGranted){
                    // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能 目录名:默认包名
                    File takePhotoDir = new File(Environment.getExternalStorageDirectory(), activity.getPackageName());
                    Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(activity)
                            .cameraFileDir(needCamera ? takePhotoDir : null) //拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                            .maxChooseCount(max) // 图片选择张数的最大值
                            .selectedPhotos(null) // 当前已选中的图片路径集合
                            .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                            .build();
                    activity.startActivityForResult(photoPickerIntent, requestCode);
                } else {
                    ToastUtils.show("图片选择需要以下权限:\n\n1.访问设备上的照片"+(needCamera?"\n\n2.拍照":""));
                }
            }
        });
    }
    /**
     * 预览单张图片
     * @param context
     * @param path
     */
    public static void preview(Context context,String path){
        BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(context)
                .saveImgDir(null)// 保存图片的目录，如果传 null，则没有保存图片功能
        ;
        photoPreviewIntentBuilder.previewPhoto(path);
        context.startActivity(photoPreviewIntentBuilder.build());
    }

    /**
     * 预览多张图片
     * @param context
     * @param paths
     * @param position 当前预览图片的索引
     */
    public static void preview(Context context, ArrayList<String> paths, int position){
        BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(context)
                .saveImgDir(null)// 保存图片的目录，如果传 null，则没有保存图片功能
                ;
        photoPreviewIntentBuilder.previewPhotos(paths)
                .currentPosition(position);
        context.startActivity(photoPreviewIntentBuilder.build());
    }
    /**
     * 加载图片
     * @param context
     * @param path
     * @param imageView
     */
    public static void load(Context context, String path, ImageView imageView){
        Glide.with(context).load(path)
                .placeholder(ERROR)
                .error(ERROR)
                .into(imageView);
    }
    /**
     * 加载本地资源图片
     * @param context
     * @param resId
     * @param imageView
     */
    public static void load(Context context, int resId, ImageView imageView){
        Glide.with(context).load(resId)
                .placeholder(ERROR)
                .error(ERROR)
                .into(imageView);
    }
    /**
     * 加载圆角图片
     * @param context
     * @param path
     * @param roundingRadius dp
     * @param imageView
     */
    public static void load(Context context, String path,int roundingRadius,ImageView imageView){
        RequestOptions coverRequestOptions = new RequestOptions()
                .transform(new CenterCrop(),new RoundedCorners(roundingRadius))
                .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
//                .skipMemoryCache(true)//不做内存缓存
                ;
        Glide.with(context).load(path)
                .apply(coverRequestOptions)
                .placeholder(ERROR)
                .error(ERROR)
                .into(imageView);
    }
    /**
     * 加载圆角本地资源图片
     * @param context
     * @param resId
     * @param roundingRadius dp
     * @param imageView
     */
    public static void load(Context context,int resId,int roundingRadius,ImageView imageView){
        RequestOptions coverRequestOptions = new RequestOptions()
                .transform(new CenterCrop(),new RoundedCorners(roundingRadius))
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(resId)
                .apply(coverRequestOptions)
                .placeholder(ERROR)
                .error(ERROR)
                .into(imageView);
    }
}
