export const { VITE_BASE_URL_SERVER } = import.meta.env || {}
export const { VITE_BASE_URL_CLIENT } = import.meta.env || {}

export const API_URL = `${VITE_BASE_URL_SERVER}/api/v1` as string

export const PREFIX_API_ADMIN = `${API_URL}/admin` as string

export const API_ADMIN_PRODUCTS = `${PREFIX_API_ADMIN}/products` as string
export const API_ADMIN_COLOR = `${API_ADMIN_PRODUCTS}/color` as string
export const API_ADMIN_RAM = `${API_ADMIN_PRODUCTS}/ram` as string
export const API_ADMIN_HARD_DRIVE = `${API_ADMIN_PRODUCTS}/harddrive` as string
export const API_ADMIN_MATERIAL = `${API_ADMIN_PRODUCTS}/material` as string
