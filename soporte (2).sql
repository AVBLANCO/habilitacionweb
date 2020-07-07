-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 07-07-2020 a las 23:09:20
-- Versión del servidor: 10.4.11-MariaDB
-- Versión de PHP: 7.4.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `soporte`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `adjunto`
--

CREATE TABLE `adjunto` (
  `id` int(11) NOT NULL,
  `solicitud` int(11) NOT NULL,
  `descripcion` varchar(100) DEFAULT NULL,
  `archivo` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dependencia`
--

CREATE TABLE `dependencia` (
  `id` int(11) NOT NULL,
  `descripcion` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `dependencia`
--

INSERT INTO `dependencia` (`id`, `descripcion`) VALUES
(1, 'Sistemas'),
(2, 'Infraestructura');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado`
--

CREATE TABLE `estado` (
  `id` int(11) NOT NULL,
  `descripcion` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `estado`
--

INSERT INTO `estado` (`id`, `descripcion`) VALUES
(1, 'Activo'),
(2, 'Solucionada'),
(3, 'Rechazada'),
(4, 'Cancelada');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `solicitud`
--

CREATE TABLE `solicitud` (
  `id` int(11) NOT NULL,
  `dependencia` int(11) DEFAULT NULL,
  `usuario` varchar(50) DEFAULT NULL,
  `fechasolicitud` timestamp NULL DEFAULT current_timestamp(),
  `estado` int(11) DEFAULT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `texto` text DEFAULT NULL,
  `fecharespuesta` timestamp NULL DEFAULT NULL,
  `respuesta` text DEFAULT NULL,
  `calificacion` int(11) DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  `tecnico` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `solicitud`
--

INSERT INTO `solicitud` (`id`, `dependencia`, `usuario`, `fechasolicitud`, `estado`, `descripcion`, `texto`, `fecharespuesta`, `respuesta`, `calificacion`, `observaciones`, `tecnico`) VALUES
(1, 1, 'marthaC', '2020-07-07 13:06:03', 1, 'habilitando Web', 'por no hacer las tareas... estoy habilitando', NULL, NULL, NULL, NULL, NULL),
(2, 2, 'alfredD', '2020-07-07 13:06:03', 1, 'voy a matar a camargo', 'por tener un pc tan lento', NULL, 'holisssss plisssss', NULL, 'no se que mas hacer', NULL),
(3, 1, 'anaB', '2020-07-07 13:07:03', 2, 'salida a vacaciones', 'plissss', NULL, NULL, NULL, NULL, NULL),
(4, 1, 'victorB', '2020-07-07 13:07:03', 1, 'pasamos web', 'con un 4', NULL, NULL, NULL, NULL, NULL),
(7, 1, 'marthaC', '2020-07-07 20:50:40', 2, NULL, NULL, NULL, NULL, 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `soporte`
--

CREATE TABLE `soporte` (
  `soporte` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `soporte`
--

INSERT INTO `soporte` (`soporte`) VALUES
('alfredD'),
('victorB');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `usuario` varchar(50) NOT NULL,
  `nombre` varchar(100) DEFAULT NULL,
  `clave` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`usuario`, `nombre`, `clave`, `email`) VALUES
('alfredD', 'Alfred Diaz ', 'a1234', 'alfreddiaz@ufps.edu.co'),
('anaB', 'ana Blanco', 'Ana12345', 'blanko1990@gmail.com'),
('marthaC', 'martha Chacin', 'm1234', 'marthaChacin@ufps.edu.co'),
('victorB', 'victor Manuel Blanco', 'Ana12345', 'victormanuelbm@ufps.edu.co');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `adjunto`
--
ALTER TABLE `adjunto`
  ADD PRIMARY KEY (`id`),
  ADD KEY `adjunto_ibfk_1` (`solicitud`);

--
-- Indices de la tabla `dependencia`
--
ALTER TABLE `dependencia`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `estado`
--
ALTER TABLE `estado`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `solicitud`
--
ALTER TABLE `solicitud`
  ADD PRIMARY KEY (`id`),
  ADD KEY `dependencia` (`dependencia`),
  ADD KEY `usuario` (`usuario`),
  ADD KEY `tecnico` (`tecnico`),
  ADD KEY `estado` (`estado`);

--
-- Indices de la tabla `soporte`
--
ALTER TABLE `soporte`
  ADD PRIMARY KEY (`soporte`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `adjunto`
--
ALTER TABLE `adjunto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `dependencia`
--
ALTER TABLE `dependencia`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `estado`
--
ALTER TABLE `estado`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `solicitud`
--
ALTER TABLE `solicitud`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `adjunto`
--
ALTER TABLE `adjunto`
  ADD CONSTRAINT `adjunto_ibfk_1` FOREIGN KEY (`solicitud`) REFERENCES `solicitud` (`id`);

--
-- Filtros para la tabla `solicitud`
--
ALTER TABLE `solicitud`
  ADD CONSTRAINT `solicitud_ibfk_1` FOREIGN KEY (`dependencia`) REFERENCES `dependencia` (`id`),
  ADD CONSTRAINT `solicitud_ibfk_2` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`usuario`),
  ADD CONSTRAINT `solicitud_ibfk_4` FOREIGN KEY (`tecnico`) REFERENCES `soporte` (`soporte`),
  ADD CONSTRAINT `solicitud_ibfk_5` FOREIGN KEY (`estado`) REFERENCES `estado` (`id`);

--
-- Filtros para la tabla `soporte`
--
ALTER TABLE `soporte`
  ADD CONSTRAINT `soporte_ibfk_1` FOREIGN KEY (`soporte`) REFERENCES `usuario` (`usuario`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
