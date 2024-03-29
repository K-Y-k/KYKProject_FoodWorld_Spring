package kyk.SpringFoodWorldProject.board.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kyk.SpringFoodWorldProject.board.domain.dto.FreeBoardUpdateForm;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardFile_id")
    private Long id;

    private String originalFileName;

    private String storedFileName;

    private String attachedType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;


    public BoardFile(String storedFileName, Board board) {
        this.storedFileName = storedFileName;
        this.board = board;
    }

    // board 엔티티에 originalFileName, storedFileName 컬럼을 추가하지 않기 위해
    public static BoardFile toBoardFileEntity(Board board, String originalFileName, String storedFileName, String attachedType) {
         return BoardFile.builder()
                 .originalFileName(originalFileName)
                 .storedFileName(storedFileName)
                 .board(board)
                 .attachedType(attachedType)
                 .build();
    }

    public void updateBoardFile(String originalFileName, String storedFileName, String attachedType){
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.attachedType = attachedType;
    }


}
