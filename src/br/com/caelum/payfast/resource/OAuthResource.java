package br.com.caelum.payfast.resource;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import br.com.caelum.payfast.oauth.TokenDao;

@Path("/token")
public class OAuthResource {
	@Inject	private TokenDao dao;
	
	@GET
	@Path("/accesstoken/{code}")
	public Response existsAccessToken(@PathParam("code") String accessToken) { 
		if(dao.existsAccessToken(accessToken)) {
			return buildOkResponse();
		} else {
			return unauthorizedResponse();
		}
	}
	
	private Response buildOkResponse() {
		return Response.ok().build();
	}

	private Response unauthorizedResponse() {
		return Response.status(HttpServletResponse.SC_UNAUTHORIZED).build();
	}
}
