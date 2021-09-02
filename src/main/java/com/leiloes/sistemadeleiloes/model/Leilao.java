package com.leiloes.sistemadeleiloes.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="leiloes_tb")
public class Leilao {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String item;

    private String status;

    private BigDecimal valorLanceMinimo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dataExpiracao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lance_id", referencedColumnName = "id")
    private Lance lance;

    public Leilao(String item, String status, BigDecimal valorLanceMinimo, Date dataExpiracao) {
        this.item = item;
        this.status = status;
        this.valorLanceMinimo = valorLanceMinimo;
        this.dataExpiracao = dataExpiracao;
    }

    public Leilao (){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getValorLanceMinimo() {
        return valorLanceMinimo;
    }

    public void setValorLanceMinimo(BigDecimal valorLanceMinimo) {
        this.valorLanceMinimo = valorLanceMinimo;
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public Lance getLance() {
        return lance;
    }

    public void setLance(Lance lance) {
        this.lance = lance;
    }
}
