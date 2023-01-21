//package kyk.SpringFoodWorldProject.board.domain.entity;
//
//import kyk.SpringFoodWorldProject.member.domain.entity.Member;
//import lombok.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//@Entity
//public class FileStore {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "file_id")
//    private Long id;
//
//    @Column(nullable = false)
//    private String originFileName;
//
//    @Column(nullable = false)
//    private String filePath;
//
//    @Column(nullable = false)
//    private String savedName;
//
//    private Long fileSize;
//
////    @Column
////    private UploadFile attachFile;
////    @Column
////    private List<UploadFile> imageFiles;
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
