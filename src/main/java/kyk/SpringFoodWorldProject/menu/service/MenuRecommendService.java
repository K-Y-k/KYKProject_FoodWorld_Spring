package kyk.SpringFoodWorldProject.menu.service;

import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUploadForm;

import java.io.IOException;

public interface MenuRecommendService {
    void uploadMenu(Long memberId, MenuRecommendUploadForm uploadForm) throws IOException;
}
