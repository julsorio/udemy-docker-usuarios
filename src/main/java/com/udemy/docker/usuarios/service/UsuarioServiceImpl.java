package com.udemy.docker.usuarios.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udemy.docker.usuarios.clients.CursoClientRest;
import com.udemy.docker.usuarios.entity.Usuario;
import com.udemy.docker.usuarios.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	static Logger logger = Logger.getLogger(UsuarioServiceImpl.class.getName());

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private CursoClientRest cursoClientRest;

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> getAllUsuarios() {
		logger.info("start UsuarioServiceImpl getAllUsuarios");
		List<Usuario> resultado = usuarioRepository.findAll();		
		logger.info("end UsuarioServiceImpl getAllUsuarios");
		return resultado;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<Usuario> getUsuarioPorId(Long id) {
		logger.info("start UsuarioServiceImpl getUsuarioPorId");
		Optional<Usuario> opt = usuarioRepository.findById(id); 
		logger.info("end UsuarioServiceImpl getUsuarioPorId");
		return opt;
	}
	
	@Override
	@Transactional
	public Usuario saveUsuario(Usuario usuario) {
		logger.info("start UsuarioServiceImpl saveUsuario");
		Usuario usuarioSaved = usuarioRepository.save(usuario);
		logger.info("end UsuarioServiceImpl saveUsuario");
		return usuarioSaved;
	}
	
	@Override
	@Transactional
	public void deleteUsuario(Long id) {
		logger.info("start UsuarioServiceImpl deleteUsuario");
		usuarioRepository.deleteById(id);
		cursoClientRest.eliminarCursoUsuario(id);
		logger.info("end UsuarioServiceImpl deleteUsuario");
	}

	@Override
	@Transactional
	public Optional<Usuario> getUsuarioPorEmail(String email) {
		logger.info("start UsuarioServiceImpl getUsuarioPorEmail");
		Optional<Usuario> opt = usuarioRepository.findByEmail(email);
		logger.info("end UsuarioServiceImpl getUsuarioPorEmail");
		return opt;
	}

	@Override
	@Transactional
	public List<Usuario> getUsuariosPorIds(Iterable<Long> ids) {
		logger.info("start UsuarioServiceImpl getUsuariosPorIds");
		List<Usuario> listaUsuarios = usuarioRepository.findAllById(ids);
		logger.info("end UsuarioServiceImpl getUsuariosPorIds");
		
		return listaUsuarios;
	}
}
