import { mergeConfig } from 'vite'
import eslint from 'vite-plugin-eslint'
import baseConfig from './vite.config.base'

export default mergeConfig(
  {
    mode: 'development',
    server: {
      port: 6788,
      open: true,
      host: '0.0.0.0',
      fs: {
        strict: true,
      },
    },
    plugins: [
      eslint({
        cache: true,
        include: ['src/**/*.ts', 'src/**/*.tsx', 'src/**/*.vue'],
        exclude: ['node_modules'],
      }),
    ],
  },
  baseConfig
)
