package com.alptugkafkasli.bankapp

import android.os.Parcelable

import android.os.Parcel

data class BranchItem(
    val ID: Int,
    val dc_SEHIR: String,
    val dc_BANKA_SUBE: String,
    val dc_BANKA_TIPI: String,
    val dc_BANK_KODU: String,
    val dc_ADRES_ADI: String,
    val dc_ADRES: String,
    val dc_POSTA_KODU: String,
    val dc_ON_OFF_LINE: String,
    val dc_ON_OFF_SITE: String,
    val dc_BOLGE_KOORDINATORLUGU: String,
    val dc_EN_YAKIM_ATM: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ID)
        parcel.writeString(dc_SEHIR)
        parcel.writeString(dc_BANKA_SUBE)
        parcel.writeString(dc_BANKA_TIPI)
        parcel.writeString(dc_BANK_KODU)
        parcel.writeString(dc_ADRES_ADI)
        parcel.writeString(dc_ADRES)
        parcel.writeString(dc_POSTA_KODU)
        parcel.writeString(dc_ON_OFF_LINE)
        parcel.writeString(dc_ON_OFF_SITE)
        parcel.writeString(dc_BOLGE_KOORDINATORLUGU)
        parcel.writeString(dc_EN_YAKIM_ATM)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BranchItem> {
        override fun createFromParcel(parcel: Parcel): BranchItem {
            return BranchItem(parcel)
        }

        override fun newArray(size: Int): Array<BranchItem?> {
            return arrayOfNulls(size)
        }
    }
}

