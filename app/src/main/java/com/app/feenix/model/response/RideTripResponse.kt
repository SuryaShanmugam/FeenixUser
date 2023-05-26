package com.app.biu.model.RequestModel.ResponseModel

import android.os.Parcel
import android.os.Parcelable


data class RideTripResponse(
    val success: Boolean?,
    val data: MutableList<RideTripResponseData>
)

data class RideTripResponseData(
    val id: String?,
    val booking_id: String?,
    val user_id: String?,
    val provider_id: String?,
    val current_provider_id: String?,
    val service_type_id: String?,
    val status: String?,
    val eta: String?,
    val cancelled_by: String?,
    val cancelled_reason: String?,
    val payment_mode: String?,
    val paid: String?,
    val driver_payout: String?,
    val driver_payment_id: String?,
    val s_address: String?,
    val s_latitude: String?,
    val s_longitude: String?,
    val d_address: String?,
    val d_latitude: String?,
    val d_longitude: String?,
    val assigned_at: String?,
    val accepted_at: String?,
    val started_at: String?,
    val finished_at: String?,
    val use_wallet: String?,
    val user_rated: String?,
    val provider_rated: String?,
    val receiver_name: String?,
    val receiver_mobile: String?,
    val pickup_instruction: String?,
    val delivery_instruction: String?,
    val package_type: String?,
    val package_details: String?,
    val confirmation_code: String?,
    val fleet_id: String?,
    val notification: String?,
    val total: Double?,
    val s_title: String?,
    val d_title: String?,
    val reroute: String?,
    val estimated_fare: String?,
    val distance_price: String?,
    val time: String?,
    val time_price: String?,
    val tax_price: String?,
    val base_price: String?,
    val wallet_balance: String?,
    val payment_image: String?,
    val payment: RideTripPayment?,
    val service_type: RideTripServiceType?,
    val user: RideTripUser?,
    val provider: RideTripProvider?,
    val provider_profiles: RideTripProviderProfiles?,
//    val rating: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable<RideTripPayment?>(RideTripPayment::class.java.classLoader),
        parcel.readParcelable<RideTripServiceType?>(RideTripServiceType::class.java.classLoader),
        parcel.readParcelable<RideTripUser?>(RideTripUser::class.java.classLoader),
        parcel.readParcelable<RideTripProvider?>(RideTripPayment::class.java.classLoader),
        parcel.readParcelable<RideTripProviderProfiles?>(RideTripPayment::class.java.classLoader),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(booking_id)
        parcel.writeString(user_id)
        parcel.writeString(provider_id)
        parcel.writeString(current_provider_id)
        parcel.writeString(service_type_id)
        parcel.writeString(status)
        parcel.writeString(eta)
        parcel.writeString(cancelled_by)
        parcel.writeString(cancelled_reason)
        parcel.writeString(payment_mode)
        parcel.writeString(paid)
        parcel.writeString(driver_payout)
        parcel.writeString(driver_payment_id)
        parcel.writeString(s_address)
        parcel.writeString(s_latitude)
        parcel.writeString(s_longitude)
        parcel.writeString(d_address)
        parcel.writeString(d_latitude)
        parcel.writeString(d_longitude)
        parcel.writeString(assigned_at)
        parcel.writeString(accepted_at)
        parcel.writeString(finished_at)
        parcel.writeString(use_wallet)
        parcel.writeString(user_rated)
        parcel.writeString(provider_rated)
        parcel.writeString(receiver_name)
        parcel.writeString(receiver_mobile)
        parcel.writeString(pickup_instruction)
        parcel.writeString(delivery_instruction)
        parcel.writeString(package_type)
        parcel.writeString(package_details)
        parcel.writeString(confirmation_code)
        parcel.writeString(fleet_id)
        parcel.writeString(notification)
        parcel.writeValue(total)
        parcel.writeString(s_title)
        parcel.writeString(d_title)
        parcel.writeString(reroute)
        parcel.writeString(estimated_fare)
        parcel.writeString(distance_price)
        parcel.writeString(time)
        parcel.writeString(time_price)
        parcel.writeString(tax_price)
        parcel.writeString(base_price)
        parcel.writeString(wallet_balance)
        parcel.writeString(payment_image)
        parcel.writeParcelable(payment, flags)
        parcel.writeParcelable(service_type, flags)
        parcel.writeParcelable(user, flags)
        parcel.writeParcelable(provider, flags)
        parcel.writeParcelable(provider_profiles, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RideTripResponseData> {
        override fun createFromParcel(parcel: Parcel): RideTripResponseData {
            return RideTripResponseData(parcel)
        }

        override fun newArray(size: Int): Array<RideTripResponseData?> {
            return arrayOfNulls(size)
        }
    }
}

data class RideTripPayment(
    val id: String?,
    val request_id: String?,
    val promocode_id: String?,
    val payment_mode: String?,
    val fixed: Double,
    val distance_taken: String?,
    val time_taken: String?,
    val commision: String?,
    val drivercommision: String?,
    val time_price: String?,
    val distance_price: String?,
    val discount: String?,
    val tax: String?,
    val wallet: String?,
    val total: String?,
    val time: String?,
    val distance: String?,
    val trip_fare: String?,
    val sub_total: String?,
    val driver_earnings: String?,
    val money_to_wallet: String?,
    val donation: String?,
    val amount_to_collect: String?,
    val minimum_fare: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(request_id)
        parcel.writeString(promocode_id)
        parcel.writeString(payment_mode)
        parcel.writeDouble(fixed)
        parcel.writeString(distance_taken)
        parcel.writeString(time_taken)
        parcel.writeString(commision)
        parcel.writeString(drivercommision)
        parcel.writeString(time_price)
        parcel.writeString(distance_price)
        parcel.writeString(discount)
        parcel.writeString(tax)
        parcel.writeString(wallet)
        parcel.writeString(total)
        parcel.writeString(time)
        parcel.writeString(distance)
        parcel.writeString(trip_fare)
        parcel.writeString(sub_total)
        parcel.writeString(driver_earnings)
        parcel.writeString(money_to_wallet)
        parcel.writeString(donation)
        parcel.writeString(amount_to_collect)
        parcel.writeString(minimum_fare)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RideTripPayment> {
        override fun createFromParcel(parcel: Parcel): RideTripPayment {
            return RideTripPayment(parcel)
        }

        override fun newArray(size: Int): Array<RideTripPayment?> {
            return arrayOfNulls(size)
        }
    }
}

data class RideTripProviderProfiles(
    val car_registration: String?,
    val provider_id: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(car_registration)
        parcel.writeString(provider_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RideTripProviderProfiles> {
        override fun createFromParcel(parcel: Parcel): RideTripProviderProfiles {
            return RideTripProviderProfiles(parcel)
        }

        override fun newArray(size: Int): Array<RideTripProviderProfiles?> {
            return arrayOfNulls(size)
        }
    }
}

data class RideTripServiceType(
    val id: String?,
    val name: String?,
    val provider_name: String?,
    val image: String?,
    val fixed: String?,
    val minimum_fare: String?,
    val base_radius: String?,
    val price: String?,
    val time: String?,
    val commission: String?,
    val drivercommission: String?,
    val calculator: String?,
    val description: String?,
    val status: String?,
    val type: String?,
    val is_delivery: Int

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(provider_name)
        parcel.writeString(image)
        parcel.writeString(fixed)
        parcel.writeString(minimum_fare)
        parcel.writeString(base_radius)
        parcel.writeString(price)
        parcel.writeString(time)
        parcel.writeString(commission)
        parcel.writeString(drivercommission)
        parcel.writeString(calculator)
        parcel.writeString(description)
        parcel.writeString(status)
        parcel.writeString(type)
        parcel.writeInt(is_delivery)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RideTripServiceType> {
        override fun createFromParcel(parcel: Parcel): RideTripServiceType {
            return RideTripServiceType(parcel)
        }

        override fun newArray(size: Int): Array<RideTripServiceType?> {
            return arrayOfNulls(size)
        }
    }
}

data class RideTripUser(
    val id: String?,
    val first_name: String?,
    val last_name: String?,
    val payment_mode: String?,
    val email: String?,
    val picture: String?,
    val device_token: String?,
    val device_id: String?,
    val device_type: String?,
    val login_by: String?,
    val referal: String?,
    val mobile: String?,
    val country_code: String?,
    val archive: String?,
    val fleet: String?

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(payment_mode)
        parcel.writeString(email)
        parcel.writeString(picture)
        parcel.writeString(device_token)
        parcel.writeString(device_id)
        parcel.writeString(device_type)
        parcel.writeString(login_by)
        parcel.writeString(referal)
        parcel.writeString(mobile)
        parcel.writeString(country_code)
        parcel.writeString(archive)
        parcel.writeString(fleet)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RideTripUser> {
        override fun createFromParcel(parcel: Parcel): RideTripUser {
            return RideTripUser(parcel)
        }

        override fun newArray(size: Int): Array<RideTripUser?> {
            return arrayOfNulls(size)
        }
    }
}

data class RideTripProvider(
    val id: String?,
    val first_name: String?,
    val last_name: String?,
    val payment_mode: String?,
    val email: String?,
    val avatar: String?,
    val status: String?,
    val wallet_balance: String?,
    val rating_count: String?,
    val referal: String?,
    val rating: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(payment_mode)
        parcel.writeString(email)
        parcel.writeString(avatar)
        parcel.writeString(status)
        parcel.writeString(wallet_balance)
        parcel.writeString(rating_count)
        parcel.writeString(referal)
        parcel.writeDouble(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RideTripProvider> {
        override fun createFromParcel(parcel: Parcel): RideTripProvider {
            return RideTripProvider(parcel)
        }

        override fun newArray(size: Int): Array<RideTripProvider?> {
            return arrayOfNulls(size)
        }
    }
}
