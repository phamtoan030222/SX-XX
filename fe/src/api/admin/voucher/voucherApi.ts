// src/api/admin/voucher/voucherApi.ts
import axios, { AxiosResponse } from 'axios';

// Base URL cho API
const BASE_URL = '/api/v1/admin/vouchers/voucher';

// Types definitions
export interface AdVoucherRequest {
  page?: number;
  size?: number;
  voucherName?: string;
  voucherStatus?: number;
}

export interface VoucherValidateRequest {
  id?: string;
  voucherName: string;
  voucherCode: string;
  voucherType: number;
  decreaseUnit: number;
  increaseUnit: number;
  startTime: number;
  endTime: number;
  conditionOfUse: number;
}

export interface AdVoucherResponse {
  id: string;
  voucherName: string;
  voucherCode: string;
  createdDate: number;
  startTime: number;
  endTime: number;
  conditionOfUse: number;
  voucherDecreaseUnit: number;
  increaseUnit: number;
  typeVoucher: number;
  voucherStatus: number;
}

export interface PageableResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  numberOfElements: number;
  first: boolean;
  last: boolean;
}

export interface ResponseObject<T> {
  data: T;
  message: string;
  success: boolean;
  errorCode?: string;
  statusCode: number;
}

export interface VoucherApiResponse extends ResponseObject<PageableResponse<AdVoucherResponse>> {}

// API Service Class
export class VoucherApiService {
  
  /**
   * Lấy danh sách tất cả voucher với phân trang và filter
   */
  static async getAllVouchers(params: AdVoucherRequest): Promise<VoucherApiResponse> {
    try {
      const response: AxiosResponse<VoucherApiResponse> = await axios.get(BASE_URL, {
        params: {
          page: params.page || 0,
          size: params.size || 10,
          voucherName: params.voucherName || null,
          voucherStatus: params.voucherStatus !== undefined ? params.voucherStatus : null
        }
      });
      return response.data;
    } catch (error) {
      console.error('Error fetching vouchers:', error);
      throw error;
    }
  }

  /**
   * Thêm voucher mới
   */
  static async createVoucher(voucher: VoucherValidateRequest): Promise<ResponseObject<null>> {
    try {
      const response: AxiosResponse<ResponseObject<null>> = await axios.post(
        `${BASE_URL}/addVoucher`, 
        voucher,
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      return response.data;
    } catch (error: any) {
      console.error('Error creating voucher:', error);
      // Xử lý error response từ backend
      if (error.response && error.response.data) {
        throw error.response.data;
      }
      throw error;
    }
  }

  /**
   * Cập nhật voucher
   */
  static async updateVoucher(voucher: VoucherValidateRequest): Promise<ResponseObject<AdVoucherResponse>> {
    try {
      const response: AxiosResponse<ResponseObject<AdVoucherResponse>> = await axios.put(
        `${BASE_URL}/updateVoucher`, 
        voucher,
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      return response.data;
    } catch (error: any) {
      console.error('Error updating voucher:', error);
      if (error.response && error.response.data) {
        throw error.response.data;
      }
      throw error;
    }
  }

  /**
   * Deactivate voucher (kết thúc sớm)
   */
  static async deactivateVoucher(id: string): Promise<ResponseObject<AdVoucherResponse>> {
    try {
      const response: AxiosResponse<ResponseObject<AdVoucherResponse>> = await axios.patch(
        `${BASE_URL}/${id}/deactivate`
      );
      return response.data;
    } catch (error: any) {
      console.error('Error deactivating voucher:', error);
      if (error.response && error.response.data) {
        throw error.response.data;
      }
      throw error;
    }
  }

  /**
   * Xóa voucher
   */
  static async deleteVoucher(id: string): Promise<ResponseObject<null>> {
    try {
      const response: AxiosResponse<ResponseObject<null>> = await axios.delete(
        `${BASE_URL}/${id}/delete`
      );
      return response.data;
    } catch (error: any) {
      console.error('Error deleting voucher:', error);
      if (error.response && error.response.data) {
        throw error.response.data;
      }
      throw error;
    }
  }
}

// Utility functions cho voucher
export class VoucherUtils {
  
  /**
   * Format timestamp thành date string
   */
  static formatDate(timestamp: number): string {
    return new Date(timestamp).toLocaleDateString('vi-VN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  /**
   * Lấy tên trạng thái voucher
   */
  static getStatusText(status: number): string {
    switch (status) {
      case 0: return 'Hoạt động';
      case 1: return 'Không hoạt động';
      case 2: return 'Hết hạn';
      default: return 'Không xác định';
    }
  }

  /**
   * Lấy class CSS cho trạng thái
   */
  static getStatusClass(status: number): string {
    switch (status) {
      case 0: return 'bg-green-100 text-green-800';
      case 1: return 'bg-red-100 text-red-800';
      case 2: return 'bg-gray-100 text-gray-800';
      default: return 'bg-gray-100 text-gray-800';
    }
  }

  /**
   * Lấy tên loại voucher
   */
  static getVoucherTypeText(type: number): string {
    switch (type) {
      case 0: return 'Giảm theo %';
      case 1: return 'Giảm theo số tiền';
      default: return 'Không xác định';
    }
  }

  /**
   * Validate form voucher
   */
  static validateVoucherForm(voucher: VoucherValidateRequest): string[] {
    const errors: string[] = [];
    
    if (!voucher.voucherName || voucher.voucherName.trim() === '') {
      errors.push('Tên voucher không được để trống');
    }
    
    if (!voucher.voucherCode || voucher.voucherCode.trim() === '') {
      errors.push('Mã voucher không được để trống');
    }
    
    if (voucher.decreaseUnit <= 0) {
      errors.push('Giá trị giảm phải lớn hơn 0');
    }
    
    if (voucher.increaseUnit < 0) {
      errors.push('Giá trị tăng không được âm');
    }
    
    if (voucher.conditionOfUse < 0) {
      errors.push('Điều kiện sử dụng không được âm');
    }
    
    const now = Date.now();
    if (voucher.startTime <= now) {
      errors.push('Thời gian bắt đầu phải lớn hơn thời gian hiện tại');
    }
    
    if (voucher.startTime >= voucher.endTime) {
      errors.push('Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc');
    }
    
    return errors;
  }

  /**
   * Kiểm tra voucher có thể chỉnh sửa không
   */
  static canEditVoucher(voucher: AdVoucherResponse): boolean {
    const now = Date.now();
    return voucher.voucherStatus === 0 && voucher.startTime > now;
  }

  /**
   * Kiểm tra voucher có thể xóa không
   */
  static canDeleteVoucher(voucher: AdVoucherResponse): boolean {
    const now = Date.now();
    return voucher.startTime > now;
  }

  /**
   * Kiểm tra voucher có thể deactivate không
   */
  static canDeactivateVoucher(voucher: AdVoucherResponse): boolean {
    const now = Date.now();
    return voucher.voucherStatus === 0 && 
           voucher.startTime <= now && 
           voucher.endTime > now;
  }
}

// Export default cho convenience
export default VoucherApiService;

// Error codes từ backend
export const VOUCHER_ERROR_CODES = {
  VOUCHER_NAME_EXISTS: 'Tên voucher đã tồn tại',
  VOUCHER_CODE_EXISTS: 'Mã voucher đã tồn tại',
  VOUCHER_TIME_INVALID: 'Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc',
  VOUCHER_START_INVALID: 'Thời gian bắt đầu phải lớn hơn thời gian hiện tại',
  VOUCHER_NOT_FOUND: 'Voucher không tồn tại',
  VOUCHER_INACTIVE: 'Voucher không hoạt động',
  VOUCHER_EXPIRED: 'Voucher đã hết hạn',
  VOUCHER_RUNNING: 'Voucher đang diễn ra, không thể cập nhật',
  VOUCHER_ALREADY_INACTIVE: 'Voucher đã không hoạt động',
  VOUCHER_STARTED: 'Voucher đã bắt đầu, không thể xóa'
} as const;