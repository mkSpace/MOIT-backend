package com.mashup.moit.fine.controller.dto

import com.mashup.moit.domain.attendance.AttendanceStatus
import com.mashup.moit.domain.fine.Fine
import com.mashup.moit.domain.study.Study
import com.mashup.moit.domain.user.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import java.time.LocalDate


@Schema(description = "벌금 송금 평가 RequestBody")
data class FineEvaluateRequest(
    @Schema(description = "벌금 송금 승인 여부")
    @field:NotNull
    val confirm: Boolean,
)

@Schema(description = "벌금 리스트 조회 Response")
data class FineListResponse(
    @Schema(description = "벌금 총액")
    val totalFineAmount: Long,
    @Schema(description = "벌금 미납 리스트")
    val fineNotYet: List<FineResponseForListView>,
    @Schema(description = "벌금 납부 리스트")
    val fineComplete: List<FineResponseForListView>,
) {
    companion object {
        fun emptyFineList() = FineListResponse(0, emptyList(), emptyList())
    }
}

@Schema(description = "Fine 정보 - Fine List 조회에 사용")
data class FineResponseForListView(
    @Schema(description = "Fine id")
    val id: Long,
    @Schema(description = "Fine 금액")
    val fineAmount: Long,
    @Schema(description = "Fine 대상 User id")
    val userId: Long,
    @Schema(description = "Fine 대상 User nickname")
    val userNickname: String,
    @Schema(description = "Fine 대상 출석 상태 (LATE, ABSENCE)")
    val attendanceStatus: AttendanceStatus,
    @Schema(description = "Fine 대상 스터디 회차")
    val studyOrder: Int,
    @Schema(description = "Fine 납부 인증 유무")
    val isApproved: Boolean,
    @Schema(description = "Fine 등록 일자 YYYY-mm-dd")
    val registerAt: LocalDate,
    @Schema(description = "Fine 납부 일자 YYYY-mm-dd")
    val approveAt: LocalDate?,
) {
    companion object {
        fun of(fine: Fine, user: User, study: Study) =
            FineResponseForListView(
                id = fine.id,
                fineAmount = fine.amount,
                userId = fine.userId,
                userNickname = user.nickname,
                attendanceStatus = fine.attendanceStatus,
                studyOrder = study.order,
                isApproved = fine.isApproved,
                registerAt = fine.registerAt.toLocalDate(),
                approveAt = fine.approvedAt?.toLocalDate()
            )
    }
}
