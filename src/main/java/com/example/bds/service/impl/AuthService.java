package com.example.bds.service.impl;

import com.example.bds.component.DataUtils;
import com.example.bds.component.JwtTokenUtil;
import com.example.bds.dto.rep.ApiResponse;
import com.example.bds.dto.rep.AuthResponse;
import com.example.bds.dto.rep.DataMailDTO;
import com.example.bds.dto.req.LoginEmailDTO;
import com.example.bds.dto.req.LoginGoogleDTO;
import com.example.bds.dto.req.LoginPhoneDTO;
import com.example.bds.dto.req.ResetPasswordDTO;
import com.example.bds.exception.DataNotFoundException;
import com.example.bds.model.GoiDichVu;
import com.example.bds.model.TaiKhoan;
import com.example.bds.repository.TaiKhoanRepository;
import com.example.bds.service.IAuthService;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    @Value("${twilio.serviceSid}")
    private String serviceSid;
    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    private final  MailService mailService;

    public String loginWithGoogle(LoginGoogleDTO req) throws Exception {

        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByEmail(req.getEmail());
        TaiKhoan taiKhoan;

        if (taiKhoanOpt.isPresent()) {
            // Đã có tài khoản trong DB
            taiKhoan = taiKhoanOpt.get();
        } else {
            // Không có tài khoản, kiểm tra password để tạo tài khoản mới
            if (req.getPassword() == null) {
                throw new DataNotFoundException("Tài khoản hoặc mật khẩu không đúng");
            }

            // Tạo mới tài khoản
            taiKhoan = new TaiKhoan();
            GoiDichVu goiDichVu = new GoiDichVu();
            goiDichVu.setId(1);

            taiKhoan.setEmail(req.getEmail());
            taiKhoan.setHoVaTen(req.getFullname());
            taiKhoan.setAnhDaiDien(req.getAvatar());
            taiKhoan.setEmailXacThuc(true);
           taiKhoan.setMaGiaHienTai(goiDichVu);

            taiKhoan.setMatKhau(passwordEncoder.encode(req.getPassword()));

            taiKhoan = taiKhoanRepository.save(taiKhoan);
        }

        String token = jwtTokenUtil.generateToken(taiKhoan);
        return token;
    }
    @Override
    public AuthResponse loginWithEmail(LoginEmailDTO req) {
        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByEmail(req.getEmail());
        if (taiKhoanOpt.isEmpty()) {
            return new AuthResponse(false, "Email chưa được đăng ký!", null);
        }

        TaiKhoan taiKhoan = taiKhoanOpt.get();
        boolean isCheckPassword = passwordEncoder.matches(req.getPassword(), taiKhoan.getMatKhau());
        if (!isCheckPassword) {
            return new AuthResponse(false, "Mật khẩu không chính xác", null);
        }
        String token = jwtTokenUtil.generateToken(taiKhoan);
        return new AuthResponse(true, "Đăng nhập thành công", token);
    }

    @Override
    public AuthResponse loginWithPhone(LoginPhoneDTO req) {
        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByDienThoai(req.getPhone());
        if (taiKhoanOpt.isEmpty()) {
            return new AuthResponse(false, "SĐT chưa được đăng ký!", null);
        }

        TaiKhoan taiKhoan = taiKhoanOpt.get();
        boolean isCheckPassword = passwordEncoder.matches(req.getPassword(), taiKhoan.getMatKhau());
        if (!isCheckPassword) {
            return new AuthResponse(false, "Mật khẩu không chính xác", null);
        }
        String token = jwtTokenUtil.generateToken(taiKhoan);
        return new AuthResponse(true, "Đăng nhập thành công", token);
    }

    @Override
    public Map<String, Object> resetPasswordRequired(ResetPasswordDTO req) {
        String email = req.getEmail();
        String phone = req.getPhone();

        Optional<TaiKhoan> taiKhoanOpt = Optional.empty();

        if (phone != null && !phone.isBlank()) {
            taiKhoanOpt = taiKhoanRepository.findByDienThoai(phone);
        }

        if (email != null && !email.isBlank()) {
            taiKhoanOpt = taiKhoanRepository.findByEmail(email);
        }

        if (taiKhoanOpt.isEmpty()) {
            return Map.of("success", false, "msg", "Không tìm thấy người dùng.");
        }

        TaiKhoan taiKhoan = taiKhoanOpt.get();
        String resetCodePaswod = DataUtils.generateTempPwd(6);
        long expiry = System.currentTimeMillis() + 5 * 60 * 1000;

        taiKhoan.setMaDatLaiMatKhau(resetCodePaswod);
        //taiKhoan.setThoiGianHetMaMatKhau(expiry);
        taiKhoanRepository.save(taiKhoan);
        if (email != null && !email.isBlank()) {
            try {
                DataMailDTO dataMail = new DataMailDTO();
                dataMail.setTo(req.getEmail());
                dataMail.setSubject("XÁC NHẬN TẠO MỚI THÔNG TIN NGƯỜI DÙNG");

                Map<String, Object> props = new HashMap<>();
                props.put("name", taiKhoan.getHoVaTen());
                props.put("username", taiKhoan.getEmail());
                props.put("password", resetCodePaswod);
                dataMail.setProps(props);

                mailService.sendHtmlMail(dataMail, "Đổi mới mật khẩu.");
                return Map.of("success", true, "msg", "Gửi mail thành công.");
            } catch (Exception exp){
                return Map.of("success", false, "msg", "Gửi mail không thành công.");
            }
        }
        if (phone != null) {{
            String fullphone = "+84" + phone;
            Verification.creator(serviceSid, fullphone, "sms").create();
            return Map.of("success", true, "msg", "Mã OTP đã gửi thành công.");
        }}

        return Map.of("success", false, "msg", "Yêu cầu không hợp lệ.");
    }

    @Override
    public ApiResponse resetByEmail(String email, String otp, String newPassword) {
        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByEmail(email);
        if (taiKhoanOpt.isEmpty()) {
            return new ApiResponse(false, "Không tìm thấy người dùng.");
        }

        TaiKhoan taiKhoan = taiKhoanOpt.get();
        boolean isValidOtp = otp.equals(taiKhoan.getMaDatLaiMatKhau());
//                &&
//                user.getResetPwdExpiry() != null &&
//                user.getResetPwdExpiry().isAfter(LocalDateTime.now());

        if (!isValidOtp) {
            return new ApiResponse(false, "OTP không hợp lệ hoặc đã hết hạn.");
        }

        taiKhoan.setMatKhau(passwordEncoder.encode(newPassword));
        taiKhoan.setThoiGianHetMaMatKhau(null);
        taiKhoanRepository.save(taiKhoan);

        return new ApiResponse(true, "Cập nhật mật khẩu thành công.");
    }


    @Override
    public ApiResponse resetByPhone(String phone, String otp, String newPassword) {

        String fullphone = "+84" + Long.parseLong(phone);
        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid, otp)
                    .setTo(fullphone)
                    .create();

            Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByDienThoai(phone);
            if (taiKhoanOpt.isEmpty()) {
                return new ApiResponse(false, "Không tìm thấy người dùng.");
            }
            if (!"approved".equals(verificationCheck.getStatus())) {
                return new ApiResponse(false, "OTP không hợp lệ.");
            }

            TaiKhoan taiKhoan = taiKhoanOpt.get();
            taiKhoan.setMatKhau(passwordEncoder.encode(newPassword));
            taiKhoan.setMaDatLaiMatKhau(null);
            taiKhoanRepository.save(taiKhoan);
        } catch (Exception e) {
            return new ApiResponse(false, "Mã OTP không hợp lệ mm.");
        }
        return new ApiResponse(true, "Cập nhật mật khẩu thành công.");
    }

    @Override
    public ApiResponse verifyByEmail(String email, String otp) {
        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByEmail(email);
        if (taiKhoanOpt.isEmpty()) {
            return new ApiResponse(false, "Không tìm thấy người dùng.");
        }

        TaiKhoan taiKhoan = taiKhoanOpt.get();
        boolean isValidOtp = otp.equals(taiKhoan.getMaDatLaiMatKhau());
//                &&
//                user.getResetPwdExpiry() != null &&
//                user.getResetPwdExpiry().isAfter(LocalDateTime.now());

        if (!isValidOtp) {
            return new ApiResponse(false, "OTP không hợp lệ hoặc đã hết hạn.");
        }

        taiKhoan.setThoiGianHetMaMatKhau(null);
        taiKhoan.setEmailXacThuc(true);
        taiKhoan.setEmail(email);
        taiKhoanRepository.save(taiKhoan);

        return new ApiResponse(true, "Cập nhật mật khẩu thành công.");
    }

}
