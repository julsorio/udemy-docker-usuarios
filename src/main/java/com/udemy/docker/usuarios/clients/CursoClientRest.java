package com.udemy.docker.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cursos", url = "localhost:8002/api/v1/")
public interface CursoClientRest {
	
	@DeleteMapping(path = "/deleteCursoUsuario/{cursoId}")
	public void eliminarCursoUsuario(@PathVariable Long cursoId);
}
