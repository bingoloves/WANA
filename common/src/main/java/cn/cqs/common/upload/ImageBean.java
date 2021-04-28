package cn.cqs.common.upload;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by bingo on 2021/4/28.
 *
 * @Author: bingo
 * @Email: 657952166@qq.com
 * @Description: 类作用描述
 * @UpdateUser: 更新者
 * @UpdateDate: 2021/4/28
 */

public class ImageBean implements MultiItemEntity{
    public static final int TYPE_ADD = 0;
    public static final int TYPE_IMAGE = 1;
    private String path;
    private String name;
    private String content;
    private int itemType = TYPE_IMAGE;

    @Override
    public int getItemType() {
        return itemType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
