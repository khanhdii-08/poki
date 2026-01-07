package com.remake.poki.model;

import com.remake.poki.enums.ElementType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pet {

    @Id
    private Long id;

    private Long skillCardId;

    private String name;

    @Setter
    private String des;

    @Enumerated(EnumType.STRING)
    private ElementType elementType;

    private int maxLevel;

    private int parentId;
    private Long childId;
    private int no;

    // Flag đánh dấu pet huyền thoại
    private boolean flagLegend = false;

}