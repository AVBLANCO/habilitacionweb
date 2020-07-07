/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Dependencia;
import dto.Estado;
import dto.Solicitud;
import dto.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import soporte.soporteNegocio;

/**
 *
 * @author Pc-Victor
 */
public class registrarSolicitudCotroller extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet registrarSolicitudCotroller</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet registrarSolicitudCotroller at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dependencia=request.getParameter("dependencia");
        String estado=request.getParameter("estado");
        String descripcion=request.getParameter("descripcion");
        Usuario u=(Usuario) request.getSession().getAttribute("usuario");
        soporteNegocio sn=new soporteNegocio();
        Solicitud s=new Solicitud();
        int e=Integer.parseInt(estado);
        int d=Integer.parseInt(dependencia);
        Estado es=sn.findEstado(e);//revisar metodo
        Dependencia de=sn.findDependencia(d); //revisar metodo
        s.setEstado(es);
        s.setFechasolicitud(new Date());
        s.setDescripcion(descripcion);
        s.setDependencia(de);
        s.setUsuario(u);
        if(sn.registrarSolicitud(s)){
            request.getSession().setAttribute("usuario",u);
            request.getRequestDispatcher("./jsp/usuario.jsp").forward(request, response);
        }else{
            request.getRequestDispatcher("./jsp/error.jsp").forward(request, response);
       
        }
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
