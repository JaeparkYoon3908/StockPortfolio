class CustomResponse(object):
    ok = dict(
        status=200,
        msg="성공"
    )
    no_index = dict(
        status=600,
        msg="index 값이 유효하지 않습니다."
    )
