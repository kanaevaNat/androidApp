package com.kanaeva.mobilecursach.rest.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.Instant

public class ResultTestDto : Serializable {

    @SerializedName("id")
    var id: Long = 0L

    @SerializedName("userId")
    var userId: Long= 0L

    @SerializedName("extraversionType")
    val extraversionType: Long= 0L

    @SerializedName("spontaneityType")
    val spontaneityType: Long= 0L

    @SerializedName("aggressivenessType")
    val aggressivenessType: Long= 0L

    @SerializedName("rigidityType")
    val rigidityType: Long= 0L

    @SerializedName("introversionType")
    val introversionType: Long= 0L

    @SerializedName("sensitivityType")
    val sensitivityType: Long= 0L

    @SerializedName("anxietyType")
    val anxietyType: Long= 0L

    @SerializedName("labilityType")
    val labilityType: Long= 0L

    @SerializedName("lieType")
    val lieType: Long= 0L

    @SerializedName("aggravationType")
    val aggravationType: Long= 0L

    @SerializedName("socialSupportSearchType")
    val socialSupportSearchType: Long= 0L

    @SerializedName("antocipitaryAvoidanceType")
    val antocipitaryAvoidanceType: Long= 0L

    @SerializedName("stressfulSituationAvoidType")
    val stressfulSituationAvoidType: Long= 0L

    @SerializedName("socialIsolationType")
    val socialIsolationType: Long= 0L

    @SerializedName("brokenRecordType")
    val brokenRecordType: Long= 0L

    @SerializedName("helplessnesType")
    val helplessnesType: Long= 0L

    @SerializedName("selfPityType")
    val selfPityType: Long= 0L

    @SerializedName("selfAccusationType")
    val selfAccusationType: Long= 0L

    @SerializedName("agressionType")
    val agressionType: Long= 0L

    @SerializedName("takinMedType")
    val takinMedType: Long= 0L

    @SerializedName("finishedAt")
    val finishedAt: String = ""

    override fun toString(): String {
        return "ResultTestDto(id=$id, userId=$userId, extraversionType=$extraversionType, spontaneityType=$spontaneityType, aggressivenessType=$aggressivenessType, rigidityType=$rigidityType, " +
                "introversionType=$introversionType, sensitivityType=$sensitivityType, anxietyType=$anxietyType, labilityType=$labilityType, lieType=$lieType, " +
                "aggravationType=$aggravationType, socialSupportSearchType=$socialSupportSearchType, antocipitaryAvoidanceType=$antocipitaryAvoidanceType, stressfulSituationAvoidType=$stressfulSituationAvoidType, socialIsolationType=$socialIsolationType," +
                "brokenRecordType=$brokenRecordType, helplessnesType=$helplessnesType, selfPityType=$selfPityType, selfAccusationType=$selfAccusationType, agressionType=$agressionType, takinMedType=$takinMedType, finishedAt='$finishedAt')"
    }
}

