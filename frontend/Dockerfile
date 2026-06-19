# ─────────────────────────────────────────────
# Stage 1: Build
# ─────────────────────────────────────────────
FROM node:20-alpine AS build
WORKDIR /app

# Copiar manifiestos de dependencias primero para aprovechar la caché de capas
COPY package*.json ./
RUN npm ci

# Copiar el resto del código fuente y compilar
COPY . .
RUN npm run build

# ─────────────────────────────────────────────
# Stage 2: Servidor de producción con Nginx
# ─────────────────────────────────────────────
FROM nginx:alpine

# Copiar el build estático generado por Vite
COPY --from=build /app/dist /usr/share/nginx/html

# Copiar configuración personalizada de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
