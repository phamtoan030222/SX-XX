export interface PaginationParams {
  page?: number
  size?: number
  orderBy?: string
  sortBy?: string
  q?: string
}

export interface DefaultResponse<T> {
  data: T
  message: string
  status: string
  success: boolean
  timestamp?: string
}

export interface PaginationResponse<T> {
  data: T[] // mảng dữ liệu
  totalPages: number
  currentPage: number
  totalElements: number
}

export type ResponseList = {
  id?: string
  orderNumber?: number
}
