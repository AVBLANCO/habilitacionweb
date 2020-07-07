/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Estado;
import dto.Solicitud;
import dto.Soporte;
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
public class responderSolicitudController extends HttpServlet {

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
            out.println("<title>Servlet responderSolicitudController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet responderSolicitudController at " + request.getContextPath() + "</h1>");
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
        int id = Integer.parseInt(request.getParameter("id"));

        String respuesta = request.getParameter("respuesta");
        Date fecha = new Date();
        String estado = request.getParameter("estado");
        Estado es = new Estado();
        es.setDescripcion(estado);
        String observacion = request.getParameter("observacion");
        
        Soporte tecnico=(Soporte)request.getSession().getAttribute("usuario");
       

        soporteNegocio c = new soporteNegocio();

        Solicitud s = c.solicitudBus(id);
       
        if (s != null) {
            s.setTecnico(tecnico);
            s.setFecharespuesta(fecha);
            s.setRespuesta(respuesta);
            s.setEstado(es);
            s.setObservaciones(observacion);
            c.responderSolicitud(s);
            request.getSession().setAttribute("usuario", tecnico);
            request.setAttribute("list", c.getSolicitudes());
            request.getRequestDispatcher("./jsp/usuarioDependiencia.jsp").forward(request, response);

            
        }
        else{
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
