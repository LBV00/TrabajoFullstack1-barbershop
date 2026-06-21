package com.barbershop.user_service.dto;


import com.barbershop.user_service.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Datos de un usuario")
public class UserDTO {

    @Schema(description = "Identificador generado por el sistema", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "RUT del usuario", example = "12.345.678-5")
    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @Schema(description = "Nombre del usuario", example = "Camila")
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Soto")
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @Schema(description = "Correo electrónico del usuario", example = "camila.soto@example.com")
    @NotBlank(message = "El gmail es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String gmail;

    @Schema(description = "Teléfono de contacto", example = "+56912345678")
    private String telefono;

    public static UserDTO fromModel(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .rut(user.getRut())
                .nombre(user.getNombre())
                .apellido(user.getApellido())
                .gmail(user.getGmail())
                .telefono(user.getTelefono())
                .build();
    }

    public User toModel() {
        return User.builder()
                .id(this.id)
                .rut(this.rut)
                .nombre(this.nombre)
                .apellido(this.apellido)
                .gmail(this.gmail)
                .telefono(this.telefono)
                .build();
    }
}
