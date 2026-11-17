import java.time.LocalDate;

public class QuesoFresco extends Lote implements Perecedero, Refrigerable {
    private double porcentajeGrasa;

    public QuesoFresco(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad, double porcentajeGrasa) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        if (calidad == Calidad.C) {
            throw new IllegalArgumentException("No se acepta queso fresco de calidad C.");
        }
        if (porcentajeGrasa < 10 || porcentajeGrasa > 40) {
            throw new IllegalArgumentException("El porcentaje de grasa debe estar entre 10% y 40%.");
        }
        this.porcentajeGrasa = porcentajeGrasa;
    }

    @Override
    public double calcularPago() {
        double tarifaBase = 0.0;
        if (calidad == Calidad.A) tarifaBase = 38.0;
        else if (calidad == Calidad.B) tarifaBase = 30.0;

        double pagoBruto = pesoRecibido * tarifaBase;
        double deduccionEmpaque = pesoRecibido * 1.50;
        return pagoBruto - deduccionEmpaque;
    }

    @Override
    public int getVidaUtilDias() {
        return 21;
    }

    @Override
    public LocalDate getFechaVencimiento() {
        return fechaIngreso.plusDays(getVidaUtilDias());
    }

    @Override
    public boolean requiereCamaraFria() {
        return true;
    }
}