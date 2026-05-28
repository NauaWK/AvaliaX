
package dev.trabalho.xfragil.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "sintomas")
public class Symptom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sintoma")
    private Integer id;  
    
    @Column(name = "descricao", nullable = false, unique = true)
    private String name;
    
    @Column(name = "peso_masculino", precision = 5, scale = 2, nullable = false)
    private BigDecimal manScore;
    
    @Column(name = "peso_feminino", precision = 5, scale = 2, nullable = false)
    private BigDecimal womanScore;

    public Symptom() {}

    public Symptom(String name, BigDecimal manScore, BigDecimal womanScore) {
        this.name = name;
        this.manScore = manScore;
        this.womanScore = womanScore;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getManScore() {
        return manScore;
    }

    public void setManScore(BigDecimal manScore) {
        this.manScore = manScore;
    }

    public BigDecimal getWomanScore() {
        return womanScore;
    }

    public void setWomanScore(BigDecimal womanScore) {
        this.womanScore = womanScore;
    }
    
}
