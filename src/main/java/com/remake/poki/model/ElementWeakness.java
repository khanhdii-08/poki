package com.remake.poki.model;

import com.remake.poki.enums.ElementType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "element_weakness")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ElementWeakness {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ElementType element;

    @Enumerated(EnumType.STRING)
    private ElementType weakAgainst;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ElementType getElement() {
        return element;
    }

    public void setElement(ElementType element) {
        this.element = element;
    }

    public ElementType getWeakAgainst() {
        return weakAgainst;
    }

    public void setWeakAgainst(ElementType weakAgainst) {
        this.weakAgainst = weakAgainst;
    }
}