package com.bachoco.persistence.entity;

import java.time.LocalDateTime;

import com.bachoco.model.Otp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="otp")
@Table(name = "TC_OTP")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OtpEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="OTP_ID")
    private Integer id;

	@Column(name="USUARIO")
    private String usuario;
	@Column(name="CODIGO")
    private String codigo;
	@Column(name="EXPIRACION")
    private LocalDateTime expiracion;

}
