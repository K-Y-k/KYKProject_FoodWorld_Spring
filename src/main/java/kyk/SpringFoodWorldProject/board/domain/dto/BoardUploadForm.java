package kyk.SpringFoodWorldProject.board.domain.dto;

import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import kyk.SpringFoodWorldProject.member.domain.entity.Member;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 글 저장 전송 객체
 */
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class BoardUploadForm {
    private Long id;

    @NotBlank(message = "제목을 입력해주세요")
    @Size(max = 60, message = "최대 50글자입니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(max = 500, message = "최대 500글자입니다.")
    private String content;

    @NotEmpty(message = "게시판을 선택해주세요!")
    private String boardType;
    private String subType;

    private List<MultipartFile> imageFiles;
    private List<MultipartFile> attachFiles;
    private List<String> originalFileName;
    private List<String> storedFileName;
    private int fileAttached;


    public Board toSaveEntity(Member member, BoardUploadForm boardDto) {
        return Board.builder()
                .title(title)
                .content(content)
                .writer(member.getName())
                .member(member)
                .boardType(boardType)
                .subType(subType)
                .fileAttached(0)
                .build();
    }
    public Board toSaveFileEntity(Member member, BoardUploadForm boardDto) {
        return Board.builder()
                .title(title)
                .content(content)
                .writer(member.getName())
                .member(member)
                .boardType(boardType)
                .subType(subType)
                .fileAttached(1)
                .build();
    }


    public BoardUploadForm(String boardType) {
        this.boardType = boardType;
    }

    public BoardUploadForm(Long id, String title, String content, String boardType, String subType, int fileAttached) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.subType = subType;
        this.fileAttached = fileAttached;
    }
}