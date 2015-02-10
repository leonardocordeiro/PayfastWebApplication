package br.com.caelum.payfast.oauth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AuthToken {
	@Id
	@GeneratedValue
	private Integer id;
	private String code;
	
	@SuppressWarnings("unused")
	private AuthToken() { 
	}
	
	public AuthToken(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
