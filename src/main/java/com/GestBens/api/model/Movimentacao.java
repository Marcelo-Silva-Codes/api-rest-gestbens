package com.GestBens.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "movimentacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_movimentacao",nullable = false)
    private LocalDateTime dataMovimentacao;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "bem_id", nullable = false)
    private Bem bem;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "localidade_origem_id", nullable = false)
    private Localidade localidadeOrigem;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name= "localidade_destino_id", nullable = false)
    private Localidade localidadeDestino;

}
