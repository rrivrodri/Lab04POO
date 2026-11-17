import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cooperativa {
    private LocalDate fechaOperacion;
    private List<Bodega> bodegas;
    private List<Lote> todosLosLotes;
    
    private double totalPagado;
    private double totalCobrado;
    private double totalPerdidas;

    public Cooperativa(LocalDate fechaInicial) {
        this.fechaOperacion = fechaInicial;
        this.bodegas = new ArrayList<>();
        this.todosLosLotes = new ArrayList<>();
        this.totalPagado = 0;
        this.totalCobrado = 0;
        this.totalPerdidas = 0;
    }

    public void registrarBodega(Bodega b) {
        bodegas.add(b);
    }

    private void validarCodigoUnico(String codigoLote) {
        for (int i = 0; i < todosLosLotes.size(); i++) {
            if (todosLosLotes.get(i).getCodigo().equalsIgnoreCase(codigoLote)) {
                throw new IllegalArgumentException("Ya existe un lote registrado con el código: " + codigoLote);
            }
        }
    }

    public void recibirLote(Lote lote, String codigoBodega) {
        validarCodigoUnico(lote.getCodigo());
        Bodega bodegaDestino = buscarBodega(codigoBodega);

        bodegaDestino.agregarLote(lote);
        todosLosLotes.add(lote);
        totalPagado += lote.calcularPago();
    }

    public void cargarLoteInicial(Lote lote, String codigoBodega) {
        validarCodigoUnico(lote.getCodigo());
        Bodega bodegaDestino = buscarBodega(codigoBodega);

        bodegaDestino.agregarLote(lote);
        todosLosLotes.add(lote);
    }

    private Bodega buscarBodega(String codigoBodega) {
        for (int i = 0; i < bodegas.size(); i++) {
            if (bodegas.get(i).getCodigo().equalsIgnoreCase(codigoBodega)) {
                return bodegas.get(i);
            }
        }
        throw new IllegalArgumentException("Bodega no encontrada.");
    }

    public void avanzarFecha(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los días a avanzar deben ser positivos.");
        this.fechaOperacion = this.fechaOperacion.plusDays(dias);
        for (int i = 0; i < bodegas.size(); i++) {
            totalPerdidas += bodegas.get(i).descartarVencidos(this.fechaOperacion);
        }
    }

    public void despacharPedido(String tipoCategoriaStr, double kilosSolicitados) {
        if (kilosSolicitados <= 0) throw new IllegalArgumentException("La cantidad solicitada debe ser mayor a cero.");
        List<Lote> candidatos = new ArrayList<>();
        
        for (int i = 0; i < bodegas.size(); i++) {
            List<Lote> lotesB = bodegas.get(i).getLotesOrdenados();
            for (int j = 0; j < lotesB.size(); j++) {
                Lote l = lotesB.get(j);
                boolean coincide = false;
                
                if (tipoCategoriaStr.equalsIgnoreCase("Cafe")) {
                    coincide = (l instanceof Cafe);
                } else if (tipoCategoriaStr.equalsIgnoreCase("CafeOrganico")) {
                    coincide = (l instanceof CafeOrganico);
                } else {
                    coincide = l.getClass().getSimpleName().equalsIgnoreCase(tipoCategoriaStr);
                }

                if (coincide) {
                    candidatos.add(l);
                }
            }
        }
        
        Collections.sort(candidatos);

        double pesoDisponibleTotal = 0;
        for (int i = 0; i < candidatos.size(); i++) {
            pesoDisponibleTotal += candidatos.get(i).getPesoDisponible();
        }

        if (pesoDisponibleTotal < kilosSolicitados) {
            throw new IllegalArgumentException("Pedido rechazado: No hay suficientes existencias.");
        }

        double kilosRestantes = kilosSolicitados;
        double costoTotalDespacho = 0;

        for (int i = 0; i < candidatos.size(); i++) {
            if (kilosRestantes <= 0) break;
            Lote l = candidatos.get(i);
            double aTomar = Math.min(l.getPesoDisponible(), kilosRestantes);
            
            costoTotalDespacho += (aTomar * l.getCostoPorKg());
            l.setPesoDisponible(l.getPesoDisponible() - aTomar);
            kilosRestantes -= aTomar;
        }

        totalCobrado += (costoTotalDespacho * 1.35);
    }

    public LocalDate getFechaOperacion() { return fechaOperacion; }
    public List<Bodega> getBodegas() { return bodegas; }
    public double getTotalPagado() { return totalPagado; }
    public double getTotalCobrado() { return totalCobrado; }
    public double getTotalPerdidas() { return totalPerdidas; }
}