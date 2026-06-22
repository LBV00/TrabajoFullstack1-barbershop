package com.barbershop.user_service.controller;

import com.barbershop.user_service.assembler.UserModelAssembler;
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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/users")
@Tag(name = "Usuarios", description = "Operaciones para administrar usuarios")
public class UserController {

   private final UserService userService;
   private final UserModelAssembler assembler;


    public UserController(UserService userService,
        UserModelAssembler assembler) {
                this.userService = userService;
                this.assembler = assembler;
        }
    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
        })
        public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> getAll() {

    List<EntityModel<UserDTO>> users = userService.findAll()
            .stream()
            .map(user -> EntityModel.of(
                    UserDTO.fromModel(user),

                    linkTo(methodOn(UserController.class)
                            .getById(user.getId()))
                            .withSelfRel()
            ))
            .toList();

    CollectionModel<EntityModel<UserDTO>> collection =
            CollectionModel.of(
                    users,
                    linkTo(methodOn(UserController.class)
                            .getAll())
                            .withSelfRel()
            );

    return ResponseEntity.ok(collection);
        }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
        })
        public ResponseEntity<EntityModel<UserDTO>> getById(
        @PathVariable Long id) {

        User user = userService.findById(id);

        if (user == null) {
        return ResponseEntity.notFound().build();
        }

        EntityModel<UserDTO> model = EntityModel.of(
            UserDTO.fromModel(user),

            linkTo(methodOn(UserController.class)
                    .getById(id))
                    .withSelfRel(),

            linkTo(methodOn(UserController.class)
                    .getAll())
                    .withRel("usuarios")
    );

        return ResponseEntity.ok(model);
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
