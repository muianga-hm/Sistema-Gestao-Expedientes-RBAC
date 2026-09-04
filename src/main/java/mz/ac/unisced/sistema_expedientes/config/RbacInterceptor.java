package mz.ac.unisced.sistema_expedientes.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RbacInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String uri = request.getRequestURI();

        // Permitir Login e Logout
        if (uri.equals("/login") || uri.equals("/logout")) {
            return true;
        }

        HttpSession session = request.getSession(false);

        // Verificar se existe sessão
        if (session == null) {
            response.sendRedirect("/login");
            return false;
        }

        // Verificar utilizador autenticado
        Object usuarioLogado = session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            response.sendRedirect("/login");
            return false;
        }

        // Obter o papel do utilizador
        String papel = (String) session.getAttribute("papel");

        // Menu principal
        if (uri.equals("/")) {
            return true;
        }

        // ADMIN tem acesso total
        if ("ADMIN".equals(papel)) {
            return true;
        }

        // Utilizadores não podem ser acessados por GESTOR/FUNCIONÁRIO
        if (uri.startsWith("/usuarios")) {
            response.sendRedirect("/");
            return false;
        }

        // Papéis não podem ser acessados por GESTOR/FUNCIONÁRIO
        if (uri.startsWith("/papeis")) {
            response.sendRedirect("/");
            return false;
        }

        // Permissões do GESTOR
        if ("GESTOR".equals(papel)) {

            if (uri.startsWith("/expedientes")
                    || uri.startsWith("/movimentacoes")
                    || uri.startsWith("/relatorios")
                    || uri.startsWith("/auditoria")) {

                return true;
            }
        }

        // Permissões do FUNCIONÁRIO
        if ("FUNCIONARIO".equals(papel)) {

            if (uri.startsWith("/expedientes")
                    || uri.startsWith("/movimentacoes")
                    || uri.startsWith("/auditoria")) {

                return true;
            }
        }

        // Acesso não autorizado
        response.sendRedirect("/");
        return false;
    }
}