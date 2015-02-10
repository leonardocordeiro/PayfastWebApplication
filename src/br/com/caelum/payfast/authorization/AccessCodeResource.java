package br.com.caelum.payfast.authorization;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import br.com.caelum.payfast.dao.AplicacaoDao;
import br.com.caelum.payfast.modelo.Aplicacao;
import br.com.caelum.payfast.oauth.TokenDao;

@Path("/access")
public class AccessCodeResource {
    @Inject private TokenDao tokens;
    @Inject private AplicacaoDao aplicacaoDao;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response returnAccessToken(@Context HttpServletRequest request) {
		String authorizationToken = request.getParameter("code");
		String clientId = request.getParameter("client_id");
		String clientSecret = request.getParameter("client_secret");
		
		try {
			if(existsAuthToken(authorizationToken) && isValidClientIdAndSecret(clientId, clientSecret)) {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
				
				String accessToken = oauthIssuerImpl.accessToken();
				tokens.addAccessToken(accessToken);
				
				return buildOkMessage(accessToken);
			}
			return buildErrorResponse();
		} catch (OAuthSystemException e) {
			throw new RuntimeException(e);
		}
    }

	private Response buildOkMessage(String accessToken) throws OAuthSystemException {
		OAuthResponse response = OAuthASResponse
				.tokenResponse(HttpServletResponse.SC_OK)
				.setAccessToken(accessToken)
				.buildJSONMessage();
		
		return buildJAXRSResponse(response);
	}

	private Response buildErrorResponse() throws OAuthSystemException {
		OAuthResponse response = OAuthASResponse
				.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
				.setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
				.buildJSONMessage();
		
		return buildJAXRSResponse(response);
	}
	
	private Response buildJAXRSResponse(OAuthResponse response) {
		return Response
				.status(response.getResponseStatus())
				.entity(response.getBody())
				.build();
	}
	
	private boolean existsAuthToken(String authToken) { 
		return this.tokens.existsAuthToken(authToken);
	}
	
	private boolean isValidClientIdAndSecret(String clientId, String clientSecret) { 
		return this.aplicacaoDao.existeAplicacao(new Aplicacao(clientId, clientSecret));
	}
}
