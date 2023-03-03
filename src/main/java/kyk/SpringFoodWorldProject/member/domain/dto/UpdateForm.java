package kyk.SpringFoodWorldProject.member.domain.dto;

import kyk.SpringFoodWorldProject.board.domain.dto.BoardUploadForm;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * 회원 수정시 사용될 DTO
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter @Setter
public class UpdateForm {
    private Long id;

    @NotEmpty(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 7, message = "최소 2글자, 최대 7글자입니다.")
    private String name;

    @Size(min = 3, max = 10, message = "최소 3글자, 최대 10글자입니다.")
    @NotEmpty(message = "아이디를 입력해주세요")
    private String loginId;

    @Size(min = 3, max = 10, message = "최소 3글자, 최대 10글자입니다.")
    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;

    private String introduce;

    private MultipartFile profileImage;
    private String fileAttached;


//    public Board toSaveEntity(Member member, BoardUploadForm boardDto) {
//        return Board.builder()
//                .title(title)
//                .content(content)
//                .writer(member.getName())
//                .member(member)
//                .boardType(boardType)
//                .subType(subType)
//                .fileAttached(0)
//                .build();
//    }
//    public Board toSaveFileEntity(Member member, BoardUploadForm boardDto) {
//        return Board.builder()
//                .title(title)
//                .content(content)
//                .writer(member.getName())
//                .member(member)
//                .boardType(boardType)
//                .subType(subType)
//                .fileAttached(1)
//                .build();
//    }


    public UpdateForm(String name, String loginId, String password, String introduce, MultipartFile profileImage, String fileAttached) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.introduce = introduce;
        this.profileImage = profileImage;
        this.fileAttached = fileAttached;
    }
}
