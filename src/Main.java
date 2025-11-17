
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Aplicación para la gestión de usuarios mediante operaciones de serialización
 * y deserialización. Permite agregar, borrar, guardar, recuperar, mostrar y
 * exportar usuarios a un fichero de texto. El almacenamiento principal se
 * realiza en un archivo binario user.dat.
 *
 * @author Hinojos Morales
 * @version 1.0
 */
public class Main {

    private static ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private static boolean cambiosNoGuardados = false;
    private static final String ARCHIVO = "user.dat";
    private static final Scanner entrada = new Scanner(System.in);

    public static void main(String[] args) {

        File archivo = new File(ARCHIVO);

        if (archivo.exists()) {
            System.out.println("Archivo encontrado. Cargando usuarios...\n");
            recuperarDatos();
        } else {
            System.out.println("No existe archivo previo. La lista comienza vacía.\n");
        }

        int opcion;

        do {
            mostrarMenu();

            while (true) {
                try {
                    opcion = Integer.parseInt(entrada.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Debe escribir un número válido.\n");
                    System.out.print("Seleccione una opción: ");
                }
            }

            switch (opcion) {
                case 1 ->
                    agregarUsuario();
                case 2 ->
                    borrarUsuario();
                case 3 ->
                    guardarDatos();
                case 4 ->
                    recuperarDatos();
                case 5 ->
                    mostrarUsuarios();
                case 6 ->
                    exportarTXT();
                case 0 -> {
                    if (!salir()) {
                        opcion = -1;
                    }
                }
                default ->
                    System.out.println("Opción no válida.\n");
            }

        } while (opcion != 0);

        entrada.close();
        System.out.println("\nPrograma finalizado.");
    }

    private static void mostrarMenu() {
        System.out.print("""
                ------------------------------------------
                === MENÚ DE GESTIÓN DE USUARIOS ===
                ------------------------------------------
                1. Agregar usuario
                2. Borrar usuario
                3. Guardar usuarios en un archivo (serializar)
                4. Cargar lista desde archivo (deserializar)
                5. Mostrar usuarios
                6. Exportar usuarios a .txt

                0. Salir

                Seleccione una opción: """);
    }

    /**
     * Permite registrar un nuevo usuario solicitando sus datos por teclado y
     * validando cada campo antes de crear el objeto y guardarlo en memoria.
     */
    private static void agregarUsuario() {
        System.out.println("""
                       ------------------------
                       === AGREGAR USUARIO ===
                       ------------------------\n""");

        String identificador;
        while (true) {
            System.out.print("Introduce el identificador (4 números y 1 letra): ");
            identificador = entrada.nextLine();

            if (!identificador.matches("^[0-9]{4}[a-zA-Z]$")) {
                System.out.println("Error: el identificador debe ser 4 números y 1 letra.\n");
                continue;
            }

            if (existeIdentificador(identificador)) {
                System.out.println("Error: ya existe un usuario con ese identificador.\n");
                continue;
            }

            break; //
        }

        String contraseña;
        while (true) {
            System.out.print("Introduce la contraseña (6 caracteres): ");
            contraseña = entrada.nextLine();

            if (contraseña.length() == 6) {
                break;
            } else {
                System.out.println("Error: longitud inválida.\n");
            }
        }
        
        int anioNacimiento;
        while (true) {
            System.out.print("Introduce el año de nacimiento (4 dígitos): ");
            String input = entrada.nextLine();

            if (input.matches("^[0-9]{4}$")) {
                anioNacimiento = Integer.parseInt(input);
                break;
            } else {
                System.out.println("Ejemplo válido: 1998\n");
            }
        }

        String direccion;
        while (true) {
            System.out.print("Introduce la dirección (Ej: Calle Real Betis Balompié 116): ");
            direccion = entrada.nextLine();

            if (direccion.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ]+([A-Za-zÁÉÍÓÚáéíóúÑñ\\- ]+)*\\s+[0-9]+$")) {
                break;
            } else {
                System.out.println("Formato no válido. Ejemplo: Calle Los Olivos 42\n");
            }
        }
        
        listaUsuarios.add(new Usuario(identificador, contraseña, direccion, anioNacimiento));
        cambiosNoGuardados = true;
        System.out.println("\nUsuario agregado correctamente.\n");
    }

    /**
     * Elimina un usuario según el identificador introducido.
     */
    private static void borrarUsuario() {
        System.out.println("""
                       ------------------------
                       === BORRAR USUARIO ===
                       ------------------------\n""");

        if (listaUsuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.\n");
            return;
        }

        System.out.print("Introduce el identificador del usuario a eliminar: ");
        String id = entrada.nextLine();

        boolean encontrado = listaUsuarios.removeIf(u -> u.getIdentificador().equalsIgnoreCase(id));

        if (encontrado) {
            cambiosNoGuardados = true;
            System.out.println("Usuario eliminado correctamente.\n");
        } else {
            System.out.println("No se encontró un usuario con ese identificador.\n");
        }
    }

    /**
     * Guarda el contenido completo de listaUsuarios en un archivo binario.
     */
    private static void guardarDatos() {
        System.out.println("""
                       ------------------------
                       === GUARDAR DATOS ===
                       ------------------------\n""");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(listaUsuarios);
            cambiosNoGuardados = false;
            System.out.println("Datos guardados correctamente.\n");
        } catch (IOException e) {
            System.out.println("Error guardando los datos.\n");
        }
    }

    /**
     * Recupera listaUsuarios desde user.dat si existe y se confirma la carga.
     */
    private static void recuperarDatos() {
        System.out.println("""
                       -------------------------------
                       === CARGAR DATOS GUARDADOS ===
                       -------------------------------\n""");

        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) {
            System.out.println("No existe el archivo user.dat.\n");
            return;
        }

        if (cambiosNoGuardados) {
            System.out.print("Hay cambios sin guardar. ¿Continuar y perderlos? (S/N): ");
            if (!entrada.nextLine().equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada.\n");
                return;
            }
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
            listaUsuarios = (ArrayList<Usuario>) ois.readObject();
            cambiosNoGuardados = false;
            System.out.println("Datos cargados correctamente.\n");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los datos.\n");
        }
    }

    /**
     * Muestra todos los usuarios guardados actualmente en la lista.
     */
    private static void mostrarUsuarios() {
        System.out.println("""
                       ------------------------
                       === LISTA DE USUARIOS ===
                       ------------------------\n""");

        if (listaUsuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.\n");
            return;
        }

        listaUsuarios.forEach(System.out::println);
        System.out.println();
    }

    /**
     * Exporta los datos actuales de memoria a un archivo user.txt.
     */
    private static void exportarTXT() {
        System.out.println("""
                       ------------------------
                       === EXPORTAR A TXT ===
                       ------------------------\n""");

        if (listaUsuarios.isEmpty()) {
            System.out.println("No hay usuarios para exportar.\n");
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("user.txt"))) {

            for (Usuario u : listaUsuarios) {
                bw.write(u.toString());
                bw.newLine();
            }

            System.out.println("La lista de usuarios ha sido exportada correctamente a user.txt\n");

        } catch (IOException e) {
            System.out.println("Error al exportar el archivo de texto.\n");
        }
    }

    /**
     * Antes de salir del programa confirma si existen cambios no guardados.
     */
    private static boolean salir() {
        if (cambiosNoGuardados) {
            System.out.print("Hay cambios sin guardar. ¿Salir de todas formas? (S/N): ");
            if (!entrada.nextLine().equalsIgnoreCase("S")) {
                System.out.println("Operación cancelada. Regresando al menú.\n");
                return false;
            }
        }
        return true;
    }

    /**
     * Comprueba si existe ya un usuario con el identificador dado.
     *
     * @param id Identificador introducido
     * @return true si el ID ya existe, false en caso contrario
     */
    private static boolean existeIdentificador(String id) {
        for (Usuario u : listaUsuarios) {
            if (u.getIdentificador().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }
    
        private static void prueba() { 
        }


}
