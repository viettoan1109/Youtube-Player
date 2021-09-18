package com.hello.youtubeplayer.data.local;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SearchSugesstion implements Parcelable {

    @NonNull
    private String searchTerm = "";
    @NonNull
    private String date = "";

    public SearchSugesstion() {
    }

    public SearchSugesstion(@NonNull String searchTerm, @NonNull String date) {
        searchTerm = searchTerm;
        date = date;
    }

    protected SearchSugesstion(Parcel in) {
        searchTerm = in.readString();
        date = in.readString();
    }

    public static final Creator<SearchSugesstion> CREATOR = new Creator<SearchSugesstion>() {
        @Override
        public SearchSugesstion createFromParcel(Parcel in) {
            return new SearchSugesstion(in);
        }

        @Override
        public SearchSugesstion[] newArray(int size) {
            return new SearchSugesstion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(searchTerm);
        dest.writeString(date);
    }

    @NonNull
    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(@NonNull String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }
}
