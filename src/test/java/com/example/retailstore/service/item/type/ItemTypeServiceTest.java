package com.example.retailstore.service.item.type;

import com.example.retailstore.entity.ItemType;
import com.example.retailstore.repository.ItemTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemTypeServiceTest {

    @Mock
    ItemTypeRepository itemTypeRepository;
    @InjectMocks
    ItemTypeServiceImpl itemTypeService;

    @Test
    void findAllTest(){
        List<ItemType> itemTypes = Arrays.asList(new ItemType(1L, "ELECTRONICS", "Electronic items", true),
                new ItemType(2L, "GROCERY", "Grocery items", false),
                new ItemType(3L, "TOYS", "Baby toys", true),
                new ItemType(4L, "APPAREL", "Clothes", true),
                new ItemType(5L, "FURNITURE", "Home furniture", true),
                new ItemType(6L, "SHOES", "Shoes and sandals", true));
        when(itemTypeRepository.findAll()).thenReturn(itemTypes);
        List<ItemType> all = itemTypeService.findAll();
        assertSame(all, itemTypes);
        verify(itemTypeRepository, times(1)).findAll();
    }
}