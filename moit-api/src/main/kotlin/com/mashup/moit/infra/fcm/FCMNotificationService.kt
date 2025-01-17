package com.mashup.moit.infra.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.mashup.moit.domain.moit.NotificationRemindOption
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FCMNotificationService(
    private val firebaseMessaging: FirebaseMessaging
) {
    private val logger = LoggerFactory.getLogger(FCMNotificationService::class.java)

    fun pushStartStudyNotification(startNotification: StudyAttendanceStartNotification) {
        val topic = getMoitTopic(startNotification.moitId)
        try {
            val notification = Notification.builder()
                .setTitle(startNotification.title)
                .setBody(startNotification.body)
                .build()

            val message = Message.builder()
                .setTopic(topic)
                .setNotification(notification)
                .build()

            val response = firebaseMessaging.send(message)
            logger.info("success to send notification : {}", response)
        } catch (e: Exception) {
            logger.error("Fail to send start Attendance Noti. topic-id : [{}], error: [{}]", topic, e.toString())
        }
    }

    fun getMoitTopic(moitId: Long): String {
        return TOPIC_MOIT_PREFIX + moitId
    }

    fun sendTopicSampleNotification(request: SampleNotificationRequest) {
        val title = STUDY_REMINDER_NOTIFICATION
        val body = when (request.remainMinutes) {
            NotificationRemindOption.STUDY_DAY_10_AM -> "오늘은 ${request.studyName} 스터디가 있는 날이에요"
            else -> "${request.studyName} 시작, ${request.remainMinutes.mean} 전입니다"
        }

        val topic = getStudyTopic(request.studyId)

        try {
            val notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

            // data 전송이 함께 필요하다면, putData 로 함께 publish
            val msg = Message.builder()
                .setTopic(topic)
                .setNotification(notification)
                .build()

            val response = firebaseMessaging.send(msg)
            logger.info("success to send notification : {}", response)
        } catch (e: Exception) {
            logger.error("Fail to send Message. topic-id : {}, title: {}, : [{}]", topic, title, e.toString())
        }
    }

    fun getStudyTopic(studyId: Long): String {
        return "study-$studyId"
    }

    companion object {
        private const val STUDY_REMINDER_NOTIFICATION = "Study Remind Notification"
        private const val TOPIC_MOIT_PREFIX = "MOIT-"
    }
}
