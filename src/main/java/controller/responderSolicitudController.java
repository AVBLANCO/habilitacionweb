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
        String op = request.getParameter("action");
        System.out.println("opcion" + op);

        switch (op) {
            case "showresponder":
                int id = Integer.parseInt(request.getParameter("id"));
                System.out.println("case1" + id);
         
                 Usuario uss=(Usuario) request.getSession().getAttribute("usuario");
                 Soporte tecnico1=uss.getSoporte();
                 
                soporteNegocio c1 = new soporteNegocio();
                request.getSession().setAttribute("usuario", tecnico1);
                request.setAttribute("idd", id);
                System.out.println("case1" + c1);
                request.getSession().setAttribute("usuario", uss);
                request.setAttribute("list", c1.getSolicitudes());
                request.getRequestDispatcher("./jsp/usuarioDependiencia.jsp").forward(request, response);

                break;
        }
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
        protected void doPost
        (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            String op = request.getParameter("action");
            System.out.println("opcion" + op);

            switch (op) {
               
                case "respuesta":
                    int id2 = Integer.parseInt(request.getParameter("id"));
                    String respuesta = request.getParameter("respuesta");
                    Date fecha = new Date();
                    String estado = request.getParameter("estado");
                    Estado es = new Estado();
                    es.setDescripcion(estado);
                    String observacion = request.getParameter("observacion");

                    Soporte tecnico = (Soporte) request.getSession().getAttribute("usuario");

                    soporteNegocio c = new soporteNegocio();

                    Solicitud s = c.solicitudBus(id2);

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

                    } else {
                        request.getRequestDispatcher("./jsp/error.jsp").forward(request, response);
                    }
                    break;
            }
            System.out.println("");

        }

        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo
        
            () {
        return "Short description";
        }// </editor-fold>

    }
