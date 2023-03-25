package kyk.SpringFoodWorldProject.menu.service;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUploadForm;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import kyk.SpringFoodWorldProject.menu.repository.MenuRecommendRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class MenuRecommendServiceImpl implements MenuRecommendService{
    private final MenuRecommendRepository menuRecommendRepository;
    private final MemberRepository memberRepository;

    @Value("${file.menuRecommendLocation}")
    private String menuRecommendLocation;

    @Override
    public void uploadMenu(Long memberId, MenuRecommendUploadForm uploadForm) throws IOException {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("회원 조회 실패: " + memberId));

        MultipartFile imageFile = uploadForm.getImageFile();

        if (imageFile.getOriginalFilename() != null && !imageFile.getOriginalFilename().isBlank()) {
            imageUpload(member, uploadForm);
        } else {
            throw new IllegalArgumentException("메뉴 사진을 선택해주세요.");
        }
    }

    private void imageUpload(Member member, MenuRecommendUploadForm uploadForm) throws IOException {
        String originalFileName = uploadForm.getImageFile().getOriginalFilename();

        // 파일에 이름을 붙일 랜덤으로 식별자 지정
        UUID uuid = UUID.randomUUID();
        String storedFileName = uuid + "_" + originalFileName;
        String savePath = menuRecommendLocation;

        // 실제 파일 저장 경로와 파일 이름 지정한 File 객체 생성 및 저장
        uploadForm.getImageFile().transferTo(new File(savePath, storedFileName));

        // DB에도 저장
        MenuRecommend menuRecommendEntity = uploadForm.toSaveEntity(member, uploadForm, storedFileName);
        menuRecommendRepository.save(menuRecommendEntity);
    }


    public Page<MenuRecommend> PageList(Long memberId, Pageable pageable) {
        return menuRecommendRepository.findByMemberId(pageable, memberId);
    }
}
