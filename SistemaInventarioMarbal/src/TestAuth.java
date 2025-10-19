import controlador.UsuarioController;
import modelo.Usuario;

public class TestAuth {
    public static void main(String[] args) {
        String user = args.length > 0 ? args[0] : "admin";
        String pass = args.length > 1 ? args[1] : "admin";

        UsuarioController uc = new UsuarioController();
        boolean ok = uc.cargarUsuariosDesdeBD();
        if (!ok) {
            System.out.println("[FAIL] No se pudieron cargar usuarios: " + uc.getLastError());
            System.exit(1);
        }
        Usuario u = uc.autenticar(user, pass);
        if (u != null) {
            System.out.println("[OK] Login correcto para usuario: " + u.getUsername() + ", rol=" + u.getRol());
            System.exit(0);
        } else {
            System.out.println("[FAIL] Credenciales incorrectas para username='" + user + "'.");
            System.out.println("Sugerencia: Asegúrate que 'usuario.password_hash' = SHA2('" + pass + "',256) y 'activo'=1.");
            System.exit(2);
        }
    }
}
