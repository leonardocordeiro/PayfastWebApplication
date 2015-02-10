package br.com.caelum.payfast.bean;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.caelum.payfast.dao.AplicacaoDao;
import br.com.caelum.payfast.modelo.Aplicacao;

@Named
@RequestScoped
public class CadastrarAplicacaoBean {

	@Inject private AplicacaoDao aplicacaoDao;
	private Aplicacao aplicacao = new Aplicacao();

	public List<Aplicacao> getAplicacoes() {
		return aplicacaoDao.getAplicacoes();
	}

	public void cadastrar() {
		aplicacao.setClientId(Long.toString(System.currentTimeMillis()));
		aplicacao.setClientSecret(Double.toString(Math.random()));
		
		aplicacaoDao.inserir(aplicacao);
	}

	public Aplicacao getAplicacao() {
		return aplicacao;
	}

	public void setAplicacao(Aplicacao aplicacao) {
		this.aplicacao = aplicacao;
	}
}
