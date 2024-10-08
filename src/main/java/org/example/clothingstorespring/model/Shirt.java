package org.example.clothingstorespring.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Shirt")
public class Shirt extends ClothingItem {

    @Enumerated(EnumType.STRING)
    private SleeveType sleeveType;

    @Enumerated(EnumType.STRING)
    private ShirtSize size;
}