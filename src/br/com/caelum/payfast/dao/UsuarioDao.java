package br.com.caelum.payfast.dao;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.caelum.payfast.modelo.Usuario;

@Stateless
public class UsuarioDao {
	
	@PersistenceContext
	private EntityManager em;
	
	public boolean existe(Usuario usuario) {
		TypedQuery<Usuario> query = em.createQuery("select u from Usuario u where u.email = ? and u.senha = ?", 
												   Usuario.class);
		query.setParameter(1, usuario.getEmail());
		query.setParameter(2, usuario.getSenha());
		try { 
			Usuario u = query.getSingleResult();
			return u != null;
		} catch(NoResultException e) { 
			return false;
		}
	}

}
