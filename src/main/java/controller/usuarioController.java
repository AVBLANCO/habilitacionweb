/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.exceptions.NonexistentEntityException;
import dto.Solicitud;
import dto.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import soporte.soporteNegocio;

/**
 *
 * @author Pc-Victor
 */
public class usuarioController extends HttpServlet {

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
            out.println("<title>Servlet usuarioController</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet usuarioController at " + request.getContextPath() + "</h1>");
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
        String action=request.getParameter("action");
        System.out.println("action : "+ action);
        soporteNegocio sn=new soporteNegocio();
        boolean calificacion =false;
        switch(action){
            
            case "eliminar":
        {
            try {
                sn.Eliminar(Integer.parseInt(request.getParameter("id")));
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(usuarioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
               Usuario u= (Usuario) request.getSession().getAttribute("usuario");
                request.getSession().setAttribute("usuario", u);
                request.getRequestDispatcher("./jsp/usuario.jsp").forward(request, response);
                break;
           case "calificar":
               int id=Integer.parseInt(request.getParameter("idinput"));
               String cal=request.getParameter("calificacion");
               Usuario u2= (Usuario) request.getSession().getAttribute("usuario");
               int c=Integer.parseInt(cal);
               String obs=request.getParameter("observacion");
               calificacion=sn.calificar(id, c, obs);
               if(calificacion){
                   request.setAttribute("list",sn.getSolicitudes(u2));
                   request.getSession().setAttribute("usuario", u2);
                   request.getRequestDispatcher("./jsp/usuario.jsp").forward(request, response);
                   
               }
               else{
                    
                   request.getRequestDispatcher("./jsp/error.jsp").forward(request, response);
                   
               }
               break;
               
               case "showcalificar":
                int idc=Integer.parseInt(request.getParameter("id"));
                   Solicitud s=sn.findSolicitud(idc);
                   Usuario u3= (Usuario) request.getSession().getAttribute("usuario");
                   request.setAttribute("list",sn.getSolicitudes(u3));
                   request.setAttribute("idServlet",s.getId());
                   request.getSession().setAttribute("usuario", u3);
                   request.getRequestDispatcher("./jsp/usuario.jsp").forward(request, response);
                   break;
            default: request.getRequestDispatcher("./jsp/error.jsp").forward(request, response);
               
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
