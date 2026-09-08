package com.example.s8189084assignment2.data.model

import android.os.Parcel
import android.os.Parcelable

// Parcelable is implemented manually here instead of with @Parcelize.
// The kotlin-parcelize compiler plugin does not currently hook into AGP 9's
// built-in Kotlin compilation, so its codegen never runs (see Phase 4 notes).
data class Sport(
    val sportName: String,
    val playerCount: Int,
    val fieldType: String,
    val olympicSport: Boolean,
    val description: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        sportName = parcel.readString() ?: "",
        playerCount = parcel.readInt(),
        fieldType = parcel.readString() ?: "",
        olympicSport = parcel.readByte() != 0.toByte(),
        description = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sportName)
        parcel.writeInt(playerCount)
        parcel.writeString(fieldType)
        parcel.writeByte(if (olympicSport) 1 else 0)
        parcel.writeString(description)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Sport> {
        override fun createFromParcel(parcel: Parcel): Sport = Sport(parcel)
        override fun newArray(size: Int): Array<Sport?> = arrayOfNulls(size)
    }
}
