package com.example.newsreader.utils.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Class holds NewsApi article data.
 */
class Article() : Parcelable{

    var id: Long = 0

    /* Article's title. */
    var title: String? = null

    /* Article's text. */
    var description: String? = null

    /* Url to article's image. */
    var urlToImage: String? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        title = parcel.readString()
        description = parcel.readString()
        urlToImage = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(urlToImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }

}
