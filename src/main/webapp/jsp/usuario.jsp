<%-- 
    Document   : usuario
    Created on : 6/07/2020, 06:50:01 PM
    Author     : Pc-Victor
--%>
<%@page import="dto.Usuario" %> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="soporte.soporteNegocio" %> 
<%@page import="dto.Solicitud" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Solicitudes UFPS - Usuario</title>

        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta name="keywords" content="Super Market Responsive web template, Bootstrap Web Templates, Flat Web Templates, Android Compatible web template, 
              Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design" />
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css" media="all" />
        <link href="css/style.css" rel="stylesheet" type="text/css" media="all" />
        <!-- font-awesome icons -->
        <link href="css/font-awesome.css" rel="stylesheet"> 
        <script src="js/jquery-1.11.1.min.js"></script>
        <link href='//fonts.googleapis.com/css?family=Raleway:400,100,100italic,200,200italic,300,400italic,500,500italic,600,600italic,700,700italic,800,800italic,900,900italic' rel='stylesheet' type='text/css'>
        <link href='//fonts.googleapis.com/css?family=Open+Sans:400,300,300italic,400italic,600,600italic,700,700italic,800,800italic' rel='stylesheet' type='text/css'>
        <script type="text/javascript" src="js/move-top.js"></script>
        <script type="text/javascript" src="js/easing.js"></script>
        <script type="text/javascript">
        </script>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
        <script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <link rel="stylesheet" href="../css/login.css">
        <link rel="stylesheet" href="../css/bootstrap.css">
        <link rel="stylesheet" href="../css/font-awesome.css">
        <link rel="stylesheet" href="../css/skdslider.css">

    </head>


    <% Usuario alsesion = (Usuario) request.getSession().getAttribute("usuario");

        if (alsesion != null) {

    %>

    <jsp:useBean id="soporte" class="soporte.soporteNegocio"/>

    <nav class="navbar navbar-expand-lg navbar-light fixed-top" id="mainNav">
        <div class="container">
            <div class="h-logo"><img style="background-color: transparent;" src="https://ww2.ufps.edu.co/public/archivos/elementos_corporativos/logo-horizontal.jpg" width="200" ></div>

            <ul class="nav navbar-nav navbar-right main-nav">
                <li><a href="#intro">Inicio</a></li>
                <li><a href="#Solicitud">Registrar Solicitud</a></li>
                <li><a href="#Listar">Listar Solicitudes</a></li>
                <li><a type="button" class="btn btn-secondary" href="cerrarSesion">Cerrar sesion</a></li>
                <li> <a> <%=alsesion.getNombre()%></a>  </li>

            </ul>

        </div>
    </nav>

    <section class="content-section bg-primary text-white text-center" id="intro">
        <div class="container">
            <div class="content-section-heading">
                <div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
                    <div class="carousel-inner">

                    </div>
                </div>
            </div>
        </div>

        <br>
        <br>
    </section>

    <section id="Solicitud">
        <div class="container">
            <h2>Registrar Solicitud</h2>
            <div class="grid_3 grid_5">
                <div class="bs-example bs-example-tabs" role="tabpanel" data-example-id="togglable-tabs">
                    <div id="myTabContent" class="tab-content">
                        <div role="tabpanel" class="tab-pane fade in active" id="expeditions" aria-labelledby="expeditions-tab">
                            <div class="row">
                                <div class="col-md-12">

                                    <form class="form-horizontal" action="${pageContext.request.contextPath}/registrarSolicitudCotroller" method="POST">

                                        <fieldset>


                                            <legend class="text-center header">Tu Solicitud aqui:</legend>

                                            <div class="form-group">
                                                <span class="col-md-1 col-md-offset-2 text-center"><i class="fa fa-user bigicon"></i></span>
                                                <div class="col-md-8">
                                                    <label for="formGroupExampleInput2">Dependencias </label> <br>
                                                    <select name="dependencia">
                                                        <c:forEach var="a" items="${soporte.getDependencia()}">
                                                            <option value="<c:out value="${a.id}"/>"> <c:out value="${a.descripcion}"/> </option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>



                                            <div class="form-group">
                                                <span class="col-md-1 col-md-offset-2 text-center"><i class="fa fa-user bigicon"></i></span>
                                                <div class="col-md-8">
                                                    <label for="formGroupExampleInput2">Estado </label> <br>
                                                    <select name="estado">
                                                        <c:forEach var="a" items="${soporte.getEstado()}">
                                                            <option value="<c:out value="${a.id}"/>"><c:out value="${a.descripcion}"/></option>

                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </div>



                                            <div class="form-group">
                                                <span class="col-md-1 col-md-offset-2 text-center"><i class="fa fa-user bigicon"></i></span>
                                                <div class="col-md-8">
                                                    <input id="Descripcion" name="descripcion " type="textarea" placeholder="Describa su Solicitud" class="form-control" />
                                                </div>
                                            </div>
                                            <!--                                            <div class="form-group">
                                                                                            <span class="col-md-1 col-md-offset-2 text-center"><i class="fa fa-user bigicon"></i></span>
                                                                                            <div class="col-md-8">
                                                                                                <select class="form-control" name="calificacion">
                                                                                                    <option selected >Calificacion</option>
                                                                                                    <option value="1">1</option>
                                                                                                    <option value="2">2</option>
                                                                                                    <option value="3">3</option>
                                                                                                    <option value="4">4</option>
                                                                                                    <option value="5">5</option>
                                                                                                </select>
                                                                                            </div>
                                                                                        </div>-->

                                            <div class="form-group">
                                                <span class="col-md-1 col-md-offset-2 text-center"><i class="fa fa-user bigicon"></i></span>
                                                <div class="col-md-8">
                                                    <form action="" method="post" enctype="multipart/form-data" target="_blank">
                                                        <p>
                                                            Sube un archivo:
                                                            <input type="file" name="archivosubido">
                                                        </p>

                                                    </form>

                                                </div>
                                            </div>

                                            <br>
                                            <br>
                                            <br>

                                            <div class="form-group">
                                                <div class="col-md-12 text-center">
                                                    <button type="submit" class="btn btn-primary btn-lg">Registrar Solicitud</button>
                                                </div>
                                            </div>
                                        </fieldset>
                                    </form>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <br>
    <br>
    <br>


    <section class="content-section bg-primary text-white text-center" id="intro">
        <div class="container">
            <div class="content-section-heading">
                <div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
                    <div class="carousel-inner">

                    </div>
                </div>
            </div>
        </div>

        <br>
        <br>
    </section>

    <section id="Listar">
        <div class="container">
            <h2>Listar Solicitudes</h2>
            <div class="grid_3 grid_5">
                <div class="bs-example bs-example-tabs" role="tabpanel" data-example-id="togglable-tabs">
                    <div id="myTabContent" class="tab-content">
                        <div role="tabpanel" class="tab-pane fade in active" id="expeditions" aria-labelledby="expeditions-tab">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="well well-sm">
                                        <div class="container">
                                            <table>
                                                <thead>
                                                    <tr><th>Id</th>
                                                        <th>Dependencia</th>
                                                        <th>Usuario</th>
                                                        <th>Fecha</th>
                                                        <th>Estado</th>
                                                        <th>Calificar</th>
                                                    </tr>
                                                </thead>


                                                <c:forEach var="solicitud"  items="${list}">
                                                    <tr>
                                                        <td><c:out value="${solicitud.id}"/></td>
                                                        <td><c:out value="${solicitud.dependencia.descripcion}"/></td>
                                                        <td><c:out value="${solicitud.usuario.usuario}"/></td>
                                                        <td><c:out value="${solicitud.fechasolicitud}"/></td>
                                                        <td><c:out value="${solicitud.estado.descripcion}"/></td>
                                                        <td><a href="usuarioController?action=showcalificar&id= <c:out value="${solicitud.id}"/>" data-toggle="modal" data-target="#myModal"> Calificar </a></td>
                                                        
                                                        <td><a href="usuarioController?action=eliminar&id=<c:out value="${solicitud.id}"/> "> Eliminar </a></td>
                                                    </tr>
                                                </c:forEach>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <br>
    <br>
    <br>

    <!--aqui-->

    <div class="modal" id="myModal">
        <div class="modal-dialog">
            <div class="modal-content">

                <!-- Modal Header -->
                <div class="modal-header">
                    <h4 class="modal-title">Calificar Solicitud</h4>


                </div>

                <form action="usuarioController?action=calificar"  >
                      <!-- Modal body -->
                      <div class="modal-body">
                      <select name="calificacion" class="form-control form-control-lg">
                            <option>1</option>
                            <option>2</option>
                            <option>3</option>
                            <option>4</option>
                            <option>5</option>
                        </select>
                </div>
                      <% String idcalificar=request.getParameter("idServlet"); %>

                      <input type="hidden" value="<%=idcalificar %>" name="idinput"   />
                      
                <input id="obervacion"  name="observacion " type="text" placeholder="observacion" class="form-control" >



                <!-- Modal footer -->
                <div class="modal-footer">
                    <button type="submit" class="btn btn-info" data-dismiss="modal">Calificar</button>
                    <button type="button" class="btn btn-danger" data-dismiss="modal">Cerrar</button>

                </div>
                </form>
            </div>
        </div>
    </div>

    <!---->


    <section class="content-section bg-primary text-white text-center" id="intro">
        <div class="container">
            <div class="content-section-heading">
                <div id="carouselExampleControls" class="carousel slide" data-ride="carousel">
                    <div class="carousel-inner">

                    </div>
                </div>
            </div>
        </div>

        <br>
        <br>
    </section>     




    <footer> Habilitacion web - 2020</footer>


    <%           } else {  %>

    <% response.sendRedirect("../index.html"); %>
    <%   }
    %>
</body>
</html>
