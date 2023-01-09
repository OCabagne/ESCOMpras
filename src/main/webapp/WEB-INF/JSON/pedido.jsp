<%@page contentType="application/json" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>
<json:object>
	<%--@elvariable id="pedidos" type="java.util.List<escompras.model.entity.Orden>"--%>
	<json:array name="lista" var="pedido" items="${ pedidos }">
		<json:object>
			<json:property name="id" value="${ pedido.idOrden }"/>
			<json:property name="tienda" value="${ pedido.tienda.nombre }"/>
			<json:property name="cliente" value='${ pedido.cliente.nombre } ${ pedido.cliente.apellidos }'/>
			<json:property name="fecha" value="${ pedido.fecha }"/>
			<json:property name="escuela" value="${ pedido.escuela.nombre }"/>
			<json:property name="total" value="${ pedido.montoTotal }"/>
			<json:property name="estado" value="${ pedido.estado.name() }"/>
			<json:property name="horaEntrega" value="${ pedido.horaEntrega.toString() }"/>
		</json:object>
	</json:array>
</json:object>