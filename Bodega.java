import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bodega {
    private String codigo;
    private double capacidadMaxima;
    private boolean camaraFria;
    private List<Lote> lotes;

    public Bodega(String codigo, double capacidadMaxima, boolean camaraFria) {
        this.codigo = codigo;
        this.capacidadMaxima = capacidadMaxima;
        this.camaraFria = camaraFria;
        this.lotes = new ArrayList<>();
    }

    public double getPesoActual() {
        double suma = 0;
        for (int i = 0; i < lotes.size(); i++) {
            Lote l = lotes.get(i);
            if (l.getEstado() == EstadoLote.DISPONIBLE) {
                suma += l.getPesoDisponible();
            }
        }
        return suma;
    }

    public boolean puedeAlmacenar(Lote lote) {
        boolean requiereFrio = (lote instanceof Refrigerable) && ((Refrigerable) lote).requiereCamaraFria();
        if (requiereFrio && !this.camaraFria) return false;
        if (!requiereFrio && this.camaraFria) return false;

        if (getPesoActual() + lote.getPesoDisponible() > capacidadMaxima) return false;
        return true;
    }

    public void agregarLote(Lote lote) {
        if (!puedeAlmacenar(lote)) {
            throw new IllegalArgumentException("La bodega " + codigo + " no cumple capacidad o requisitos térmicos.");
        }
        lotes.add(lote);
    }

    public List<Lote> getLotesOrdenados() {
        List<Lote> disponibles = new ArrayList<>();
        for (int i = 0; i < lotes.size(); i++) {
            Lote l = lotes.get(i);
            if (l.getEstado() == EstadoLote.DISPONIBLE) {
                disponibles.add(l);
            }
        }
        Collections.sort(disponibles);
        return disponibles;
    }

    public double descartarVencidos(LocalDate fechaOperacion) {
        double perdida = 0;
        for (int i = 0; i < lotes.size(); i++) {
            Lote l = lotes.get(i);
            if (l.getEstado() == EstadoLote.DISPONIBLE && l.esPerecedero()) {
                Perecedero p = (Perecedero) l;
                if (p.estaVencido(fechaOperacion)) {
                    l.setEstado(EstadoLote.DESCARTADO);
                    perdida += (l.getPesoDisponible() * l.getCostoPorKg());
                    l.setPesoDisponible(0);
                }
            }
        }
        
        for (int i = lotes.size() - 1; i >= 0; i--) {
            if (lotes.get(i).getEstado() != EstadoLote.DISPONIBLE) {
                lotes.remove(i);
            }
        }
        return perdida;
    }

    public String getCodigo() { return codigo; }
    public double getCapacidadMaxima() { return capacidadMaxima; }
    public boolean isCamaraFria() { return camaraFria; }
}