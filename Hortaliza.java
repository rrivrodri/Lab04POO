import java.time.LocalDate;

public class Hortaliza extends Lote implements Perecedero, Refrigerable {
    private int vidaUtilDias;

    public Hortaliza(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad, int vidaUtilDias) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        if (vidaUtilDias < 3 || vidaUtilDias > 14) {
            throw new IllegalArgumentException("La vida útil de la hortaliza debe estar entre 3 y 14 días.");
        }
        this.vidaUtilDias = vidaUtilDias;
    }

    @Override
    public double calcularPago() {
        double tarifaBase = 0.0;
        if (calidad == Calidad.A) tarifaBase = 4.0;
        else if (calidad == Calidad.B) tarifaBase = 3.0;
        else if (calidad == Calidad.C) tarifaBase = 2.0;

        double pesoPago = pesoRecibido * 0.95;
        return pesoPago * tarifaBase;
    }

    @Override
    public int getVidaUtilDias() {
        return vidaUtilDias;
    }

    @Override
    public LocalDate getFechaVencimiento() {
        return fechaIngreso.plusDays(vidaUtilDias);
    }

    @Override
    public boolean requiereCamaraFria() {
        return true;
    }
}