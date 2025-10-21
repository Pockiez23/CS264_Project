FROM nginx:1.27-alpine
WORKDIR /usr/share/nginx/html
# ไฟล์เว็บ จะ bind-mount ผ่าน compose
