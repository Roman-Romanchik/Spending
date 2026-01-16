package org.aren_rend;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "notes")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpendingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String category;
    private String subCategory;
    private String name;
    private int amount;
    private double price;
    private double totalPrice;

    @Override
    public String toString() {
        return category + " : " + subCategory + " : " + name +  " : " + amount + " : " + price + " : " + totalPrice;
    }
}
