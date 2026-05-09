package br.com.creche_pet.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class UserDto {

    private Long idUser;

    @NotEmpty(message = "Nome não pode estar vazio")
    private String nome;

    @NotEmpty(message = "Email não pode estar vazio")
    @Email
    private String email;

    @NotEmpty(message = "Senha não pode estar vazia")
    private String senha;
    
    
    
    private boolean aprovado;
    
    


  
	public boolean isAprovado() {
		return aprovado;
	}

	public void setAprovado(boolean aprovado) {
		this.aprovado = aprovado;
	}

	// getters e setters
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
