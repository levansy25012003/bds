package com.example.bds.service.impl;

import com.example.bds.component.JwtTokenUtil;
import com.example.bds.dto.admin.TaiKhoanDTO;
import com.example.bds.dto.admin.TaiKhoanResponseAdminDTO;
import com.example.bds.dto.rep.Pagination;
import com.example.bds.dto.rep.TaiKhoanResponse;
import com.example.bds.dto.rep.WishlistRepDto;
import com.example.bds.dto.req.GoiDichVuDto;
import com.example.bds.exception.DataNotFoundException;
import com.example.bds.model.GoiDichVu;
import com.example.bds.model.TaiKhoan;
import com.example.bds.repository.GoiDichVuRepository;
import com.example.bds.repository.TaiKhoanRepository;
import com.example.bds.service.IDonHangService;
import com.example.bds.service.ITaiKhoanService;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaiKhoanService implements ITaiKhoanService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final GoiDichVuRepository goiDichVuRepository;
    private final IDonHangService donHangService;

    @Override
    public String login(String email, String password) throws Exception {
        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByEmail(email);
        if (taiKhoanOpt.isEmpty()) {
            throw new DataNotFoundException("Khong tim thay email" + email);
        }

        TaiKhoan taiKhoan = taiKhoanOpt.get();
        if (!passwordEncoder.matches(password, taiKhoan.getMatKhau())) {
            throw new DataNotFoundException("Sai mat khau");
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password, taiKhoan.getAuthorities());
        try {
            authenticationManager.authenticate(authenticationToken);

        } catch (Exception e) {
            // Xử lý các lỗi khác nếu có
            throw new RuntimeException("Lỗi xác thực: " + e.getMessage());
        }

        return jwtTokenUtil.generateToken(taiKhoan);
    }

    @Override
    public TaiKhoan findById(Integer mataikhoan) throws DataNotFoundException {
        return taiKhoanRepository.findById(mataikhoan)
                .orElseThrow(() -> new DataNotFoundException("Khong tim thay tai khoan voi ma: " + mataikhoan));
    }

    @Override
    public Optional<TaiKhoan> finByPhoneNumber(String phoneNumber) throws DataNotFoundException {
        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findByDienThoai(phoneNumber);
        return taiKhoanOpt;
    }

    @Override
    public int updatePhoneAndVerified(Integer id, String phone) {
        int a = taiKhoanRepository.updatePhoneAndVerified(id, phone);
        return taiKhoanRepository.updatePhoneAndVerified(id, phone); // Chuyển sang Long nếu cần
    }

    public TaiKhoanResponse findByCurrentLogin() throws DataNotFoundException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();
        TaiKhoanResponse dto = taiKhoanRepository.findByWithDetailByEmail(email)
                .map(tk -> {
                            List<WishlistRepDto> wishlistDtos = WishlistRepDto.fromEntity(new ArrayList<>(tk.getDanhSachYeuThiches()));
                         return TaiKhoanResponse.builder()
                        .email(tk.getEmail())
                        .id(tk.getId())
                        .fullname(tk.getHoVaTen())
                        .role(tk.getVaiTro().getDisplayName())
                        .phone(tk.getDienThoai())
                        .createdAt(null)
                        .balance(tk.getSoDu())
                        .score(tk.getDiemTichLuy())
                        .avatar(tk.getAnhDaiDien())
                        .phoneVerified(tk.getDienThoaiXacThuc())
                        .emailVerified(tk.getEmailXacThuc())
                        .currentPricingId(tk.getMaGiaHienTai() != null ? tk.getMaGiaHienTai().getId() : null)
                        .rPricing(mapGoiDichVuToDto(tk.getMaGiaHienTai()))
                        .rWishlist(wishlistDtos)
                        .build();
                })
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy user."));
        return dto;
    }
    private GoiDichVuDto mapGoiDichVuToDto(GoiDichVu entity) {
        if (entity == null) return null;
        return GoiDichVuDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .isDisplayedImmediately(entity.getIsDisplayedImmediately())
                .isDisplayedSeller(entity.getIsDisplayedSeller())
                .isDisplayedPhoneBtn(entity.getIsDisplayedPhoneBtn())
                .isDisplayedDescription(entity.getIsDisplayedDescription())
                .imageSize(entity.getImageSize())
                .priority(entity.getPriority())
                .requiredScore(entity.getRequiredScore())
                .requiredScoreNextLevel(entity.getRequiredScoreNextLevel())
                .price(entity.getPrice())
                .expiredDay(entity.getExpiredDay())
                .imageUrl(entity.getImageUrl())
                .build();
    }

    public TaiKhoanResponseAdminDTO getAllTaiKhoanByAdmin(int limit, int page, String sort, String order, String fullname) {
        Sort.Direction direction = order.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, "ngayTao"));

        Page<TaiKhoan> taiKhoanPage;
        if (fullname != null && !fullname.isEmpty()) {
            taiKhoanPage = taiKhoanRepository.findByHoVaTenContainingIgnoreCase(fullname, pageable);
        } else {
            taiKhoanPage = taiKhoanRepository.findAll(pageable);
        }
        List<TaiKhoan> taiKhoans = taiKhoanPage.getContent();
        List<TaiKhoanDTO> taiKhoanDTOS = new ArrayList<>();
        for(TaiKhoan item : taiKhoans) {
            TaiKhoanDTO taiKhoanDTO = TaiKhoanDTO.fromEntity(item);
            taiKhoanDTOS.add(taiKhoanDTO);
        }
        Pagination pagination = Pagination.builder()
                .limit(limit)
                .page(page)
                .count(taiKhoanPage.getTotalElements())
                .totalPages(taiKhoanPage.getTotalPages())
                .build();
        TaiKhoanResponseAdminDTO result = TaiKhoanResponseAdminDTO.builder()
                .success(true)
                .users(taiKhoanDTOS)
                .pagination(pagination)
                .build();
        return result;
    }

    @Override
    public boolean updateRoleTaiKhoanByAdmin(Integer id, String role) {

        TaiKhoan.VaiTro roleNew = switch (role) {
            case "Thành viên" -> TaiKhoan.VaiTro.THANHVIEN;
            case "Chủ bất động sản" -> TaiKhoan.VaiTro.CHUBATDONGSAN;
            case "Quản trị viên" -> TaiKhoan.VaiTro.QUANTRIVIEN;
            default -> TaiKhoan.VaiTro.THANHVIEN;
        };

        int result = taiKhoanRepository.updateRoleById(id, roleNew);
        return result > 0;
    }

    public boolean deleteTaiKhoanByAdmin(Integer id) {
        if (taiKhoanRepository.findById(id).isPresent()) {
            taiKhoanRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean buyGoiDichVu(TaiKhoan taiKhoan, Integer maGoiDichVu, Integer total) {
        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findById(taiKhoan.getId());
        Optional<GoiDichVu> goiDichVu = goiDichVuRepository.findById(maGoiDichVu);

        if (!taiKhoanOpt.isPresent()) {
            return false;
        }
        TaiKhoan taiKhoanExiting = taiKhoanOpt.get();
        taiKhoanExiting.setMaGiaHienTai(goiDichVu.get());
        taiKhoanExiting.setSoDu(taiKhoanExiting.getSoDu() - total);
        taiKhoanRepository.save(taiKhoanExiting);

        return donHangService.createDonHang(taiKhoan, maGoiDichVu, total);
    }

    @Override
    public boolean increaseSoDuTaiKhoan(Integer id, Integer amount) {

        Optional<TaiKhoan> taiKhoanOpt = taiKhoanRepository.findById(id);
        if (!taiKhoanOpt.isPresent()) {
            return false;
        }
        TaiKhoan taiKhoan = taiKhoanOpt.get();
        taiKhoan.setSoDu(taiKhoan.getSoDu() + amount);

        taiKhoanRepository.save(taiKhoan);
        return true;
    }
}
