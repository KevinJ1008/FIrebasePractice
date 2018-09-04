package com.kevinj1008.firebasepractice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Article {
    public String mAuthor;
    public String mTitle;
    public String mTag;
    public String mContent;
    public long mCreateTime;
    public String mAuthorTag;


    public Article () {

    }

    public Article(String author, String content, String title, String tag, String authorTag) {
        mAuthor = author;
        mContent = content;
        mTitle = title;
        mTag = tag;
        mCreateTime = new Date().getTime();
        mAuthorTag = authorTag;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> add = new HashMap<>();
        add.put("author", mAuthor);
        add.put("content", mContent);
        add.put("title", mTitle);
        add.put("tag", mTag);
        add.put("created_time", mCreateTime);
        add.put("author_tag", mAuthorTag);
        return add;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public long getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(long createTime) {
        mCreateTime = createTime;
    }
}
