package com.example.retailstore.service.item.type;

import com.example.retailstore.entity.ItemType;
import com.example.retailstore.repository.ItemTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemTypeServiceImpl implements ItemTypeService{

    private final ItemTypeRepository itemTypeRepository;

    /**
     * Retrieves all item types.
     *
     * @return A list of all item types available in the system.
     */
    @Override
    public List<ItemType> findAll() {
        return itemTypeRepository.findAll();
    }

}
