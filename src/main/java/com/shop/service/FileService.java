package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    // 파일 업로드하는 메소드
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID();  // 서로 다른 개체들을 구별하기 위해서 이름을 부여할 때 사용 -> 파일의 이름으로 사용하면 파일명 중복 문제 해결가능
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension; // UUID로 받은 값과 원래 파일 이름의 확장자 조합해 저장될 파일 이름 만들기
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;   // 업로드된 파일의 이름을 반환함
    }

    // 파일 삭제하는 메소드
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);   // 파일이 저장된 경로를 이용하여 파일 객쳋를 생성
        
        if(deleteFile.exists()) {   //해당 파일 존재하면 파일 삭제하기
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}
