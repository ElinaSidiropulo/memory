package com.example.memorytrainer.models

import android.os.Parcel
import android.os.Parcelable

data class Card(
    val value: Int,
    val name: String,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false // ✅ Add this line
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte() // ✅ Read isMatched
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(value)
        parcel.writeString(name)
        parcel.writeByte(if (isFlipped) 1 else 0)
        parcel.writeByte(if (isMatched) 1 else 0) // ✅ Write isMatched
    }

    override fun describeContents(): Int = 0

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Card> = object : Parcelable.Creator<Card> {
            override fun createFromParcel(parcel: Parcel): Card = Card(parcel)
            override fun newArray(size: Int): Array<Card?> = arrayOfNulls(size)
        }
    }
}
