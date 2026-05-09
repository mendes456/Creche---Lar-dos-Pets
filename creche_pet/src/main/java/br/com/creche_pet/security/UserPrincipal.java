package br.com.creche_pet.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class UserPrincipal extends User {
    private final String nomeCompleto;

    public UserPrincipal(String email, String senha, String nomeCompleto, Collection<? extends GrantedAuthority> authorities) {
        super(email, senha, authorities);
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }
}