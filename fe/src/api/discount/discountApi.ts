// src/api/discount/discountApi.ts
import { API_ADMIN_DISCOUNT } from '@/constants/url'
import { DefaultResponse, PaginationParams } from '@/types/api.common'
import request from '../request'


export interface ParamsGetDiscount extends PaginationParams {
  q?: string | ''
  discountName?: string
  // discountCode?:string
  discountStatus?: number
}


export interface DiscountResponse {
  id: string
  discountName: string
  discountCode: string
  createdDate: number
  startTime: number
  endTime: number
  percentage: number
  description: string
}


export interface CreateDiscountRequest {
  discountName: string
  discountCode: string
  percentage: number
  startDate: number
  endDate: number
  description?: string
}

export interface UpdateDiscountRequest {
  discountName: string
  discountCode: string
  percentage: number
  startDate: number
  endDate: number
  description?: string
}

export const getAllDiscounts = async (params: ParamsGetDiscount) => {
 const queryParams: any = {
   page: Math.max(1, params.page || 1), 
   size: params.size || 10,
 }

 if (params.q && params.q.trim()) {
   queryParams.discountName = params.q.trim()
 } else if (params.discountName && params.discountName.trim()) {
   queryParams.discountName = params.discountName.trim()
 }

 if (params.discountStatus !== undefined && params.discountStatus !== null) {
   queryParams.discountStatus = params.discountStatus
 }

 try {
   const res = await request.get<DefaultResponse<any>>(API_ADMIN_DISCOUNT, {
     params: queryParams,
   })


   const responseData = res.data.data

   return {
     items: responseData.data || [],
     totalItems: responseData.totalElements || 0,
     totalPages: responseData.totalPages || 0,
     currentPage: responseData.currentPage || 1,
   }
 } catch (error) {
   console.error("Error fetching discounts:", error);
   return {
     items: [],
     totalItems: 0,
     totalPages: 0,
     currentPage: 1,
   }
 }
}


export const createDiscount = async (data: CreateDiscountRequest) => {
  const res = await request.post(`${API_ADMIN_DISCOUNT}/addDiscount`, data)
  return res.data
}


export const updateDiscount = async (id: string, data: UpdateDiscountRequest) => {
  const res = await request.put(`${API_ADMIN_DISCOUNT}/updateDiscount/${id}`, data)
  return res.data
}


export const deactivateDiscount = async (id: string) => {
  const res = await request.put(`${API_ADMIN_DISCOUNT}/end/${id}`)
  return res.data
}

export const startDiscount = async (id: string) => {
  const res = await request.put(`${API_ADMIN_DISCOUNT}/start/${id}`)
  return res.data
}




export const deleteDiscount = async (id: string) => {
  const res = await request.delete(`${API_ADMIN_DISCOUNT}/delete/${id}`)
  return res.data
}
