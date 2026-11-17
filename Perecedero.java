import java.time.LocalDate;

public interface Perecedero {
    int getVidaUtilDias();
    LocalDate getFechaVencimiento();
    
    default boolean estaVencido(LocalDate fechaOperacion) {
        return fechaOperacion.isAfter(getFechaVencimiento());
    }
}