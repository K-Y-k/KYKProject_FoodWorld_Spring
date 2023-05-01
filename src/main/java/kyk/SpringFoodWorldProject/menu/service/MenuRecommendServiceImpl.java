package kyk.SpringFoodWorldProject.menu.service;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardSearchCond;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUpdateForm;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUploadForm;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuSearchCond;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import kyk.SpringFoodWorldProject.menu.repository.MenuRecommendRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;
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


    @Override
    public Page<MenuRecommend> PageList(Long memberId, Pageable pageable) {
        return menuRecommendRepository.findByMemberId(pageable, memberId);
    }

    @Override
    public Optional<MenuRecommend> findById(Long menuRecommendId) {
        return menuRecommendRepository.findById(menuRecommendId);
    }


    @Override
    public MenuRecommend randomPick(String selectedCategory) {
        if (selectedCategory == null || selectedCategory.equals("전체")) {
            Random random = new Random();
            Long lastId = menuRecommendRepository.findLastId();
            int randomId = random.nextInt(lastId.intValue()) + 1; // 랜덤 함수는 0~넣은값-1까지 랜덤으로 가져오므로 1~넣은값으로 가져오고 싶어 +1

            return menuRecommendRepository.findById((long) randomId).orElseThrow(() ->
                    new IllegalArgumentException("메뉴 가져오기 실패: 메뉴룰 찾지 못했습니다." + randomId));
        }
        else {
            Long findRandomId = menuRecommendRepository.findLastIdWithCategory(selectedCategory);

            return menuRecommendRepository.findById(findRandomId).orElseThrow(() ->
                    new IllegalArgumentException("메뉴 가져오기 실패: 메뉴룰 찾지 못했습니다." + findRandomId));
        }
    }


    @Override
    public void updateMenu(Long menuRecommendId, MenuRecommendUpdateForm updateForm) throws IOException {
        MenuRecommend menuRecommend = menuRecommendRepository.findById(menuRecommendId).orElseThrow(() ->
                new IllegalArgumentException("메뉴 조회 실패: " + menuRecommendId));

        MultipartFile imageFile = updateForm.getImageFile();

        if (imageFile.getOriginalFilename() != null && !imageFile.getOriginalFilename().isBlank()) {
            // 기존 디렉토리에 저장되었던 파일 삭제
            Path beforeFilePath = Paths.get(menuRecommendLocation+"\\"+menuRecommend.getStoredFileName());

            try {
                Files.deleteIfExists(beforeFilePath);
            } catch (DirectoryNotEmptyException e) {
                log.info("디렉토리가 비어있지 않습니다");
            } catch (IOException e) {
                e.printStackTrace();
            }


            // 다시 새로 등록
            String originalFileName = imageFile.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String storedFileName = uuid + "_" + originalFileName;
            String savePath = menuRecommendLocation;

            imageFile.transferTo(new File(savePath, storedFileName));

            menuRecommend.updateMenuNewFile(updateForm, originalFileName, storedFileName);

        } else {
            menuRecommend.updateMenu(updateForm);
        }
    }

    @Override
    public void delete(Long menuRecommendId) throws IOException {
        MenuRecommend menuRecommend = menuRecommendRepository.findById(menuRecommendId).orElseThrow(() ->
                new IllegalArgumentException("메뉴 가져오기 실패: 메뉴룰 찾지 못했습니다." + menuRecommendId));

        Path beforeFilePath = Paths.get(menuRecommendLocation+"\\"+menuRecommend.getStoredFileName());

        try {
            Files.deleteIfExists(beforeFilePath);
        } catch (DirectoryNotEmptyException e) {
            log.info("디렉토리가 비어있지 않습니다");
        } catch (IOException e) {
            e.printStackTrace();
        }

        menuRecommendRepository.delete(menuRecommendId);
    }

    @Override
    public Page<MenuRecommend> categoryMenuList(MenuSearchCond menuSearchCond, Pageable pageable) {
       return menuRecommendRepository.categoryMenuList(menuSearchCond, pageable);
    }

    @Override
    public Long findFirstCursorMenuId(String memberId) {
        return menuRecommendRepository.findFirstCursorMenuId(memberId);
    }

    @Override
    public Slice<MenuRecommend> searchBySlice(Long lastCursorId, Boolean first, Pageable pageable, String memberId) {
        return menuRecommendRepository.searchBySlice(lastCursorId, first, pageable, memberId);
    }
}
