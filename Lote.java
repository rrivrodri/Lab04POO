import java.time.LocalDate;

public abstract class Lote implements Comparable<Lote> {
    protected String codigo;
    protected String productor;
    protected LocalDate fechaIngreso;
    protected double pesoRecibido;
    protected double pesoDisponible;
    protected Calidad calidad;
    protected EstadoLote estado;

    public Lote(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad) {
        if (codigo == null || codigo.trim().isEmpty()) throw new IllegalArgumentException("El código no puede estar vacío.");
        if (productor == null || productor.trim().isEmpty()) throw new IllegalArgumentException("El productor no puede estar vacío.");
        if (pesoRecibido <= 0) throw new IllegalArgumentException("El peso recibido debe ser mayor a cero.");
        if (fechaIngreso == null) throw new IllegalArgumentException("La fecha de ingreso es inválida.");
        
        this.codigo = codigo;
        this.productor = productor;
        this.fechaIngreso = fechaIngreso;
        this.pesoRecibido = pesoRecibido;
        this.pesoDisponible = pesoRecibido;
        this.calidad = calidad;
        this.estado = EstadoLote.DISPONIBLE;
    }

    public abstract double calcularPago();
    
    public double getCostoPorKg() {
        return calcularPago() / pesoRecibido;
    }

    public boolean esPerecedero() {
        return this instanceof Perecedero;
    }

    @Override
    public int compareTo(Lote otro) {
        boolean estePerecedero = this.esPerecedero();
        boolean otroPerecedero = otro.esPerecedero();
        
        if (estePerecedero && !otroPerecedero) return -1;
        if (!estePerecedero && otroPerecedero) return 1;

        if (estePerecedero && otroPerecedero) {
            LocalDate v1 = ((Perecedero) this).getFechaVencimiento();
            LocalDate v2 = ((Perecedero) otro).getFechaVencimiento();
            int cmpVenc = v1.compareTo(v2);
            if (cmpVenc != 0) return cmpVenc;
        } else {
            int cmpIng = this.fechaIngreso.compareTo(otro.fechaIngreso);
            if (cmpIng != 0) return cmpIng;
        }

        int cmpCalidad = this.calidad.compareTo(otro.calidad);
        if (cmpCalidad != 0) return cmpCalidad;

        return this.codigo.compareTo(otro.codigo);
    }

    public String getCodigo() { return codigo; }
    public String getProductor() { return productor; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public double getPesoRecibido() { return pesoRecibido; }
    public double getPesoDisponible() { return pesoDisponible; }
    
    public void setPesoDisponible(double pesoDisponible) {
        this.pesoDisponible = pesoDisponible;
        if (this.pesoDisponible <= 0) {
            this.pesoDisponible = 0;
            if (this.estado != EstadoLote.DESCARTADO) {
                this.estado = EstadoLote.AGOTADO;
            }
        }
    }
    
    public Calidad getCalidad() { return calidad; }
    public EstadoLote getEstado() { return estado; }
    public void setEstado(EstadoLote estado) { this.estado = estado; }
}