package com.barbershop.user_service.controller;

import com.barbershop.user_service.dto.UserDTO;
import com.barbershop.user_service.exception.ApiErrorResponse;
import com.barbershop.user_service.model.User;
import com.barbershop.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Operaciones para administrar usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene todos los usuarios registrados.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados", content = @Content)
    })
    public ResponseEntity<List<UserDTO>> getAll() {
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        List<UserDTO> dtos = users.stream().map(UserDTO::fromModel).collect(Collectors.toList());
        return ResponseEntity.ok(dtos); // 200
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    public ResponseEntity<UserDTO> getById(
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(UserDTO.fromModel(user));
        }
        return ResponseEntity.notFound().build(); // 404
    }

    @PostMapping
    @Operation(summary = "Crear usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDTO) {
        User savedUser = userService.save(userDTO.toModel());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.fromModel(savedUser)); // 201
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    public ResponseEntity<UserDTO> update(
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO) {
        if (userService.findById(id) != null) {
            userDTO.setId(id); 
            User updatedUser = userService.save(userDTO.toModel());
            return ResponseEntity.ok(UserDTO.fromModel(updatedUser));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long id) {
        if (userService.findById(id) != null) {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/exists")
    @Operation(summary = "Comprobar si existe un usuario")
    @ApiResponse(responseCode = "200", description = "Resultado de la comprobación",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    public ResponseEntity<Boolean> existsUser(
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.existsById(id));
    }
}
