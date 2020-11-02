package org.woopy.entity;

/**
 * @author woopy
 * @data 2020/8/18 - 17:08
 */
public class BlogTag {

    private int blogsId;
    private int tagsId;

    public int getBlogsId() {
        return blogsId;
    }

    public void setBlogsId(int blogsId) {
        this.blogsId = blogsId;
    }

    public int getTagsId() {
        return tagsId;
    }

    public void setTagsId(int tagsId) {
        this.tagsId = tagsId;
    }

    @Override
    public String toString() {
        return "BlogTag{" +
                "blogsId=" + blogsId +
                ", tagsId=" + tagsId +
                '}';
    }
}
