package com.yjpapp.stockportfolio.network

class ServerRespCode {
    companion object{
        // 성공
        const val OK = 200 // 요청 성공
        const val Created = 201 //리소스 생성 성공
        const val Accepted = 202 // 비동기 처리 시작 성공
        const val NoContent = 204 // 응답 Body에 의도적으로 아무것도 포함하지 않음

        // 기타
        const val MovedPermanently = 301 // 리소스 이동함
        const val SeeOther = 303 // 다른 URI 참조 알림
        const val NotModified = 304 // 기존 데이터 사용
        const val TemporaryRedirect = 307 // 다른 URI로 다시 요청

        // 실패
        const val BadRequest = 400 // 일반적인 요청 실패
        const val Unauthorized = 401 // 인증 실패

        //인증 실패
        const val Forbidden = 403 // 인증 상태에 관계없이 엑세스 금지
        const val NotFound = 404 // 요청 URI에 해당하는 리소스 없음
        const val MethodNotAllowd = 405 // 지원되지 않는 HTTP 메서드
        const val NotAcceptable = 406 // 요청한 리소스 미디어 타입 제공 불가
        const val Conflict = 409 // 리소스 상태 위반
        const val PreconditionFailed = 412 // 조건부 연산 지원 불가
        const val UnsupportedMediaType = 415 // 요청의 Payload에 있는 미디어 타입 처리 불가
        const val InternalServerError = 500 // 내부 서버 오류
    }

}