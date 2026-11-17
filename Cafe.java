import java.time.LocalDate;

public class Cafe extends Lote {
    protected double humedad;

    public Cafe(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad, double humedad) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        if (humedad < 8.0 || humedad > 14.0) {
            throw new IllegalArgumentException("La humedad del café debe estar entre 8% y 14%.");
        }
        this.humedad = humedad;
    }

    @Override
    public double calcularPago() {
        double tarifaBase = 0.0;
        if (calidad == Calidad.A) tarifaBase = 12.0;
        else if (calidad == Calidad.B) tarifaBase = 9.0;
        else if (calidad == Calidad.C) tarifaBase = 6.0;

        double pagoTotal = pesoRecibido * tarifaBase;
        
        if (humedad > 12.0) {
            double exceso = humedad - 12.0;
            int puntos = (int) Math.ceil(exceso);
            double descuentoPorcentaje = puntos * 0.03;
            pagoTotal -= (pagoTotal * descuentoPorcentaje);
        }
        return pagoTotal;
    }

    public double getHumedad() { return humedad; }
}