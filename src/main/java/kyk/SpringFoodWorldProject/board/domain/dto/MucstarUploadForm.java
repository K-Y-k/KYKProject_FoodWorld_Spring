package kyk.SpringFoodWorldProject.board.domain.dto;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class MucstarUploadForm {
    private Long id;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 500, message = "최대 500글자입니다.")
    private String content;

    @NotEmpty(message = "게시판을 선택해주세요!")
    private String boardType;
    private String subType;

    private List<MultipartFile> imageFiles;
    private List<String> originalFileName;
    private List<String> storedFileName;

    public Board toSaveEntity(Member member, BoardUploadForm boardDto) {
        return Board.builder()
                .content(content)
                .writer(member.getName())
                .member(member)
                .boardType(boardType)
                .subType(subType)
                .fileAttached(0)
                .build();
    }
    public Board toSaveFileEntity(Member member, MucstarUploadForm boardDto) {
        return Board.builder()
                .content(content)
                .writer(member.getName())
                .member(member)
                .boardType(boardType)
                .subType(subType)
                .fileAttached(1)
                .build();
    }


    public MucstarUploadForm(String boardType) {
        this.boardType = boardType;
    }

    public MucstarUploadForm(Long id, String title, String content, String boardType, String subType) {
        this.id = id;
        this.content = content;
        this.boardType = boardType;
        this.subType = subType;
    }
}
