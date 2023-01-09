<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag display-name="lib" pageEncoding="UTF-8" %>

<%@attribute name="gridOnly" description="Load only containers, rows and columns from Bootstrap." %>
<%@attribute name="rebootOnly" description="Load Bootstrap Reboot only." %>
<%@attribute name="sassUtilitiesOnly" description="Load Bootstrap Sass utilities only." %>
<%@attribute name="noPopper" description="Load Boostrap without tooltips, popovers and dropdowns." %>
<%@attribute name="estadoLib" description="Load the dropdown for Estado." %>

<base href="${ pageContext.request.contextPath }/"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<link rel="icon" href="favicon.ico"/>
<link rel="stylesheet"
	<c:if test="${ gridOnly }" var="prev" scope="page">
		href="lib/Bootstrap5/css/bootstrap-grid.min.css"
	</c:if>
	<c:if test="${ not prev and rebootOnly }" var="prev" scope="page">
		href="lib/Bootstrap5/css/bootstrap-reboot.min.css"
	</c:if>
	<c:if test="${ not prev and sassUtilitiesOnly }" var="prev" scope="page">
		href="lib/Bootstrap5/css/bootstrap-utilities.min.css"
	</c:if>
	<c:if test="${ not prev }" var="prev" scope="page">
		href="lib/Bootstrap5/css/bootstrap.min.css"
	</c:if>
/>
<link rel="stylesheet" href="lib/escompras.css"/>

<script
	<c:if test="${ noPopper ne null and noPopper}" var="prev" scope="page">
		src="lib/Bootstrap5/js/bootstrap.min.js"
	</c:if>
	<c:if test="${ not prev }">
		src="lib/Bootstrap5/js/bootstrap.bundle.min.js"
	</c:if>
></script>

<script src="lib/escompras.js"></script>
<c:if test="${ estadoLib ne null and estadoLib}">
	<script src="lib/estado.js"></script>
</c:if>
