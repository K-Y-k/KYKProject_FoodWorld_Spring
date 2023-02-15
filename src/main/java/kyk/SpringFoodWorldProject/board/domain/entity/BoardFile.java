package kyk.SpringFoodWorldProject.board.domain.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardFile extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String originalFileName;

    private String storedFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    // board 엔티티에 originalFileName, storedFileName 컬럼을 추가하지 않기 위해
    public static BoardFile toBoardFileEntity(Board board, String originalFileName, String storedFileName) {
         return BoardFile.builder()
                 .originalFileName(originalFileName)
                 .storedFileName(storedFileName)
                 .board(board)
                 .build();
    }


}
