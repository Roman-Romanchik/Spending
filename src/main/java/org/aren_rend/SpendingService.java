package org.aren_rend;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpendingService {
    private final SpendingRepositories spendingRepositories;

    @Transactional
    public String saveNote(LocalDate date, String category, String subCategory,
                         String name, int amount, double price) {
        SpendingEntity entity = SpendingEntity.builder()
                .date(date)
                .category(category)
                .subCategory(subCategory)
                .name(name)
                .amount(amount)
                .price(price)
                .totalPrice(price * amount)
                .build();
        spendingRepositories.save(entity);
        return entity.toString();
    }

    public List<String> displaySavedNotes() {
        List<SpendingEntity> dbNotes = spendingRepositories.findAll();
        return dbNotes.stream()
                .map(SpendingEntity::toString)
                .toList();
    }
}
