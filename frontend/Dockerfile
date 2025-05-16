## React 앱 빌드
#FROM node:22.13.1-alpine as builder
#
#WORKDIR /app
#
#COPY package*.json ./
#RUN npm install


# Nginx로 정적 파일 서빙
FROM nginx:stable-alpine

# Nginx 설정 파일 제거 (optional)
RUN rm /etc/nginx/conf.d/default.conf

# 사용자 정의 설정 복사 (필요한 경우)
# COPY nginx.conf /etc/nginx/conf.d

# React 빌드 결과 복사
COPY --from=builder /build /usr/share/nginx/html
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]