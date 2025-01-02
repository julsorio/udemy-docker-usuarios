package com.udemy.docker.usuarios.service;

import java.util.List;
import java.util.Optional;

import com.udemy.docker.usuarios.entity.Usuario;

public interface UsuarioService {

	public List<Usuario> getAllUsuarios();
	
	public Optional<Usuario> getUsuarioPorId(Long id);
	
	public Usuario saveUsuario(Usuario usuario);
	
	public void deleteUsuario(Long id);
	
	public Optional<Usuario> getUsuarioPorEmail(String email);
	
	public List<Usuario> getUsuariosPorIds(Iterable<Long> ids);
}
