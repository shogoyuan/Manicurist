package com.qiang.manicurists.bean;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * 搜索的建议关键词的实体类
 * Created by Administrator on 2016/7/21.
 */
public class Suggestion implements SearchSuggestion {

    private String mSuggestionName;
    private boolean mIsHistory = false;

    public Suggestion(String key) {
        this.mSuggestionName = key.toLowerCase();
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }


    @Override
    public String getBody() {
        return mSuggestionName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSuggestionName);
        dest.writeInt(mIsHistory ? 1 : 0);
    }

    public Suggestion(Parcel source) {
        this.mSuggestionName = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public static final Creator<Suggestion> CREATOR = new Creator<Suggestion>() {
        @Override
        public Suggestion createFromParcel(Parcel in) {
            return new Suggestion(in);
        }

        @Override
        public Suggestion[] newArray(int size) {
            return new Suggestion[size];
        }
    };
}
