package kyk.SpringFoodWorldProject.member.domain.entity;

import kyk.SpringFoodWorldProject.board.domain.entity.BaseTimeEntity;
import kyk.SpringFoodWorldProject.board.domain.entity.Board;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProfileFile extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profileFile_id")
    private Long id;

    private String originalFileName;

    private String storedFileName;

    private String fileAttached;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public ProfileFile(String storedFileName, Member member) {
        this.storedFileName = storedFileName;
        this.member = member;
    }

    // board 엔티티에 originalFileName, storedFileName 컬럼을 추가하지 않기 위해
    public static ProfileFile toProfileFileEntity(Member member, String originalFileName, String storedFileName, String fileAttached) {
         return ProfileFile.builder()
                 .originalFileName(originalFileName)
                 .storedFileName(storedFileName)
                 .member(member)
                 .fileAttached(fileAttached)
                 .build();
    }


}
