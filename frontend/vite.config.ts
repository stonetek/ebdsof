import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    host: true,        // escuta em todas as interfaces (0.0.0.0)
    port: 5173,
    strictPort: false,
    hmr: {
      host: '192.168.100.2' // opcional: força HMR a conectar via este IP
    }
  }
})
