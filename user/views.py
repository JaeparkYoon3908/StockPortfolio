# user/views.py

from django.db import connection
from rest_framework import permissions
from rest_framework.response import Response
from rest_framework.views import APIView
import requests

from .models import UserInfo


class RegistUser(APIView):
    permission_classes = [permissions.AllowAny]
    authentication_classes = []
    def post(self, request):
        user_email = request.data.get('user_email', "") # 클라이언트에서 올리는 user_id
        user_name = request.data.get('user_name', "") # 클라이언트에서 올리는 user_pw
        login_type = request.data.get('login_type', "") # 클라이언트에서 올리는 login_type


        if UserInfo.objects.filter(user_email=user_email).exists() and UserInfo.objects.filter(login_type=login_type).exists():
            data = dict(
                status=600,
                msg="이미 회원가입 되어있는 아이디입니다."
            )
            return Response(data)

        UserInfo.objects.create(user_email=user_email, user_name=user_name, login_type=login_type) # LoginUser 모델에 새로운 object 생성

        response = requests.post(
            "http://112.147.50.202/api/token/",
            json={"username": "yunjaepark", "password": "fnclrkakql3#"}
        )
        token = response.json().get("token")
        try:
            cursor = connection.cursor()

            query = "SELECT user_index FROM user_info WHERE user_email='"+user_email+"' && user_name='"+user_name+"' && login_type='"+login_type+"'"
            cursor.execute(query)
            user_info = cursor.fetchall()

            data = dict(
                status=200,
                data=dict(
                    user_index=user_info[0][0],
                    user_email=user_email,
                    user_name=user_name,
                    login_type=login_type
                )
            )
            connection.commit()
            connection.close()

        except:
            connection.rollback()
            data = dict(
                status=610,
                msg="user_index를 찾을 수 없습니다."
            )

        return Response(data=data)
class SelectUser(APIView):
    permission_classes = [permissions.AllowAny]
    authentication_classes = []
    def get(self, request):
        user_index = request.GET.get('user_index', "")
        response = requests.post(
            "http://112.147.50.202/api/token/",
            json={"username": "yunjaepark", "password": "fnclrkakql3#"}
        )
        token = "jwt " + response.json().get("token")

        try:
            cursor = connection.cursor()

            query = "SELECT * FROM user_info WHERE user_index="+user_index
            cursor.execute(query)
            user_info = cursor.fetchall()

            data = dict(
                status=200,
                data=dict(
                    user_index=user_info[0][0],
                    user_email=user_info[0][1],
                    user_name=user_info[0][2],
                    login_type=user_info[0][3],
                    token=token,
                )
            )
            connection.commit()
            connection.close()

        except:
            connection.rollback()
            data = dict(
                status=610,
                msg="user_index를 찾을 수 없습니다."
            )

        return Response(data=data)
