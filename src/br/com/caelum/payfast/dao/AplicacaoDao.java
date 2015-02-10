package br.com.caelum.payfast.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.caelum.payfast.modelo.Aplicacao;

@Stateless
public class AplicacaoDao {
	@PersistenceContext private EntityManager em;
	
	public void inserir(Aplicacao aplicacao) { 
		em.persist(aplicacao);
	}
	
	public List<Aplicacao> getAplicacoes() { 
		return em.createQuery("from Aplicacao", Aplicacao.class).getResultList();
	}
	
	public boolean existeClientId(Aplicacao aplicacao) {
		String jpql = "select a from Aplicacao a where a.clientId= ?";
		
		TypedQuery<Aplicacao> query = em.createQuery(jpql, Aplicacao.class);
		query.setParameter(1, aplicacao.getClientId());
		
		try { 
			Aplicacao result = query.getSingleResult();
			return result != null;
		} catch(NoResultException e) { 
			return false;
		}
	}
	
	public boolean existeAplicacao(Aplicacao aplicacao) {
		String jpql = "select a from Aplicacao a where a.clientId= ? and a.clientSecret= ?";
		
		TypedQuery<Aplicacao> query = em.createQuery(jpql, Aplicacao.class);
		query.setParameter(1, aplicacao.getClientId());
		query.setParameter(2, aplicacao.getClientSecret());
		
		try { 
			Aplicacao result = query.getSingleResult();
			return result != null;
		} catch(NoResultException e) { 
			return false;
		}
	}

}
