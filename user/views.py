# user/views.py

from django.db import connection
from rest_framework import permissions
from rest_framework.response import Response
from rest_framework.views import APIView
import requests

from .models import UserInfo
from response_set.response import CustomResponse


class RegistUser(APIView):
    permission_classes = [permissions.AllowAny]
    authentication_classes = []
    def post(self, request):
        user_email = request.data.get('user_email', "") # 클라이언트에서 올리는 user_id
        user_name = request.data.get('user_name', "") # 클라이언트에서 올리는 user_pw
        login_type = request.data.get('login_type', "") # 클라이언트에서 올리는 login_type

        response = requests.post(
            "http://112.147.50.241/api/token/",
            # "http://127.0.0.1:8000/api/token/",
            json={"username": "yunjaepark", "password": "fnclrkakql3#"}
        )
        token = "jwt " + response.json().get("token")

        if UserInfo.objects.filter(user_email=user_email, login_type=login_type).exists() :
            try:
                cursor = connection.cursor()
                query = "SELECT user_index FROM user_info WHERE user_email='" + user_email + "' && user_name='" + user_name + "' && login_type='" + login_type + "'"
                cursor.execute(query)
                user_info = cursor.fetchall()
                connection.commit()
                connection.close()
                data = dict(
                    status=200,
                    data=dict(
                        user_index=user_info[0][0],
                        user_email=user_email,
                        user_name=user_name,
                        login_type=login_type,
                        token=token
                    )
                )
            except:
                connection.rollback()
                data = CustomResponse.exception

            return Response(data)

        UserInfo.objects.create(user_email=user_email, user_name=user_name, login_type=login_type) # LoginUser 모델에 새로운 object 생성
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
                    login_type=login_type,
                    token=token
                )
            )
            connection.commit()
            connection.close()

        except:
            connection.rollback()
            data = CustomResponse.exception

        return Response(data=data)
class SelectUser(APIView):
    permission_classes = [permissions.AllowAny]
    authentication_classes = []
    def get(self, request):
        user_index = request.GET.get('user_index', "")
        response = requests.post(
            "http://112.147.50.241/api/token/",
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
            data = CustomResponse.exception

        return Response(data=data)