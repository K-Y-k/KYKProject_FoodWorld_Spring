package kyk.SpringFoodWorldProject.menu.service;

import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.member.repository.MemberRepository;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUpdateForm;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUploadForm;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class MenuRecommendServiceImplTest {
    @Mock
    private Random random;
    @Autowired
    MenuRecommendServiceImpl menuRecommendService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("이미지를 필수로 넣은 메뉴를 업로드하고 전체의 리스트로 조회가 정상 작동하는지 테스트")
    void uploadMenuAndPageList() throws IOException {
        // given
        Member saveMember = memberRepository.saveMember(new Member("kyk", "asd", "111"));

        // 테스트용 이미지 생성
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] content = baos.toByteArray();
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", content);

        MenuRecommendUploadForm uploadForm= new MenuRecommendUploadForm("한식", "식당명", "메뉴이름", file);

        // when
        menuRecommendService.uploadMenu(saveMember.getId(), uploadForm);

        Pageable pageable = PageRequest.of(0, 9, Sort.Direction.DESC, "id");
        Page<MenuRecommend> menuRecommends = menuRecommendService.PageList(saveMember.getId(), pageable);

        // then
        for (MenuRecommend menuRecommend : menuRecommends) {
            Assertions.assertThat(menuRecommend.getMenuName()).isEqualTo("메뉴이름");
        }
        Assertions.assertThat(menuRecommends.getTotalElements()).isEqualTo(1);
    }


    @Test
    @DisplayName("등록했던 메뉴를 수정 테스트")
    void updateMenu() throws IOException {
        // given
        Member saveMember = memberRepository.saveMember(new Member("kyk", "asd", "111"));

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] content = baos.toByteArray();
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", content);

        MenuRecommendUploadForm uploadForm= new MenuRecommendUploadForm("한식", "식당명", "메뉴이름", file);
        menuRecommendService.uploadMenu(saveMember.getId(), uploadForm);

        MenuRecommendUpdateForm updateForm = new MenuRecommendUpdateForm("한식", "식당수정", "메뉴수정", file);

        // when
        menuRecommendService.updateMenu(saveMember.getId(), updateForm);

        // then
        Pageable pageable = PageRequest.of(0, 9, Sort.Direction.DESC, "id");
        Page<MenuRecommend> menuRecommends = menuRecommendService.PageList(saveMember.getId(), pageable);

        for (MenuRecommend menuRecommend : menuRecommends) {
            Assertions.assertThat(menuRecommend.getMenuName()).isEqualTo("메뉴수정");
        }
    }

    @Test
    @DisplayName("등록했던 메뉴를 삭제 테스트")
    void deleteMenu() throws IOException {
        // given
        Member saveMember = memberRepository.saveMember(new Member("kyk", "asd", "111"));

        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] content = baos.toByteArray();
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", content);

        MenuRecommendUploadForm uploadForm= new MenuRecommendUploadForm("한식", "식당명", "메뉴이름", file);
        menuRecommendService.uploadMenu(saveMember.getId(), uploadForm);

        // when
        menuRecommendService.delete(1L);

        // then
        Pageable pageable = PageRequest.of(0, 9, Sort.Direction.DESC, "id");
        Page<MenuRecommend> menuRecommends = menuRecommendService.PageList(saveMember.getId(), pageable);

        for (MenuRecommend menuRecommend : menuRecommends) {
            Assertions.assertThat(menuRecommend).isNull();
        }
    }
}