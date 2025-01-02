package com.udemy.docker.usuarios.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udemy.docker.usuarios.entity.Usuario;
import com.udemy.docker.usuarios.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1")
public class UsuarioController {
	static Logger logger = Logger.getLogger(UsuarioController.class.getName());

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping(path = "/usuarios")
	public ResponseEntity<List<Usuario>> getAllUsuarios() {
		logger.info("start UsuarioController getAllUsuarios");
		
		ResponseEntity<List<Usuario>> respuesta = null;
		List<Usuario> listaUsuarios = usuarioService.getAllUsuarios();
		
		respuesta = new ResponseEntity<List<Usuario>>(listaUsuarios, HttpStatus.OK);
		
		logger.info("end UsuarioController getAllUsuarios");
		
		return respuesta;
	}

	@GetMapping(path = "/usuario/{id}")
	public ResponseEntity<Usuario> getUsuarioPorId(@PathVariable(name = "id") Long id) {
		logger.info("start UsuarioController getUsuarioPorId");
		
		Optional<Usuario> usuario = usuarioService.getUsuarioPorId(id);
		ResponseEntity<Usuario> respuesta = null;

		if (usuario.isEmpty()) {
			respuesta = new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
		} else {
			respuesta = new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
		}

		logger.info("end UsuarioController getUsuarioPorId");
		
		return respuesta;
	}

	@PostMapping(path = "/usuario")
	public ResponseEntity<?> saveUsuario(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {
		logger.info("start UsuarioController saveUsuario");
		
		ResponseEntity<?> respuesta = null;

		if (bindingResult.hasErrors()) {
			return validar(bindingResult);

		}
		
		if(!usuario.getEmail().isEmpty() && usuarioService.getUsuarioPorEmail(usuario.getEmail()).isPresent()) {
			respuesta = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		Usuario usuarioGuardado = null;

		try {
			usuarioGuardado = usuarioService.saveUsuario(usuario);
			respuesta = new ResponseEntity<Usuario>(usuarioGuardado, HttpStatus.CREATED);
		} catch (Exception e) {
			logger.info("error save usuario " + e.getMessage());
			respuesta = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		logger.info("end UsuarioController saveUsuario");

		return respuesta;
	}

	@PutMapping(path = "/usuario/{id}")
	public ResponseEntity<?> updateUsuario(@PathVariable(name = "id") Long id, @Valid @RequestBody Usuario usuario, BindingResult bindingResult) {
		logger.info("start UsuarioController updateUsuario");
		
		ResponseEntity<?> respuesta = null;
		
		if (bindingResult.hasErrors()) {
			return validar(bindingResult);
		}
		
		Optional<Usuario> usuarioOptional = null;
		Usuario usuarioDB = null;

		try {
			usuarioOptional = usuarioService.getUsuarioPorId(id);
			if (usuarioOptional.isEmpty()) {
				respuesta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				usuarioDB = usuarioOptional.get();
				
				if(usuario.getEmail().isEmpty() || usuario.getEmail().equalsIgnoreCase(usuarioDB.getEmail()) || usuarioService.getUsuarioPorEmail(usuario.getEmail()).isPresent()) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				
				usuarioDB.setEmail(usuario.getEmail());
				usuarioDB.setNombre(usuario.getNombre());
				usuarioDB.setPassword(usuario.getPassword());
				
				respuesta = new ResponseEntity<Usuario>(usuarioService.saveUsuario(usuarioDB), HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("error update usuario " + e.getMessage());
			respuesta = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		logger.info("end UsuarioController updateUsuario");
		
		return respuesta;
	}

	@DeleteMapping(path = "/usuario/{id}")
	public ResponseEntity<?> deleteUsuario(@PathVariable(name = "id") Long id) {
		logger.info("start UsuarioController deleteUsuario");
		
		ResponseEntity<?> respuesta = null;
		
		Optional<Usuario> usuarioGuardado = null;
		try {
			usuarioGuardado = usuarioService.getUsuarioPorId(id);
			if (usuarioGuardado.isEmpty()) {
				respuesta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				usuarioService.deleteUsuario(id);
				respuesta = new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("error delete usuario " + e.getMessage());
			respuesta = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		logger.info("end UsuarioController deleteUsuario");
		
		return respuesta;
	}
	
	@GetMapping(path =  "/usuariosCurso")
	public ResponseEntity<?> getUsuariosCurso(@RequestParam List<Long> ids) {
		logger.info("start UsuarioController deleteUsuario");
		
		ResponseEntity<?> respuesta = null;
		List<Usuario> listaUsuarios = null;
		
		listaUsuarios = usuarioService.getUsuariosPorIds(ids);
		respuesta = new ResponseEntity<>(listaUsuarios, HttpStatus.OK);
		
		logger.info("start UsuarioController deleteUsuario");
		
		return respuesta;
		
	}

	private ResponseEntity<Map<String, String>> validar(BindingResult bindingResult) {
		Map<String, String> mapaErrores = new HashMap<>();
		bindingResult.getFieldErrors().forEach(error -> {
			mapaErrores.put(error.getField(),
					"Error en el campo " + error.getField() + " : " + error.getDefaultMessage());
		});

		return new ResponseEntity<Map<String, String>>(mapaErrores, HttpStatus.BAD_REQUEST);
	}

}
