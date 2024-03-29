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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public ProfileFile(String originalFileName, String storedFileName, Member member) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.member = member;
    }
}
