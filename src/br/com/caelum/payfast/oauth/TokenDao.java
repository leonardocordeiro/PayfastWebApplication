package br.com.caelum.payfast.oauth;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class TokenDao {
	@PersistenceContext	private EntityManager em;
	
	public void addAuthToken(String authToken) { 
		AuthToken token = new AuthToken(authToken);
		em.persist(token);
	}
	
	public void addAccessToken(String accessToken) { 
		AccessToken token = new AccessToken(accessToken);
		em.persist(token);
	}
	
	public boolean existsAuthToken(String token) { 
		TypedQuery<AuthToken> query = em.createQuery("select a from AuthToken a where a.code = ?", AuthToken.class);
		query.setParameter(1, token);
		
		try {
			return query.getSingleResult() != null; 
		} catch(NoResultException e) {
			return false;
		}
	}
	
	public boolean existsAccessToken(String token) { 
		TypedQuery<AccessToken> query = em.createQuery("select a from AccessToken a where a.code = ?", AccessToken.class);
		query.setParameter(1, token);
		
		try {
			return query.getSingleResult() != null; 
		} catch(NoResultException e) {
			return false;
		}
	}

}
