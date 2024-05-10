class CustomResponse(object):
    ok = dict(
        status=200,
        msg="성공"
    )
    exception = dict(
        status=600,
        msg="일시적 오류가 발생했습니다."
    )
    no_index = dict(
        status=601,
        msg="index 값이 유효하지 않습니다."
    )
