package com.mashup.moit.controller.fine

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.controller.fine.dto.FineEvaluateRequest
import com.mashup.moit.controller.fine.dto.FineListResponse
import com.mashup.moit.controller.fine.dto.FineResponse
import com.mashup.moit.facade.FineFacade
import com.mashup.moit.security.authentication.UserInfo
import com.mashup.moit.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Fine", description = "벌금 인증 관련 Api 입니다.")
@RequestMapping("/api/v1/moit/{moitId}/fine")
@RestController
class FineController(
    private val fineFacade: FineFacade,
) {

    @Operation(summary = "벌금 인증 요청 API", description = "벌금 송금 인증을 요청하는 API - Image 업로드가 필요")
    @PostMapping(
        "/{fineId}",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun certificationFine(
        @GetAuth userInfo: UserInfo,
        @PathVariable("moitId") moitId: Long,
        @PathVariable("fineId") fineId: Long,
        @RequestParam("finePaymentImage") finePaymentImage: MultipartFile
    ): MoitApiResponse<FineResponse> {
        return MoitApiResponse.success(
            fineFacade.addFineCertification(userInfo.id, userInfo.nickname, fineId, finePaymentImage)
        )
    }

    @Operation(summary = "벌금 인증 평가 API", description = "벌금 송금 인증을 평가하는 API - 스터디장만 가능")
    @PostMapping("/{fineId}/evaluate")
    fun evaluateFine(
        @GetAuth userInfo: UserInfo,
        @PathVariable("moitId") moitId: Long,
        @PathVariable("fineId") fineId: Long,
        @RequestBody fineEvaluateRequest: FineEvaluateRequest
    ): MoitApiResponse<Unit> {
        fineFacade.evaluateFine(userInfo.id, moitId, fineId, fineEvaluateRequest.confirm)
        return MoitApiResponse.success()
    }

    @Operation(summary = "벌금 리스트 조회 API", description = "벌금 리스트 조회 API")
    @GetMapping
    fun fineList(@PathVariable("moitId") moitId: Long): MoitApiResponse<FineListResponse> {
        return MoitApiResponse.success(fineFacade.getFineList(moitId))
    }
}
