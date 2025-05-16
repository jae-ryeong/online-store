## React 앱 빌드
#FROM node:22.13.1-alpine as builder
#
#WORKDIR /app
#
#COPY package*.json ./
#RUN npm install


# Nginx로 정적 파일 서빙
FROM nginx:stable-alpine

# 빌드된 파일 복사 (로컬에서 옮긴 build 폴더 기준)
COPY build /usr/share/nginx/html

# Nginx 설정 복사
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]