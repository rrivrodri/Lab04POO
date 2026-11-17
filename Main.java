import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LocalDate fechaInicio = LocalDate.of(2026, 3, 5);
        Cooperativa cooperativa = new Cooperativa(fechaInicio);

        cooperativa.registrarBodega(new Bodega("BOD-FRIO-01", 1000.0, true));
        cooperativa.registrarBodega(new Bodega("BOD-SECA-01", 2000.0, false));

        try {
            cooperativa.cargarLoteInicial(new Cafe("CAF-01", "Don Pedro", fechaInicio.minusDays(2), 500.0, Calidad.A, 13.4), "BOD-SECA-01");
            cooperativa.cargarLoteInicial(new Hortaliza("HOR-01", "Occidente", fechaInicio.minusDays(5), 200.0, Calidad.A, 7), "BOD-FRIO-01");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n=== MENÚ ACOPIOGT ===");
            System.out.println("Fecha: " + cooperativa.getFechaOperacion());
            System.out.println("1. Ver inventario en bodegas");
            System.out.println("2. Registrar y despachar pedido");
            System.out.println("3. Avanzar fecha");
            System.out.println("4. Ver reportes financieros");
            System.out.println("5. Salir");
            System.out.print("Elija una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                if (opcion == 1) {
                    for (int i = 0; i < cooperativa.getBodegas().size(); i++) {
                        Bodega b = cooperativa.getBodegas().get(i);
                        System.out.println("Bodega: " + b.getCodigo() + " (Fría: " + b.isCamaraFria() + ")");
                        for (int j = 0; j < b.getLotesOrdenados().size(); j++) {
                            Lote l = b.getLotesOrdenados().get(j);
                            System.out.println("   - Lote: " + l.getCodigo() + " | Tipo: " + l.getClass().getSimpleName() + " | Disp: " + l.getPesoDisponible() + "kg | Estado: " + l.getEstado());
                        }
                    }
                } else if (opcion == 2) {
                    System.out.print("Escriba la categoría exacta (Cafe, CafeOrganico, Hortaliza, Cardamomo, QuesoFresco): ");
                    String cat = scanner.nextLine();
                    System.out.print("Kilos requeridos: ");
                    double kg = Double.parseDouble(scanner.nextLine());
                    cooperativa.despacharPedido(cat, kg);
                    System.out.println("¡Pedido despachado con éxito!");
                } else if (opcion == 3) {
                    System.out.print("Días a avanzar: ");
                    int d = Integer.parseInt(scanner.nextLine());
                    cooperativa.avanzarFecha(d);
                    System.out.println("Fecha actualizada correctamente.");
                } else if (opcion == 4) {
                    System.out.println("Total pagado: Q" + String.format("%.2f", cooperativa.getTotalPagado()));
                    System.out.println("Total cobrado: Q" + String.format("%.2f", cooperativa.getTotalCobrado()));
                    System.out.println("Total pérdidas: Q" + String.format("%.2f", cooperativa.getTotalPerdidas()));
                }
            } catch (Exception e) {
                System.out.println("[ERROR RECHAZADO] " + e.getMessage());
            }
        } while (opcion != 5);
        
        scanner.close();
    }
}