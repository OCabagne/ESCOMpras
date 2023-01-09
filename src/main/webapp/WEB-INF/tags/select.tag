<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag display-name="select" pageEncoding="UTF-8" body-content="scriptless" %>

<%@attribute name="name" description="Input's name and ID." required="true" type="java.lang.String" %>
<%@attribute name="required" description="Is required?" type="java.lang.Boolean" %>
<%@attribute name="onchange" description="JS function to call on change." type="java.lang.String" %>
<%@attribute name="placeholder" description="Input's name and ID." type="java.lang.String" %>
<%@attribute name="items" description="Element list." type="java.util.List" required="true" %>
<%@attribute name="form" description="Input's form." type="java.lang.String" %>

<div class="mb-4" id="${name}Div">
	<div class="form-group row">
		<div class="col-sm-10">
			<select class="form-select" class="form-control col-sm-12"
				id="${name}" name="${name}"
				<c:out value="${ required? 'required' : '' }"/>
				onchange="${ onchange }"
				<c:if test="${ not empty form }">form="${ form }"</c:if>
			>
				<c:if test="${ not empty placeholder }">
					<option value="" selected disabled>${ placeholder }</option>
				</c:if>
				<c:forEach var="item" items="${ items }">
					<option class="dropdown-item">${ item }</option>
				</c:forEach>
			</select>
		</div>
	</div>
</div>