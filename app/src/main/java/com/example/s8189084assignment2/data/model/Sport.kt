package com.example.s8189084assignment2.data.model

import android.os.Parcel
import android.os.Parcelable

// One sport's details, passed from Dashboard to Details through Safe Args.
//
// Parcelable is written manually, since kotlin-parcelize doesn't work with this project's Kotlin setup.
data class Sport(
    val sportName: String,
    val playerCount: Int,
    val fieldType: String,
    val olympicSport: Boolean,
    val description: String
) : Parcelable {

    // Rebuilds a Sport from a Parcel.
    constructor(parcel: Parcel) : this(
        sportName = parcel.readString() ?: "",
        playerCount = parcel.readInt(),
        fieldType = parcel.readString() ?: "",
        olympicSport = parcel.readByte() != 0.toByte(),
        description = parcel.readString() ?: ""
    )

    // Writes every field into the Parcel.
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(sportName)
        parcel.writeInt(playerCount)
        parcel.writeString(fieldType)
        parcel.writeByte(if (olympicSport) 1 else 0)
        parcel.writeString(description)
    }

    override fun describeContents(): Int = 0

    // Required by Parcelable, turns a Parcel back into a Sport.
    companion object CREATOR : Parcelable.Creator<Sport> {
        override fun createFromParcel(parcel: Parcel): Sport = Sport(parcel)
        override fun newArray(size: Int): Array<Sport?> = arrayOfNulls(size)
    }
}
