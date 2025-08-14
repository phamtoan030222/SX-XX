package com.sd20201.datn.entity;

import com.sd20201.datn.entity.base.PrimaryEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "screen")
public class Screen extends PrimaryEntity implements Serializable {

    private Float physicalSize;

    private String technology;

    private String material;

    @ManyToOne
    @JoinColumn(name = "id_screen_resolution", referencedColumnName = "id")
    private ScreenResolution resolution;

}
