package com.udemy.docker.usuarios.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udemy.docker.usuarios.entity.Usuario;
import com.udemy.docker.usuarios.service.UsuarioService;

@RestController
@RequestMapping("api/v1")
public class UsuarioController {

	 @Autowired
	 private UsuarioService usuarioService;
	 
	 @GetMapping(path = "/usuarios")
	 public ResponseEntity<List<Usuario>> getAllUsuarios() {
		 return new ResponseEntity<List<Usuario>>(usuarioService.getAllUsuarios(),HttpStatus.OK);
	 }
	 
	 @GetMapping(path="/usuario/{id}")
	 public ResponseEntity<Usuario> getUsuarioPorId(@PathVariable(name = "id") Long id) {
		 Optional<Usuario> usuario = usuarioService.getUsuarioPorId(id);
		 
		 if(usuario.isEmpty()) {
			 return new ResponseEntity<Usuario>(HttpStatus.NOT_FOUND);
		 } else {
			 return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
		 }
		 
	 }
	 
	 @PostMapping(path="/usuario")
	 public ResponseEntity<Usuario> saveUsuario(@RequestBody Usuario usuario) {
		 Usuario usuarioGuardado = null;
		 
		 try {
			usuarioGuardado = usuarioService.saveUsuario(usuario);
			return new ResponseEntity<Usuario>(usuarioGuardado, HttpStatus.CREATED);
		} catch (Exception e) {
			System.out.println("error save usuario " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	 }
	 
	 @PutMapping(path = "/usuario/{id}")
	 public ResponseEntity<Usuario> updateUsuario(@PathVariable(name = "id") Long id, @RequestBody Usuario usuario) {
		 Optional<Usuario> usuarioGuardado = null;
		 
		 try {
			usuarioGuardado = usuarioService.getUsuarioPorId(id);
			if(usuarioGuardado.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<Usuario>(usuarioService.saveUsuario(usuario), HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println("error update usuario " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	 }
	 
	 @DeleteMapping(path = "/usuario/{id}")
	 public ResponseEntity<Void> deleteUsuario(@PathVariable(name = "id") Long id) {
		 Optional<Usuario> usuarioGuardado = null;
		 try {
			usuarioGuardado = usuarioService.getUsuarioPorId(id);
			if(usuarioGuardado.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				usuarioService.deleteUsuario(id);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			System.out.println("error delete usuario " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	 }
	 
}
