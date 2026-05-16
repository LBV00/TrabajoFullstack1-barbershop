package com.barbershop.user_service.dto;


import com.barbershop.user_service.model.User;
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
public class UserDTO {

    private Long id;
    
    @NotBlank(message = "El RUT es obligatorio")
    private String rut;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;
    
    @NotBlank(message = "El gmail es obligatorio")
    @Email(message = "Formato de correo inválido")
    private String gmail;
    
    private String telefono;

    // Convertir de Entidad a DTO
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

    // Convertir de DTO a Entidad
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