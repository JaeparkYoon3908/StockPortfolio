package com.yjpapp.stockportfolio.model

import android.os.Parcel
import android.os.Parcelable

data class MemoInfo(
    var id: Int,
    var date: String?,
    var title: String?,
    var content: String?
    ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(date)
        parcel.writeString(title)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MemoInfo> {
        override fun createFromParcel(parcel: Parcel): MemoInfo {
            return MemoInfo(parcel)
        }

        override fun newArray(size: Int): Array<MemoInfo?> {
            return arrayOfNulls(size)
        }
    }
}
