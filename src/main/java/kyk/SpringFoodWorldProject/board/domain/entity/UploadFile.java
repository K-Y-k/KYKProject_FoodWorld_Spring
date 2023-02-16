//package kyk.SpringFoodWorldProject.board.domain.entity;
//
//import lombok.AccessLevel;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.Embeddable;
//
//@Data
//@Embeddable
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class UploadFile {
//    // 파일명이 동일하면 안되기에 uploadFileName과 storeFileName을 분리했다.
//    // storeFileName은 유효Id를 사용해 겹치는 것을 방지
//    private String originalFileName; // 업로드한 파일명 : 고객이 보여지는 화면에서 활용
//
//    private String storedFileName;  // 시스템에 저장할 파일명
//
//    public UploadFile(String originalFileName, String storedFileName) {
//        this.originalFileName = originalFileName;
//        this.storedFileName = storedFileName;
//    }
//}
