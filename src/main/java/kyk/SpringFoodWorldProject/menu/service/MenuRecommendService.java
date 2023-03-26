package kyk.SpringFoodWorldProject.menu.service;

import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUpdateForm;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUploadForm;
import kyk.SpringFoodWorldProject.menu.domain.entity.MenuRecommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface MenuRecommendService {
    void uploadMenu(Long memberId, MenuRecommendUploadForm uploadForm) throws IOException;
    Page<MenuRecommend> PageList(Long memberId, Pageable pageable);
    MenuRecommend findById(Long menuRecommendId);
    void updateMenu(Long menuRecommendId, MenuRecommendUpdateForm updateForm) throws IOException;
    void delete(Long menuRecommendId) throws IOException;
}
