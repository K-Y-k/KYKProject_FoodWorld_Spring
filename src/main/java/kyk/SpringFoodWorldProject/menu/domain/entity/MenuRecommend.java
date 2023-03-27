package kyk.SpringFoodWorldProject.menu.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kyk.SpringFoodWorldProject.board.domain.entity.BaseTimeEntity;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import kyk.SpringFoodWorldProject.menu.domain.dto.MenuRecommendUpdateForm;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuRecommend extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menuRecommend_id")
    private Long id;

    private String category;

    private String franchises;

    private String menuName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "member_id")
    private Member member;

    private String originalFileName;

    private String storedFileName;


    public MenuRecommend(String category, String franchises, String menuName, Member member, String originalFileName, String storedFileName) {
        this.category = category;
        this.franchises = franchises;
        this.menuName = menuName;
        this.member = member;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
    }


    public void updateMenu(MenuRecommendUpdateForm updateForm) {
        this.category = updateForm.getCategory();
        this.franchises = updateForm.getFranchises();
        this.menuName = updateForm.getMenuName();
    }

    public void updateMenuNewFile(MenuRecommendUpdateForm updateForm, String originalFileName, String storedFileName) {
        this.category = updateForm.getCategory();
        this.franchises = updateForm.getFranchises();
        this.menuName = updateForm.getMenuName();
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
    }
}
