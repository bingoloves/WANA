package cn.cqs.common.upload;

import java.util.List;

/**
 * Created by bingo on 2021/4/16.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/16
 */

public interface NineViewCallback {
    void addImage();
    void onPreview(int position, List<String> images);
    void deleteImage(int position);
}
