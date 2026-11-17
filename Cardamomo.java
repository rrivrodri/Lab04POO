import java.time.LocalDate;

public class Cardamomo extends Lote implements Perecedero {
    private double porcentajeGranoVerde;

    public Cardamomo(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad, double porcentajeGranoVerde) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        if (porcentajeGranoVerde < 0 || porcentajeGranoVerde > 100) {
            throw new IllegalArgumentException("El porcentaje de grano verde debe estar entre 0 y 100.");
        }
        this.porcentajeGranoVerde = porcentajeGranoVerde;
    }

    @Override
    public double calcularPago() {
        double tarifaBase = 0.0;
        if (calidad == Calidad.A) tarifaBase = 60.0;
        else if (calidad == Calidad.B) tarifaBase = 48.0;
        else if (calidad == Calidad.C) tarifaBase = 35.0;

        double pago = pesoRecibido * tarifaBase;
        if (porcentajeGranoVerde >= 85.0) {
            pago *= 1.10;
        }
        return pago;
    }

    @Override
    public int getVidaUtilDias() {
        return 180;
    }

    @Override
    public LocalDate getFechaVencimiento() {
        return fechaIngreso.plusDays(getVidaUtilDias());
    }
}