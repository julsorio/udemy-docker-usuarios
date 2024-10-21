package com.udemy.docker.usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udemy.docker.usuarios.entity.Usuario;
import com.udemy.docker.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> getAllUsuarios() {
		return usuarioRepository.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> getUsuarioPorId(Long id) {
		return usuarioRepository.findById(id);
	}
	
	@Override
	@Transactional
	public Usuario saveUsuario(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
	
	@Override
	@Transactional
	public void deleteUsuario(Long id) {
		usuarioRepository.deleteById(id);
	}
}
