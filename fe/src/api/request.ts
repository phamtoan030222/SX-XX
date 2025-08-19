import { VITE_BASE_URL_SERVER } from '@/constant/url'
import axios from 'axios'

const request = axios.create({
  baseURL: VITE_BASE_URL_SERVER,
})

export default request
