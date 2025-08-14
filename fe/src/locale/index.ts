import { createI18n } from 'vue-i18n'
import en from './en-US'
import vi from './vi-VN' // Thêm file tiếng Việt

export const LOCALE_OPTIONS = [
  { label: 'Tiếng Việt', value: 'vi-VN' },
  { label: 'English', value: 'en-US' },
]

const defaultLocale = localStorage.getItem('arco-locale') || 'vi-VN'

const i18n = createI18n({
  locale: defaultLocale,
  fallbackLocale: 'en-US',
  legacy: false,
  allowComposition: true,
  messages: {
    'en-US': en,
    'vi-VN': vi, // Thêm bản dịch tiếng Việt
  },
})

export default i18n
