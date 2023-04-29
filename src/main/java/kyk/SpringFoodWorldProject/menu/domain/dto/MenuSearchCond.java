package kyk.SpringFoodWorldProject.menu.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuSearchCond {
    private String selectedCategory;
    private String menuNameKeyword;
    private String franchisesKeyword;
}
