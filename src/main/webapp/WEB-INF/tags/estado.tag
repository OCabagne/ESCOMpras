<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag display-name="estado" pageEncoding="UTF-8" body-content="empty" import="escompras.util.EstadoUtil" %>

<%@attribute name="pedidoId" description="ID de la compra." required="true" type="java.lang.Integer" %>
<%@attribute name="estado" description="Estado of the pedido." required="true" type="java.lang.String" %>
<%@attribute name="isCliente" description="True cuando el usuario sea cliente."
	     required="true" type="java.lang.Boolean" %>

<select id="estado${ pedidoId }"
	onchange="ChangeEstado('${ pedidoId }')"
>
	<c:forEach var="estadoConst" items="${ EstadoUtil.getEstadoValues() }">
		<option value="${ estadoConst }"
			<c:if test="${ estado == estadoConst }">selected</c:if>>
			${ estadoConst.toString().toLowerCase() }
		</option>
	</c:forEach>
</select>
<span id="estadoLoading${ pedidoId }" class="d-none"></span>
<script>SetIsCliente(${ isCliente }, '${ pedidoId }')</script>
