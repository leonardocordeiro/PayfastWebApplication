package br.com.caelum.payfast.oauth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AccessToken {
	@Id
	@GeneratedValue
	private Integer id;
	private String code;

	@SuppressWarnings("unused")
	private AccessToken() {
	}
	
	public AccessToken(String accessToken) {
		this.code = accessToken;
	}

	public String getCode() {
		return code;
	}

}
