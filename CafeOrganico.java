import java.time.LocalDate;

public class CafeOrganico extends Cafe {
    private String certificadora;
    private LocalDate validezSello;

    public CafeOrganico(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad, double humedad, String certificadora, LocalDate validezSello) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad, humedad);
        if (validezSello == null || validezSello.isBefore(fechaIngreso)) {
            throw new IllegalArgumentException("El sello de certificación orgánica ya venció a la fecha de ingreso.");
        }
        this.certificadora = certificadora;
        this.validezSello = validezSello;
    }

    @Override
    public double calcularPago() {
        double pagoCafeBase = super.calcularPago();
        double bonificacion = pesoRecibido * 0.75;
        return pagoCafeBase + bonificacion;
    }
}