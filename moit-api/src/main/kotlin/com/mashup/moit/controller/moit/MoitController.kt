package com.mashup.moit.controller.moit

import com.mashup.moit.common.MoitApiResponse
import com.mashup.moit.controller.moit.dto.MoitCreateRequest
import com.mashup.moit.controller.moit.dto.MoitDetailsResponse
import com.mashup.moit.controller.moit.dto.MoitJoinRequest
import com.mashup.moit.controller.moit.dto.MoitJoinResponse
import com.mashup.moit.controller.moit.dto.MoitJoinUserListResponse
import com.mashup.moit.controller.moit.dto.MoitStudyListResponse
import com.mashup.moit.controller.moit.dto.MyMoitListResponse
import com.mashup.moit.facade.MoitFacade
import com.mashup.moit.security.authentication.UserInfo
import com.mashup.moit.security.resolver.GetAuth
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Tag(name = "Moit", description = "Moit 관련 Api 입니다.")
@RequestMapping("/api/v1/moit")
@RestController
class MoitController(
    private val moitFacade: MoitFacade
) {
    @Operation(summary = "Moit create API", description = "moit 생성")
    @PostMapping
    fun createMoit(
        @GetAuth userInfo: UserInfo,
        @Valid @RequestBody request: MoitCreateRequest,
    ): MoitApiResponse<Long> {
        return MoitApiResponse.success(moitFacade.create(userInfo.id, request))
    }

    @Operation(summary = "Moit join API", description = "moit 가입 요청")
    @PostMapping("/join")
    fun joinMoit(
        @GetAuth userInfo: UserInfo,
        @RequestBody @Valid request: MoitJoinRequest,
    ): MoitApiResponse<MoitJoinResponse> {
        return MoitApiResponse.success(moitFacade.joinAsMember(userInfo.id, request.invitationCode))
    }

    @Operation(summary = "Moit Details API", description = "moit 상세 조회")
    @GetMapping("/{moitId}")
    fun getDetails(@PathVariable moitId: Long): MoitApiResponse<MoitDetailsResponse> {
        return MoitApiResponse.success(moitFacade.getMoitDetails(moitId))
    }

    @Operation(summary = "My Moit List API", description = "내 Moit List 조회")
    @GetMapping
    fun getMyMoits(
        @GetAuth userInfo: UserInfo,
    ): MoitApiResponse<MyMoitListResponse> {
        return MoitApiResponse.success(moitFacade.getMoitsByUserId(userInfo.id))
    }

    @Operation(summary = "Moit Join User List API", description = "Moit 가입 유저 조회")
    @GetMapping("/{moitId}/users")
    fun getMyMoits(
        @PathVariable moitId: Long,
    ): MoitApiResponse<MoitJoinUserListResponse> {
        return MoitApiResponse.success(moitFacade.getUsersByMoitId(moitId))
    }

    @Operation(summary = "All attendances of all studies in Moit API", description = "Moit의 모든 스터디 출결 조회")
    @GetMapping("/{moitId}/attendance")
    fun getAllAttendances(@PathVariable moitId: Long): MoitApiResponse<MoitStudyListResponse> {
        return MoitApiResponse.success(moitFacade.getAllAttendances(moitId))
    }

    @Operation(summary = "Moit 이미지 업로드 (add/update)", description = "Moit 대표 이미지 업로드 (추가/수정 시, 사용)")
    @PostMapping(
        "/{moitId}/image",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadMoitImage(
        @PathVariable("moitId") moitId: Long,
        @RequestParam("moitImage") moitImage: MultipartFile
    ): MoitApiResponse<Unit> {
        moitFacade.addMoitImage(moitId, moitImage)
        return MoitApiResponse.success()
    }

}
