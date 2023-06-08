package com.onejo.seosuri.service;

import com.onejo.seosuri.ai.orc.ImageToText;
import com.onejo.seosuri.controller.dto.classification.CategoryResDto;
import com.onejo.seosuri.domain.classification.CategoryTitle;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ClassificationService {

    @Transactional
    public String ocrImage(MultipartFile multipartFile) throws BusinessException {
        try {
            File file = convert(multipartFile);
            ImageToText imageToText = new ImageToText();
            String ocr_res_str = imageToText.detectText(file);
            return ocr_res_str;
        } catch(IllegalStateException e){
            throw new BusinessException(ErrorCode.MULTIPARTFILE_CONVERT_ERROR);
        }
    }

    public File convert(MultipartFile file)
    {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_IO_ERROR);
        }
        return convFile;
    }

    @Transactional
    public List<CategoryResDto> classification(String ocr_res_str){
        List<CategoryResDto> res = new ArrayList<>();

        // bert로 분류
        CategoryResDto ageCategoryResDto = new CategoryResDto(CategoryTitle.AGE);
        CategoryResDto unknownNumCategoryResDto = new CategoryResDto(CategoryTitle.UNKNOWN_NUM);
        CategoryResDto colorTapeCategoryResDto = new CategoryResDto(CategoryTitle.COLOR_TAPE);
        CategoryResDto geometryCategoryResDto = new CategoryResDto(CategoryTitle.GEOMETRY_APP_CALC);


        List<CategoryResDto> elseRes = new ArrayList<>();
        elseRes.add(unknownNumCategoryResDto);
        elseRes.add(colorTapeCategoryResDto);
        elseRes.add(geometryCategoryResDto);
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int first = random.nextInt(3);  // 0~2
        int second = (first + random.nextInt(2) + 1) % 3;  // 0~1
        int third = 3 - first - second;


        res.add(ageCategoryResDto);
        res.add(elseRes.get(first));
        res.add(elseRes.get(second));
        res.add(elseRes.get(third));

        return res;
    }

}
