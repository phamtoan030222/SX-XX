// src/api/discount/discountApi.ts
import { API_ADMIN_DISCOUNT } from '@/constants/url';
import { DefaultResponse, PaginationParams } from '@/types/api.common';
import request from '../request';

// Giao diện cho các tham số API
export interface ParamsGetDiscount extends PaginationParams {
  q?: string | '';
  discountName?: string;
  discountStatus?: number;
}

// Giao diện cho cấu trúc dữ liệu trả về của một đợt giảm giá
export interface DiscountResponse {
  id: string;
  discountName: string;
  discountCode: string;
  createdDate: number;
  startTime: number;
  endTime: number;
  percentage: number;
  description: string;
}

// Giao diện cho yêu cầu tạo mới
export interface CreateDiscountRequest {
  discountName: string;
  discountCode: string;
  percentage: number;
  startDate: number;
  endDate: number;
  description?: string;
}

// Giao diện cho yêu cầu cập nhật
export interface UpdateDiscountRequest {
  id: string;
  discountName: string;
  discountCode: string;
  percentage: number;
  startDate: number;
  endDate: number;
  description?: string;
}

// Lấy danh sách tất cả các đợt giảm giá
export const getAllDiscounts = async (params: ParamsGetDiscount) => {
  // Chuẩn bị query params
  const queryParams = {
    ...params,
    // Gán trường q vào discountName để tìm kiếm
    discountName: params.q,
  };

  try {
    const res = await request.get<DefaultResponse<any>>(API_ADMIN_DISCOUNT, {
      params: queryParams,
    });

    // Sửa lỗi: Lấy dữ liệu từ response một cách chính xác
    // Axios trả về dữ liệu trong res.data
    // DefaultResponse chứa trường 'data', và trong trường 'data' của bạn
    // lại chứa các trường khác như 'data', 'totalPages', 'totalElements'...
    const responseData = res.data.data;

    return {
      items: responseData.data || [],
      totalItems: responseData.totalElements || 0,
      totalPages: responseData.totalPages || 0,
      currentPage: responseData.currentPage || 1,
    };
  } catch (error) {
    console.error("Error fetching discounts:", error);
    // Trả về dữ liệu trống trong trường hợp lỗi để tránh crash
    return {
      items: [],
      totalItems: 0,
      totalPages: 0,
      currentPage: 1,
    };
  }
};

// Hàm tạo mới một đợt giảm giá
export const createDiscount = async (data: CreateDiscountRequest) => {
  const res = await request.post(`${API_ADMIN_DISCOUNT}/addDiscount`, data);
  return res.data;
};

// Hàm cập nhật một đợt giảm giá
export const updateDiscount = async (data: UpdateDiscountRequest) => {
  const res = await request.put(`${API_ADMIN_DISCOUNT}/updateDiscount/${data.id}`, data);
  return res.data;
};

// Hàm hủy kích hoạt một đợt giảm giá (ví dụ)
export const deactivateDiscount = async (id: string) => {
  const res = await request.put(`${API_ADMIN_DISCOUNT}/deactivate/${id}`);
  return res.data;
};

// Hàm xóa một đợt giảm giá (ví dụ)
export const deleteDiscount = async (id: string) => {
  const res = await request.delete(`${API_ADMIN_DISCOUNT}/delete/${id}`);
  return res.data;
};