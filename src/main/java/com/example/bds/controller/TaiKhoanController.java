package com.example.bds.controller;

import com.example.bds.dto.admin.TaiKhoanResponseAdminDTO;
import com.example.bds.dto.rep.*;
import com.example.bds.dto.req.*;
import com.example.bds.exception.DataNotFoundException;
import com.example.bds.model.DanhSachYeuThich;
import com.example.bds.model.TaiKhoan;
import com.example.bds.repository.TaiKhoanRepository;
import com.example.bds.service.IAuthService;
import com.example.bds.service.IDanhSachYeuThichService;
import com.example.bds.service.IThanhToanService;
import com.example.bds.service.ITinHetHanService;
import com.example.bds.service.impl.TaiKhoanService;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class TaiKhoanController {

    @Value("${twilio.serviceSid}")
    private String serviceSid;
    private final TaiKhoanService taiKhoanService;
    private final TaiKhoanRepository taiKhoanRepository;
    private final IDanhSachYeuThichService danhSachYeuThichService;
    private final ITinHetHanService tinHetHanService;
    private final IThanhToanService thanhToanService;
    private final IAuthService authService;

    @GetMapping("/me")
    public ResponseEntity<?> getMe() {
        try {
            TaiKhoanResponse dto = taiKhoanService.findByCurrentLogin();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "user", dto,
                    "msg", "OK"
            ));
        } catch (DataNotFoundException e) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "user", null,
                    "msg", e.getMessage()
            ));
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody SendOtpRequest request) throws DataNotFoundException {
        String fullphone = "+84" + request.getPhone();

        Optional<TaiKhoan> taiKhoanExisting = taiKhoanService.finByPhoneNumber(request.getPhone());
        if (taiKhoanExisting.isPresent()) {
            return ResponseEntity.ok().body(new ApiResponse(false, "Số điện thoại đã được đăng ký"));
        }

       try {
           Verification.creator(serviceSid, fullphone, "sms").create();
           return ResponseEntity.ok(new ApiResponse(true, "Mã OTP đã gửi thành công."));
       } catch (Exception e) {
           return ResponseEntity.ok(new ApiResponse(false, "Mã OTP đã gửi không thành công."));
       }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest request) {
       String fullPhone = "+84" + request.getPhone();

       try {
           VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid, request.getCode())
                   .setTo(fullPhone)
                   .create();
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           TaiKhoan currentUser = (TaiKhoan) authentication.getPrincipal();
            if ("approved".equals(verificationCheck.getStatus())) {
                // Update số điện thoại cho người dùng
                int updated = taiKhoanService.updatePhoneAndVerified(currentUser.getId(), request.getPhone());
                boolean success = updated > 0;
                return ResponseEntity.ok(new ApiResponse(success, success ? "Xác minh điện thoại thành công." : "Xác minh điện thoại không thành công."));
            } else {
                return ResponseEntity.ok(new ApiResponse(false, "Xác minh điện thoại không thành công."));
            }
       } catch (Exception e) {
           return ResponseEntity.ok(new ApiResponse(false, "Mã OTP không hợp lệ mm."));
       }

   }

    @PutMapping("/upgrade-owner")
    public ResponseEntity<?> updateRoleOwner() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan currentUser = (TaiKhoan) authentication.getPrincipal();

        if (!currentUser.getDienThoaiXacThuc() || !currentUser.getEmailXacThuc() || currentUser.getSoDu() == 0) {
            return ResponseEntity.ok(new ApiResponse(false, "Bạn chưa đủ điều kiện nâng cấp thành chủ bất động sản."));
        }

        currentUser.setVaiTro(TaiKhoan.VaiTro.CHUBATDONGSAN);
        taiKhoanRepository.save(currentUser);

        return ResponseEntity.ok(new ApiResponse(true, "Nâng cấp thành công, bạn hãy đăng nhập lại để cập nhật nhé"));
    }

    @GetMapping("/wishlist")
    public ResponseEntity<?> getWishlist(@AuthenticationPrincipal TaiKhoan taiKhoan) {
        List<WishlistRepDto> wishlistRepDtos = danhSachYeuThichService.getAllWishlistByTaiKhoan(taiKhoan.getId());
        return ResponseEntity.ok(Map.of(
                "success", true,
                "wls", wishlistRepDtos
        ));
    }

    @PutMapping("/wishlist")
    public ResponseEntity<?> addWishlist(@RequestBody WishlistReqDto req,
                                         @AuthenticationPrincipal TaiKhoan taiKhoan) {
        String message = danhSachYeuThichService.toggleWishlist(req.getIdProperty(), taiKhoan.getId());
        return ResponseEntity.ok(new ApiResponse(true, message));
    }

    @GetMapping("/admin")
    public ResponseEntity<?>  getUserAdmin(@RequestParam(defaultValue = "5") int limit,
                                           @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "createdAt") String sort,
                                           @RequestParam(defaultValue = "DESC") String order,
                                           @RequestParam(required = false) String fullname) {
        TaiKhoanResponseAdminDTO taiKhoanResponses = taiKhoanService.getAllTaiKhoanByAdmin(limit, page, sort, order, fullname);
        return ResponseEntity.ok(taiKhoanResponses);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateRoleTaiKhoan(@RequestBody Map<String, String> req,
                                                @PathVariable("id") Integer id) {
        boolean success = taiKhoanService.updateRoleTaiKhoanByAdmin(id, req.get("role"));
        return ResponseEntity.ok().body(new ApiResponse(success, success ?  "Cập nhật vai trò thành viên thành công.":
                                                                            "Cập nhật vai trò thành viên không thành công."));
    }

    @DeleteMapping("/admin")
    public ResponseEntity<?> deleteTaiKhoan(@RequestParam("idUsers") Integer id) {
        String a = "aa";
        boolean success = taiKhoanService.deleteTaiKhoanByAdmin(id);
        return ResponseEntity.ok().body(new ApiResponse(success, success ? "Xóa thành viên thành công." :
                                                                            "Xóa thành viên không thành công."));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(@RequestParam(value = "days", required = false, defaultValue = "220") int days,
                                          @RequestParam(value = "type", required = false, defaultValue = "day") String type,
                                          @RequestParam(value = "from", required = false) String from,
                                          @RequestParam(value = "to", required = false) String to) {
        String a = "aa";
        DashboardData data = new DashboardData();
        data.setAnonymous(72);
        data.setRegisted(0);
        data.setPosts(List.of(
                new PostStatDTO("01/03/25", 19),
                new PostStatDTO("02/04/25", 2)
        ));
        data.setCreatedUser(21);
        data.setTotalIncomes(new BigDecimal("11000000"));
        data.setPricingReport(List.of(
                new PricingReportDTO(4L, 1),
                new PricingReportDTO(5L, 1)
        ));

        DashboardResponseDTO response = new DashboardResponseDTO();
        response.setSuccess(true);
        response.setData(data);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/expired-history")
    public ResponseEntity<?> getExpiredHistory(@RequestParam(defaultValue = "5") int limit,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "ngayTao") String sort,
                                               @RequestParam(defaultValue = "DESC") String order,
                                               @AuthenticationPrincipal TaiKhoan taiKhoan) {
        List<ExpiredHistory> expiredHistoryList = tinHetHanService.getExpiredHistory(limit, page, sort, order, taiKhoan);


        return ResponseEntity.ok(Map.of(
                "success", true,
                "payments", expiredHistoryList,
                "msg", new Pagination(5, 1, 2, 1)
        ));
    }
    @PutMapping("/buy-pricing")
    public ResponseEntity<?> buyPricings(@RequestBody Map<String, Integer> req,
                                         @AuthenticationPrincipal TaiKhoan taiKhoan) {
        String a = "aa";
        Integer idPricing = req.get("idPricing");
        Integer total = req.get("total");
        boolean success = taiKhoanService.buyGoiDichVu(taiKhoan, idPricing, total);
        return ResponseEntity.ok(new ApiResponse(success, success ? "Nâng cấp thành công" :
                                                                    "Nâng cấp không thành công."));
    }

    @GetMapping("/payment-history")
    public ResponseEntity<?> getPaymentHistory(@RequestParam(defaultValue = "5") int limit,
                                               @RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "ngayTao") String sort,
                                               @RequestParam(defaultValue = "DESC") String order,
                                               @AuthenticationPrincipal TaiKhoan taiKhoan) {
        List<ThanhToanRepDTO> thanhToanList = thanhToanService.getLichSuThanhToanThanhToan(limit, page, sort, order, taiKhoan);
        Pagination pagination = Pagination.builder()
                .limit(5)
                .page(1)
                .count(thanhToanList.size())
                .totalPages(1)
                .build();
        return ResponseEntity.ok(Map.of("success", true,
                                        "payments", thanhToanList,
                                        "pagination", pagination ));
    }

    @PostMapping("/reset-password-required")
    public ResponseEntity<?> resetPasswordRequired(@RequestBody ResetPasswordDTO req) {
        return ResponseEntity.ok(authService.resetPasswordRequired(req));
    }

    @PostMapping("/reset-password-verify")
    public ResponseEntity<?> resetPasswordVerify(@RequestBody ResetPassVerifyDTO req) {

        if (req.getEmail() != null) {
            ApiResponse apiResponse = authService.resetByEmail(
                    req.getEmail(),
                    req.getOtp(),
                    req.getPassword()
            );
            return ResponseEntity.ok(apiResponse);
        } else if (req.getPhone() != null) {
            ApiResponse apiResponse = authService.resetByPhone(
                    req.getPhone(),
                    req.getOtp(),
                    req.getPassword()
            );
            return ResponseEntity.ok(apiResponse);
        }
        return null;
    }

    @PostMapping("/send-mail")
    public ResponseEntity<?> sendOtpEmail(@RequestBody ResetPasswordDTO req) {
        return ResponseEntity.ok(authService.resetPasswordRequired(req));
    }

    @PostMapping("/verify-mail")
    public ResponseEntity<?> verifyEmail(@RequestBody ResetPassVerifyDTO req) {

        if (req.getEmail() != null) {
            ApiResponse apiResponse = authService.verifyByEmail(
                    req.getEmail(),
                    req.getOtp()
            );
            return ResponseEntity.ok(apiResponse);
        } else if (req.getPhone() != null) {
            ApiResponse apiResponse = authService.resetByPhone(
                    req.getPhone(),
                    req.getOtp(),
                    req.getPassword()
            );
            return ResponseEntity.ok(apiResponse);
        }
        return null;
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody TaiKhoanUpdateDTO req,
                                           @AuthenticationPrincipal TaiKhoan taiKhoan) {
        TaiKhoan taiKhoanExit = taiKhoanRepository.findById(taiKhoan.getId()).get();
        taiKhoanExit.setHoVaTen(req.getFullname());
        taiKhoanExit.setAnhDaiDien(req.getAvatar().get(0));
        taiKhoanRepository.save(taiKhoanExit);

        try {
            TaiKhoanResponse dto = taiKhoanService.findByCurrentLogin();
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "user", dto,
                    "msg", "Cập nhậnhaatjoong tin thành công."
            ));
        } catch (DataNotFoundException e) {
            return ResponseEntity.ok(Map.of(
                    "success", false,
                    "user", null,
                    "msg", e.getMessage()
            ));
        }

    }


}
