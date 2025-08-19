export const { VITE_BASE_URL_SERVER } = import.meta.env || {}

export const { VITE_BASE_URL_CLIENT } = import.meta.env || {}

// DOMAIN
export const DOMAIN_BACKEND = `${VITE_BASE_URL_SERVER}` as string

export const DOMAIN_FRONTEND = `${VITE_BASE_URL_CLIENT}` as string

export const URL_FRONTEND = `${DOMAIN_FRONTEND}/redirect`

//SUB_REDIRECT
export const SCREEN_ROLE_ADMIN = `&screen=ADMIN`

export const URL_OAUTH2_GOOGLE_ADMIN = () =>
  `${DOMAIN_BACKEND}/oauth2/authorize/google?redirect_uri=${URL_FRONTEND}${SCREEN_ROLE_ADMIN}` as string

export const API_URL = `${VITE_BASE_URL_SERVER}/api/v1` as string

// AUTH API
export const PREFIX_API_AUTH = `${API_URL}/auth` as string

export const PREFIX_API_PERMITALL = `${API_URL}/permitall` as string
// ADMIN API
export const PREFIX_API_ADMIN = `${API_URL}/admin` as string
