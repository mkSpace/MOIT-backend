package com.mashup.moit.sample.controller.sample.dto

import com.mashup.moit.domain.moit.NotificationRemindOption
import com.mashup.moit.domain.sample.Sample
import java.time.LocalDateTime

data class SampleCreateRequest(
    val name: String,
)

data class SampleResponse(
    val id: Long,
    val name: String,
) {
    companion object {
        fun of(sample: Sample) = SampleResponse(
            id = sample.id,
            name = sample.name,
        )
    }
}

data class SampleNotificationRequest(
    val targetId: Long,
    val targetName: String,
    val remainMinutes: NotificationRemindOption,
)
