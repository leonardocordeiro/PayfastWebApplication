package br.com.caelum.payfast.bean;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import br.com.caelum.payfast.dao.AplicacaoDao;
import br.com.caelum.payfast.dao.UsuarioDao;
import br.com.caelum.payfast.modelo.Aplicacao;
import br.com.caelum.payfast.modelo.Usuario;
import br.com.caelum.payfast.oauth.TokenDao;

@Named
@RequestScoped
public class LoginBean {
	@Inject	private AplicacaoDao aplicacaoDao;
	@Inject	private UsuarioDao usuarioDao;
	@Inject	private TokenDao tokens;
	
	private Usuario usuario = new Usuario();
	private Aplicacao aplicacao = new Aplicacao();
	private String callbackUrl;
	
	private boolean login;
	private HttpServletResponse httpResponse;
	private HttpServletRequest httpRequest;
	
	@PostConstruct
	public void setup() { 
		httpResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	}
	
	public void verificaCredenciais() {
		if(aplicacaoDao.existeClientId(aplicacao)) 
			login = true;
	}

	public String login() {
		if (usuarioDao.existe(usuario)) {
			try {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				
				String authorizationToken = oauthIssuerImpl.authorizationCode();
				tokens.addAuthToken(authorizationToken);
				
				OAuthResponse response = buildOkResponse(authorizationToken);

				httpResponse.sendRedirect(response.getLocationUri());
			} catch (OAuthSystemException | IOException e) {
				throw new RuntimeException(e);
			}
		}

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Email ou Senha inválidos!"));
		return null;
	}

	private OAuthResponse buildOkResponse(String authorizationToken) throws OAuthSystemException {
		OAuthResponse response = OAuthASResponse
				.authorizationResponse(httpRequest, HttpServletResponse.SC_FOUND)
				.setCode(authorizationToken)
		        .location(callbackUrl)
		        .buildQueryMessage();
		
		return response;
	}
	
	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Aplicacao getAplicacao() {
		return aplicacao;
	}

	public void setAplicacao(Aplicacao aplicacao) {
		this.aplicacao = aplicacao;
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}
}
