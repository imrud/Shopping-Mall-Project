package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    // 상품을 등록하는 클래스

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        // 상품 등록
        Item item = itemFormDto.createItem();   // 상품 등록 폼으로부터 입력 받은 데이터를 이용하여 item 객체 생성
        itemRepository.save(item);  // 상품 데이터를 저장하기

        // 이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0)      // 첫 번째 이미지일 경우 대표 상품이미지 여부 값을 "Y"로 세팅
                itemImg.setRepimgYn("Y");
            else    // 나머지 상품 이미지는 "N"으로 설정하기
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));    // 상품의 이미지 정보 저장하기
        }

        return item.getId();
    }
    @Transactional(readOnly = true) // 상품 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정 -> 이런경우 jpa가 더티체킹(변경감지) 수행하지 않아 성능 향상 시킬 수 있음
    public ItemFormDto getItemDtl(Long itemId){

        // 해당 삼품의 이미지를 조회. 등록순으로 가지고 오기 위해서 상품 이미지 아이디 오름차순으로 가지고 오기
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        // 조회한 itemImg 엔티티를 itemImgDto 객체로 만들어서 리스트에 추가한다.
        for (ItemImg itemImg : itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // 상품의 아이디를 통해 상품 엔티티를 조회한다. 존재하지 않을 때는 EntityNotFoundException 을 발생시킨다.
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new); // 상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회하기
        item.updateItem(itemFormDto);   // 상품 등록 화면으로부터 전달 받은 ItemFormDto를 토앻 상품 엔티티를 업데이트 한다.

        List<Long> itemImgIds = itemFormDto.getItemImgIds();    // 상품 이미지 아이디 리스트를 조회한다.

        //이미지 등록
        // 상품 이미지를 업데이트하기 위해서 updateItemImg() 메소드에
        // 상품 이미지 아이디와, 상품 이미지 파일 정보를 파라미터로 전달한다
        for(int i=0; i  <itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);

    }
}
