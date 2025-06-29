package com.example.bds.controller;

import com.example.bds.dto.admin.BatDongSanResponseAdminDTO;
import com.example.bds.dto.admin.StatusRequestDTO;
import com.example.bds.dto.rep.*;
import com.example.bds.dto.req.CommentReqDTO;
import com.example.bds.dto.req.CreatePostRequest;
import com.example.bds.dto.req.ExpireDTO;
import com.example.bds.dto.req.RatingDTO;
import com.example.bds.model.BatDongSan;
import com.example.bds.model.TaiKhoan;
import com.example.bds.repository.BatDongSanRepository;
import com.example.bds.service.IBatDongSanService;
import com.example.bds.service.IBinhLuanService;
import com.example.bds.service.IDanhGiaService;
import com.example.bds.service.ITinHetHanService;
import com.example.bds.service.impl.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class BatDongSanController {

    private final IBatDongSanService batDongSanService;
    private final IDanhGiaService danhGiaService;
    private final IBinhLuanService binhLuanService;
    private final ITinHetHanService tinHetHanService;
    private final MailService mailService;
    private final BatDongSanRepository batDongSanRepository;

    @PostMapping("/new")
    public ResponseEntity<?> createBatDongSan(@RequestBody CreatePostRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan currentUser = (TaiKhoan) authentication.getPrincipal();

        boolean success = batDongSanService.createBatDongSan(request, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse(success, success ? "Tạo tin đăng thành công." : "Tạo tin đăng không thành công."));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getBatDongSanByOwn(
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") String order,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String propertyType,
            @RequestParam(required = false) String status
            ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan currentUser = (TaiKhoan) authentication.getPrincipal();

        Page<BatDongSan> resultPage = batDongSanService.getProductByOwn(title, propertyType, status, currentUser.getId(), page, limit, sort, order);
        Page<BatDongSanDTO> pageDto = resultPage.map(BatDongSanDTO::fromEntity);
        ProductResponseDTO<BatDongSanDTO> response = ProductResponseDTO.<BatDongSanDTO>builder()
                .success(true)
                .properties(pageDto.getContent())
                .pagination(Pagination.builder()
                        .limit(limit)
                        .page(page)
                        .count(resultPage.getTotalElements())
                        .totalPages(resultPage.getTotalPages())
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/public")
    public ResponseEntity<?> getBatDongSan(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String listingType,
            @RequestParam(required = false) Integer bedroom,
            @RequestParam(required = false) Integer bathroom,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String propertyType
    ) {
        List<Integer> sizeRange = parseIntList(size);
        List<Long> priceRange = parseLongList(price);
        // Giá trị mặc định nếu không truyền: size (0 -> Integer.MAX), price (0 -> MAX)
        int minSize = (sizeRange != null && sizeRange.size() >= 1) ? sizeRange.get(0) : 0;
        int maxSize = (sizeRange != null && sizeRange.size() >= 2) ? sizeRange.get(1) : Integer.MAX_VALUE;

        long minPrice = (priceRange != null && priceRange.size() >= 1) ? priceRange.get(0) : 0;
        long maxPrice = (priceRange != null && priceRange.size() >= 2) ? priceRange.get(1) : 10000000000L;

        Page<BatDongSan> resultPage = batDongSanService.getPostAll(title, propertyType, listingType, address,
                                                                    minSize, maxSize, minPrice, maxPrice,
                                                                    bedroom, bathroom, page, limit);
        Page<PostResponseDTO> pageDto = resultPage.map(PostResponseDTO::fromEntity);
        ProductResponseDTO<PostResponseDTO> response = ProductResponseDTO.<PostResponseDTO>builder()
                .success(true)
                .properties(pageDto.getContent())
                .pagination(Pagination.builder()
                        .limit(limit)
                        .page(page)
                        .count(resultPage.getTotalElements())
                        .totalPages(resultPage.getTotalPages())
                        .build())
                .build();
        return ResponseEntity.ok(response);
    }

    private List<Integer> parseIntList(String input) {
        if (input == null || input.isEmpty()) return Collections.emptyList();
        input = input.replaceAll("\\[|\\]|\\s", "");
        return Arrays.stream(input.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBatDongSan(@PathVariable("id") Integer id,
                                              @RequestBody CreatePostRequest request) {

        boolean success = batDongSanService.updateBatDongSan(request, id);
        return ResponseEntity.ok(new ApiResponse(success, success ? "Cập nhật tin đăng thành công." : "Cập nhật tin không thành công."));
    }

    @DeleteMapping("/remove/")
    public  ResponseEntity<?> deleteBatDongSan(@RequestParam("postIds") Integer id) {
        boolean success = batDongSanService.deleteBatDongSan(id);
        return ResponseEntity.ok(new ApiResponse(success, success ? "Xóa tin đăng thành công." : "Xóa tin đăng không thành công."));
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<?> getDetailBatDongSan(@PathVariable("id") Integer id) {
        Optional<BatDongSan> bds = batDongSanService.findBaDongSanId(id);
        BatDongSanDTO xxx = BatDongSanDTO.fromEntity(bds.get());

        if (bds.isPresent()) {
            // Tăng view cho bds
            batDongSanService.increaseView(bds.get().getId());
        }

        List<VoterDTO> voterDtos = danhGiaService.findRatingByMaBatDongSan(bds.get().getId());
        List<CommentDTO> commentDTOS = binhLuanService.findCommentByMaBatDongSan(bds.get().getId());

        PostDetailDTO postDetailDTO = PostDetailDTO.builder()
                .success(true)
                .postData(xxx)
                .voters(voterDtos)
                .comments(commentDTOS)
                .build();
        return ResponseEntity.ok(postDetailDTO);
    }
    @PostMapping("/rating")
    public ResponseEntity<?> ratingBatDongSan(@RequestBody RatingDTO req,
                                              @AuthenticationPrincipal TaiKhoan taiKhoanExiting) {

        boolean success = danhGiaService.handleRating(req, taiKhoanExiting);
        return ResponseEntity.ok(new ApiResponse(success, success ? "Đánh giá tin đăng thành công, cảm ơn bạn!" :
                                                                    "Đánh giá thất bại, vui lòng thử lại."));
    }

    @PostMapping("/comment-new")
    public ResponseEntity<?> commentBatDongSan(@RequestBody CommentReqDTO req,
                                               @AuthenticationPrincipal TaiKhoan currentUser) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        TaiKhoan currentUser = (TaiKhoan) authentication.getPrincipal();

        boolean success = batDongSanService.createNewComment(currentUser.getId(), req);
        return ResponseEntity.ok(new ApiResponse(success, success ? "Bình luận thành công." : "Có lỗi, hãy thử lại."));
    }

    @GetMapping("/admin/posts")
    public ResponseEntity<?> getBatDongSanByAdmin(@RequestParam(defaultValue = "5", required = false) int limit,
                                                  @RequestParam(defaultValue = "1", required = false) int page,
                                                  @RequestParam(defaultValue = "createdAt", required = false) String sort,
                                                  @RequestParam(defaultValue = "DESC", required = false) String order,
                                                  @RequestParam(required = false) String status,
                                                  @RequestParam(required = false) Boolean isPublic) {

        BatDongSanResponseAdminDTO result = batDongSanService.getBatDongSanByAdmin(limit, page, sort, order, status, isPublic);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<?> updateStatusAndPublicBatDongSan(@PathVariable("id") Integer id,
                                                             @RequestBody StatusRequestDTO req) {

        boolean success = batDongSanService.updateStatusAndPublicBatDongSan(req, id);
        BatDongSan bds = batDongSanRepository.findById(id).get();
        // Gửi mail
        try {
            DataMailDTO dataMail = new DataMailDTO();
            dataMail.setTo("levansy25012003@gmai.com");
            dataMail.setSubject("Chúc mừng tin của bạn đã đc phê duyệt");

            Map<String, Object> props = new HashMap<>();
            props.put("name", "lê Văn Sỹ");
            props.put("username", bds.getTieuDe());
            props.put("password", bds.getId());

            dataMail.setProps(props);

            mailService.sendHtmlMail(dataMail, "Đổi mới mật khẩu.");

        } catch (Exception exp){
            ResponseEntity.ok(new ApiResponse(false, "Có lỗi hãy thử lại sau."));
        }
        return ResponseEntity.ok(new ApiResponse(success, success ? "Cập nhật trạng thái tin đăng thành công" :
                                                                    "Có lỗi hãy thử lại sau."));
    }

    @DeleteMapping("/remove-by-admin/{id}")
    public ResponseEntity<?> deleteBatDongSanByAdmin(@PathVariable("id") Integer id) {

        String a = "aa";
        boolean success = batDongSanService.deleteBatDongSan(id);
        return ResponseEntity.ok(new ApiResponse(success, success ? "Xóa tin đăng thành công." :
                                                                    "Xóa tin đăng không thành công."));
    }

    @PostMapping("/expire-post")
    public ResponseEntity<?> expirePost(@RequestBody ExpireDTO req,
                                        @AuthenticationPrincipal TaiKhoan taiKhoan) {
        String a = "aa";
        boolean success = tinHetHanService.createTinHetHan(req, taiKhoan);
        return ResponseEntity.ok(new ApiResponse(success, success ? "Gia hạn thành công." :
                                                                    "Gia hạn thất bại."));
    }

    @PostMapping("/draft")
    public ResponseEntity<?> saveTheDraft(@RequestBody CreatePostRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan currentUser = (TaiKhoan) authentication.getPrincipal();

        boolean success = batDongSanService.createBatDongSanNhap(request, currentUser.getId());
        return ResponseEntity.ok(new ApiResponse(success, success ? "Đã lưu bản nháp thành công" : "Lưu bản nháp không thành công."));
    }


    @GetMapping("/user-draft")
    public ResponseEntity<?> getUserPosts(@RequestParam(defaultValue = "5") int limit,
                                          @RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "createdAt") String sort,
                                          @RequestParam(defaultValue = "DESC") String order,
                                          @RequestParam(required = false) String title,
                                          @RequestParam(required = false) String propertyType,
                                          @RequestParam(required = false) String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan currentUser = (TaiKhoan) authentication.getPrincipal();

        Page<BatDongSan> resultPage = batDongSanService.getDraftByOwn(title, propertyType, status, currentUser.getId(), page, limit, sort, order);
        Page<BatDongSanDTO> pageDto = resultPage.map(BatDongSanDTO::fromEntity);
        ProductResponseDTO<BatDongSanDTO> response = ProductResponseDTO.<BatDongSanDTO>builder()
                .success(true)
                .properties(pageDto.getContent())
                .pagination(Pagination.builder()
                        .limit(limit)
                        .page(page)
                        .count(resultPage.getTotalElements())
                        .totalPages(resultPage.getTotalPages())
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }
    @PutMapping("/draft/{id}")
    public ResponseEntity<?> updateDraftBatDongSan(@PathVariable("id") Integer id,
                                              @RequestBody CreatePostRequest request) {

        boolean success = batDongSanService.updateBatDongSan(request, id);
        return ResponseEntity.ok(new ApiResponse(success, success ? "Cập nhật tin đăng thành công." : "Cập nhật tin không thành công."));
    }

    @PostMapping("/public-draft/{id}")
    public ResponseEntity<?> publicDraftBatDongSan(@PathVariable("id") Integer id,
                                                   @RequestBody CreatePostRequest request) {
        boolean isDraft = false;
        boolean success = batDongSanService.updateDraftBatDongSan(request, id, isDraft);
        return ResponseEntity.ok(new ApiResponse(success, success ? "Công khai tin đăng thành công." : "Công khai tin không thành công."));
    }
    private List<Long> parseLongList(String input) {
        if (input == null || input.isEmpty()) return Collections.emptyList();
        input = input.replaceAll("\\[|\\]|\\s", ""); // Xóa dấu [] và khoảng trắng
        return Arrays.stream(input.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
