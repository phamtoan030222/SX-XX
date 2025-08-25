package com.sd20201.datn.utils;

import com.sd20201.datn.core.admin.vouchers.voucherDetail.model.request.AdVoucherDetailRequest;
import com.sd20201.datn.core.common.base.PageableRequest;
import com.sd20201.datn.core.common.base.ResponseObject;
import com.sd20201.datn.infrastructure.constant.PaginationConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;


import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Helper {

    public static String appendWildcard(String url) {
        return url + "/**";
    }


    public static Pageable createPageable(PageableRequest request, String defaultSortBy, String defaultOrderBy) {
        // Đảm bảo chỉ số trang luôn lớn hơn 0 trước khi trừ 1
        int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
        int size = request.getSize() == 0 ? PaginationConstant.DEFAULT_SIZE : request.getSize();
        Sort.Direction direction = request.getOrderBy() == null || request.getOrderBy().isEmpty()
                ? Sort.Direction.fromString(defaultOrderBy)
                : Sort.Direction.fromString(request.getOrderBy());
        String sortBy = request.getSortBy();
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = defaultSortBy;
        }
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    public static Pageable createPageable(int page, int size) {
        // Đảm bảo chỉ số trang (page) luôn lớn hơn hoặc bằng 0
        // Nếu frontend gửi 1, chúng ta sẽ chuyển nó thành 0
        // Nếu frontend gửi 0, chúng ta cũng sẽ coi đó là 0 để tránh lỗi
        int adjustedPage = Math.max(0, page - 1);

        return PageRequest.of(adjustedPage, size);
    }

    public static Pageable createPageable(PageableRequest request) {
        return createPageable(request, "createdDate", "DESC");
    }


    public static ResponseEntity<?> createResponseEntity(ResponseObject<?> responseObject) {
        return new ResponseEntity<>(responseObject, responseObject.getStatus());
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^0[0-9]{9,10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return !matcher.matches();
    }

    public static String replaceManySpaceToOneSpace(String name) {
        // Thay thế tất cả khoảng trắng liên tiếp bằng dấu gạch dưới
        return name.replaceAll("\\s+", " ");
    }

    public static String replaceSpaceToEmpty(String name) {
        // Thay thế tất cả khoảng trắng liên tiếp bằng dấu gạch dưới
        return name.replaceAll("\\s+", "");
    }

    private static final Map<Character, Character> SPECIAL_CHAR_MAP = new HashMap<>();

    static {
        SPECIAL_CHAR_MAP.put('đ', 'd');
        SPECIAL_CHAR_MAP.put('Đ', 'D');
        SPECIAL_CHAR_MAP.put('ơ', 'o');
        SPECIAL_CHAR_MAP.put('Ơ', 'O');
        SPECIAL_CHAR_MAP.put('ớ', 'o');
        SPECIAL_CHAR_MAP.put('ờ', 'o');
        SPECIAL_CHAR_MAP.put('ở', 'o');
        SPECIAL_CHAR_MAP.put('ỡ', 'o');
        SPECIAL_CHAR_MAP.put('ợ', 'o');
        SPECIAL_CHAR_MAP.put('ố', 'o');
        SPECIAL_CHAR_MAP.put('ồ', 'o');
        SPECIAL_CHAR_MAP.put('ổ', 'o');
        SPECIAL_CHAR_MAP.put('ỗ', 'o');
        SPECIAL_CHAR_MAP.put('ộ', 'o');
        SPECIAL_CHAR_MAP.put('ớ', 'o');
        SPECIAL_CHAR_MAP.put('ờ', 'o');
        SPECIAL_CHAR_MAP.put('ở', 'o');
        SPECIAL_CHAR_MAP.put('ỡ', 'o');
        SPECIAL_CHAR_MAP.put('ợ', 'o');
        SPECIAL_CHAR_MAP.put('ă', 'a');
        SPECIAL_CHAR_MAP.put('ắ', 'a');
        SPECIAL_CHAR_MAP.put('ằ', 'a');
        SPECIAL_CHAR_MAP.put('ẵ', 'a');
        SPECIAL_CHAR_MAP.put('ặ', 'a');
        SPECIAL_CHAR_MAP.put('â', 'a');
        SPECIAL_CHAR_MAP.put('ấ', 'a');
        SPECIAL_CHAR_MAP.put('ầ', 'a');
        SPECIAL_CHAR_MAP.put('ẩ', 'a');
        SPECIAL_CHAR_MAP.put('ẫ', 'a');
        SPECIAL_CHAR_MAP.put('ậ', 'a');
        SPECIAL_CHAR_MAP.put('ư', 'u');
        SPECIAL_CHAR_MAP.put('ứ', 'u');
        SPECIAL_CHAR_MAP.put('ừ', 'u');
        SPECIAL_CHAR_MAP.put('ử', 'u');
        SPECIAL_CHAR_MAP.put('ữ', 'u');
        SPECIAL_CHAR_MAP.put('ự', 'u');
        // Thêm các ký tự khác nếu cần
    }

    public static String generateCodeFromName(String name) {
        // Chuyển role name chuỗi thành chữ hoa
        String upperCaseString = name.toUpperCase();

        // Thay thế các ký tự đặc biệt
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : upperCaseString.toCharArray()) {
            if (SPECIAL_CHAR_MAP.containsKey(c)) {
                stringBuilder.append(SPECIAL_CHAR_MAP.get(c));
            } else {
                stringBuilder.append(c);
            }
        }
        String replacedString = stringBuilder.toString();

        // Loại bỏ dấu
        String normalizedString = Normalizer.normalize(replacedString, Normalizer.Form.NFD);
        String withoutAccentString = normalizedString.replaceAll("\\p{M}", "");

        // Thay thế tất cả khoảng trắng liên tiếp bằng dấu gạch dưới
        return withoutAccentString.replaceAll("\\s+", "_");
    }

    public static String generateCodeProductDetail() {
        return "PD" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

}
