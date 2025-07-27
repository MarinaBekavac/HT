package mb.projects.ht.service;

import lombok.RequiredArgsConstructor;
import mb.projects.ht.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
