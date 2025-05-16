## React 앱 빌드
#FROM node:22.13.1-alpine as builder
#
#WORKDIR /app
#
#COPY package*.json ./
#RUN npm install


FROM nginx:stable-alpine

# 기본 Nginx 설정 파일 제거 (우리가 직접 작성한 설정 파일을 사용하기 위함)
RUN rm /etc/nginx/conf.d/default.conf

# 사용자 정의 Nginx 설정 파일 복사
# Dockerfile이 있는 디렉토리(my-react-app)의 nginx/nginx.conf 경로를 참조합니다.
COPY nginx/nginx.conf /etc/nginx/conf.d/

# 로컬에서 빌드된 React 앱의 'build' 결과물 복사
# Dockerfile이 있는 디렉토리(my-react-app)의 build 폴더를 참조합니다.
COPY build /usr/share/nginx/html

# Nginx가 수신 대기할 포트 명시
EXPOSE 80

# Nginx 시작 명령
CMD ["nginx", "-g", "daemon off;"]