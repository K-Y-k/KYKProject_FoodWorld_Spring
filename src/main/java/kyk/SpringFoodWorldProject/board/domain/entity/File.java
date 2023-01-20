//package kyk.SpringFoodWorldProject.board.domain.entity;
//
//import kyk.SpringFoodWorldProject.member.domain.entity.Member;
//import lombok.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Entity
//public class File {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "file_id")
//    private Long id;
//
//    @Column(updatable = false)
//    private UploadFile attachFile;
//
//    @Column(updatable = false)
//    private List<UploadFile> imageFiles;
//
//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member;
//
//    @ManyToOne
//    @JoinColumn(name = "board_id")
//    private Board board;
//
//}
